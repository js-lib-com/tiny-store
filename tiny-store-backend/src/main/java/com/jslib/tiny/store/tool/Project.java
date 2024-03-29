package com.jslib.tiny.store.tool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;

import com.jslib.tiny.store.Context;
import com.jslib.tiny.store.dao.Database;
import com.jslib.tiny.store.meta.DataService;
import com.jslib.tiny.store.meta.ServiceOperation;
import com.jslib.tiny.store.meta.Store;
import com.jslib.tiny.store.meta.StoreEntity;
import com.jslib.tiny.store.template.SourceTemplate;
import com.jslib.tiny.store.tool.IMavenClient.Coordinates;
import com.jslib.tiny.store.util.Files;
import com.jslib.tiny.store.util.Strings;

import com.jslib.api.log.Log;
import com.jslib.api.log.LogFactory;

public class Project {
	private static final Log log = LogFactory.getLog(Project.class);

	private final Store store;
	private final Database database;

	private final File projectDir;
	private final File runtimeDir;

	private final File serverWarFile;
	private final File clientJarFile;

	private final ISourceCompiler compiler;
	private final IMavenClient maven;

	public Project(Context context, Store store, Database database) throws IOException {
		this.store = store;
		this.database = database;

		// by convention project name is the store name
		this.projectDir = new File(context.getWorkspaceDir(), store.getName());
		this.runtimeDir = context.getRuntimeDir();

		// Tomcat uses pound (#) for multi-level context path
		// it is replaced with path separator: app#1.0 -> app/1.0 used as http://api.server/app/1.0/service/operation
		this.serverWarFile = new File(Files.serverTargetDir(projectDir), Strings.concat(store.getName(), '#', store.getVersion(), ".war"));
		// client jar uses 'store' suffix: app-store-1.0.jar
		this.clientJarFile = new File(Files.clientTargetDir(projectDir), Strings.concat(store.getName(), "-store-", store.getVersion(), ".jar"));

		this.compiler = new SourceCompiler();
		this.compiler.setVersion(ISourceCompiler.Version.JAVA_11);

		this.maven = new MavenClientImpl(context.getProperties());

		generateProjectFiles();
	}

	public Store getStore() {
		return store;
	}

	public File getProjectDir() {
		return projectDir;
	}

	public File getServerClassesDir() throws IOException {
		return Files.serverClassesDir(projectDir);
	}

	public void clean() throws IOException {
		Files.removeFilesHierarchy(Files.serverModuleDir(projectDir));
		Files.removeFilesHierarchy(Files.clientModuleDir(projectDir));
		generateProjectFiles();
	}

	private void generateProjectFiles() throws IOException {
		generate("/project-pom.xml.vtl", Files.projectPomFile(projectDir), store);
		generate("/server-pom.xml.vtl", Files.serverPomFile(projectDir), store);
		generate("/client-pom.xml.vtl", Files.clientPomFile(projectDir), store);
		generate("/gitignore.vtl", Files.gitIgnoreFile(projectDir));
		generate("/README.md.vtl", Files.readmeFile(projectDir), store);
	}

	public boolean generateSources() throws IOException {
		generateProjectFiles();

		List<StoreEntity> entities = database.getStoreEntities(store.id());
		for (StoreEntity entity : entities) {
			String className = Strings.concat(store.getPackageName(), '.', Strings.simpleName(entity.getClassName()));
			generate("/entity.java.vtl", Files.serverSourceFile(projectDir, className), entity);
			generate("/model.java.vtl", Files.clientSourceFile(projectDir, className), entity);
		}

		List<DataService> services = database.getStoreServices(store.id());
		Map<String, List<ServiceOperation>> serviceOperations = new HashMap<>();
		for (DataService service : services) {
			String simpleClassName = Strings.simpleName(service.getClassName());
			String interfaceName = Strings.concat(store.getPackageName(), '.', 'I', simpleClassName);
			String className = Strings.concat(store.getPackageName(), '.', simpleClassName);
			List<ServiceOperation> operations = database.getServiceOperations(service.id());
			serviceOperations.put(service.id(), operations);
			generate("/service-server-interface.java.vtl", Files.serverSourceFile(projectDir, interfaceName), store, service, operations);
			generate("/service-implementation.java.vtl", Files.serverSourceFile(projectDir, className), store, service, operations);
			generate("/service-client-interface.java.vtl", Files.clientSourceFile(projectDir, interfaceName), store, service, operations);
		}

		generate("/web.xml.vtl", Files.webDescriptorFile(projectDir), store);
		generate("/app.xml.vtl", Files.appDescriptorFile(projectDir), store, services);
		generate("/context.xml.vtl", Files.contextFile(projectDir), store);
		generate("/persistence.xml.vtl", Files.persistenceFile(projectDir), store, entities);

		generate("/manual.md.vtl", Files.manualFile(projectDir), store, services, serviceOperations, entities);

		return !entities.isEmpty() || !services.isEmpty();
	}

	private static void generate(String template, File targetFile, Object... arguments) throws IOException {
		SourceTemplate sourceFile = new SourceTemplate(template);
		try (Writer writer = new FileWriter(targetFile)) {
			sourceFile.generate(writer, arguments);
		}
	}

	public void generateSource(StoreEntity entity) throws IOException {
		generate("/entity.java.vtl", Files.serverSourceFile(projectDir, entity.getClassName()), entity);
		generate("/model.java.vtl", Files.clientSourceFile(projectDir, entity.getClassName()), entity);
	}

	// --------------------------------------------------------------------------------------------

	public String compileServerSources() throws IOException {
		File librariesDir = new File(runtimeDir, "libx");
		if (!librariesDir.exists()) {
			librariesDir = new File(runtimeDir, "lib");
		}
		File[] libraries = new File[] { //
				new File(librariesDir, "js-commons-1.3.1.jar"), //
				new File(librariesDir, "jakarta.jakartaee-api-9.1.0.jar"), //
				new File(librariesDir, "js-transaction-api-1.3.jar") //
		};
		return compiler.compile(Files.serverSourceDir(projectDir), Files.serverClassesDir(projectDir), libraries);
	}

	public void buildServerWar() throws IOException {
		Manifest manifest = new Manifest();
		// manifest version is critical; without it war file is properly generated but Tomcat refuses to process it
		manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
		manifest.getMainAttributes().putValue("Created-By", "Tiny Store");

		File warDir = Files.serverWarDir(projectDir, serverWarFile);
		Files.removeFilesHierarchy(warDir);

		Files.copy(Files.appDescriptorFile(projectDir), Files.warAppDescriptorFile(warDir));
		Files.copy(Files.persistenceFile(projectDir), Files.warPersistenceFile(warDir));
		Files.copy(Files.webDescriptorFile(projectDir), Files.warWebDescriptorFile(warDir));
		Files.copy(Files.contextFile(projectDir), Files.warContextFile(warDir));
		Files.copyFiles(Files.serverClassesDir(projectDir), Files.warClassDir(warDir));

		Files.createJavaArchive(manifest, warDir, serverWarFile);
	}

	public void deployServerWar() throws IOException {
		File webappsDir = new File(runtimeDir, "webapps");
		Files.copy(serverWarFile, new File(webappsDir, serverWarFile.getName()));
	}

	public void undeployServerWar() {
		File webappsDir = new File(runtimeDir, "webapps");
		File deployedWarFile = new File(webappsDir, serverWarFile.getName());
		log.info("Undeploy WAR |{file_path}|.", deployedWarFile);
		if (!deployedWarFile.delete()) {
			log.error("Fail to delete file |{file_path}|.", deployedWarFile);
		}
	}

	public String compileClientSources() throws IOException {
		return compiler.compile(Files.clientSourceDir(projectDir), Files.clientClassesDir(projectDir));
	}

	public void buildClientJar() throws IOException {
		Manifest manifest = new Manifest();
		manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
		manifest.getMainAttributes().putValue("Created-By", "Tiny Store");
		Files.createJavaArchive(manifest, Files.clientClassesDir(projectDir), clientJarFile);
	}

	public void deployClientJar() throws IOException {
		IMavenClient.Coordinates coordinates = new Coordinates(store.getPackageName(), store.getName() + "-store", store.getVersion());
		maven.deploy(store.getMavenServer(), coordinates, clientJarFile);
	}

	public void deleteSource(String className) throws IOException {
		File file = Files.serverSourceFile(projectDir, className);
		if (file.exists() && !file.delete()) {
			throw new IOException("Fail to delete server source file " + file);
		}

		file = Files.clientSourceFile(projectDir, className);
		if (file.exists() && !file.delete()) {
			throw new IOException("Fail to delete client source file " + file);
		}
	}

	public void deleteClass(String className) throws IOException {
		File file = Files.serverClassFile(projectDir, className);
		if (file.exists() && !file.delete()) {
			throw new IOException("Fail to delete server class file " + file);
		}

		file = Files.clientClassFile(projectDir, className);
		if (file.exists() && !file.delete()) {
			throw new IOException("Fail to delete client class file " + file);
		}
	}

	public String compileSources() throws IOException {
		String errorMessage = compileServerSources();
		if (errorMessage != null) {
			return errorMessage;
		}
		return compileClientSources();
	}
}
