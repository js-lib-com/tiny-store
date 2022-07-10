package js.tiny.store.tool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.jar.Manifest;

import js.log.Log;
import js.log.LogFactory;
import js.tiny.store.Context;
import js.tiny.store.dao.Database;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.template.SourceTemplate;
import js.tiny.store.tool.IMavenClient.Coordinates;
import js.tiny.store.util.Files;
import js.tiny.store.util.Strings;

public class Project {
	private static final Log log = LogFactory.getLog(Project.class);

	private final Store store;
	private final Database dao;

	private final File projectDir;
	private final File runtimeDir;

	private final File serverWarFile;
	private final File clientJarFile;

	private final ICompiler compiler;
	private final IMavenClient maven;

	public Project(Context context, Store store, Database dao) throws IOException {
		this.store = store;
		this.dao = dao;

		// by convention project name is the store name
		this.projectDir = new File(context.getWorkspaceDir(), store.getName());
		this.runtimeDir = context.getRuntimeDir();

		// Tomcat uses pound (#) for multi-level context path
		// it is replaced with path separator: app#1.0 -> app/1.0 used as http://api.server/app/1.0/service/operation
		this.serverWarFile = new File(Files.serverTargetDir(projectDir), Strings.concat(store.getName(), '#', store.getVersion(), ".war"));
		// client jar uses 'store' suffix: app-store-1.0.jar
		this.clientJarFile = new File(Files.clientTargetDir(projectDir), Strings.concat(store.getName(), "-store-", store.getVersion(), ".jar"));

		this.compiler = new CompilerImpl();
		this.compiler.setVersion(ICompiler.Version.JAVA_8);

		this.maven = new MavenClientImpl(context.getProperties());

		generateProjectFiles();
	}

	public File getProjectDir() {
		return projectDir;
	}

	public void clean() throws IOException {
		Files.removeFilesHierarchy(Files.serverModuleDir(projectDir));
		Files.removeFilesHierarchy(Files.clientModuleDir(projectDir));
		generateProjectFiles();
	}

	private void generateProjectFiles() throws IOException {
		generate("/parent-pom.xml.vtl", Files.parentPomFile(projectDir), store);
		generate("/server-pom.xml.vtl", Files.serverPomFile(projectDir), store);
		generate("/client-pom.xml.vtl", Files.clientPomFile(projectDir), store);
		generate("/gitignore.vtl", Files.gitIgnoreFile(projectDir));
		generate("/README.md.vtl", Files.readmeFile(projectDir), store);
	}

	public boolean generateSources() throws IOException {
		generateProjectFiles();

		List<StoreEntity> entities = dao.getStoreEntities(store.id());
		for (StoreEntity entity : entities) {
			String className = Strings.concat(store.getPackageName(), '.', Strings.simpleName(entity.getClassName()));
			generate("/entity.java.vtl", Files.serverSourceFile(projectDir, className), entity);
			generate("/model.java.vtl", Files.clientSourceFile(projectDir, className), entity);
		}

		List<DataService> services = dao.getStoreServices(store.id());
		for (DataService service : services) {
			String simpleClassName = Strings.simpleName(service.getClassName());
			String interfaceName = Strings.concat(store.getPackageName(), '.', 'I', simpleClassName);
			String className = Strings.concat(store.getPackageName(), '.', simpleClassName);
			List<ServiceOperation> operations = dao.getServiceOperations(service.id());
			generate("/service-server-interface.java.vtl", Files.serverSourceFile(projectDir, interfaceName), store, service, operations);
			generate("/service-implementation.java.vtl", Files.serverSourceFile(projectDir, className), store, service, operations);
			generate("/service-client-interface.java.vtl", Files.clientSourceFile(projectDir, interfaceName), store, service, operations);
		}

		generate("/web.xml.vtl", Files.webDescriptorFile(projectDir), store);
		generate("/app.xml.vtl", Files.appDescriptorFile(projectDir), store, services);
		// TODO: hack on connection string ampersand escape
		generate("/context.xml.vtl", Files.contextFile(projectDir), store);
		generate("/persistence.xml.vtl", Files.persistenceFile(projectDir), store, entities);

		return !entities.isEmpty() || !services.isEmpty();
	}

	private static void generate(String template, File targetFile, Object... arguments) throws IOException {
		SourceTemplate sourceFile = new SourceTemplate(template);
		try (Writer writer = new FileWriter(targetFile)) {
			sourceFile.generate(writer, arguments);
		}
	}

	// --------------------------------------------------------------------------------------------

	public String compileServerSources() throws IOException {
		File librariesDir = new File(runtimeDir, "libx");
		if (!librariesDir.exists()) {
			librariesDir = new File(runtimeDir, "lib");
		}
		File[] libraries = new File[] { //
				new File(librariesDir, "js-commons-1.3.1.jar"), //
				new File(librariesDir, "js-jee-api-1.1.jar"), //
				new File(librariesDir, "js-transaction-api-1.3.jar") //
		};
		return compiler.compile(Files.serverSourceDir(projectDir), Files.serverClassDir(projectDir), libraries);
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
		Files.copyFiles(Files.serverClassDir(projectDir), Files.warClassDir(warDir));

		Files.createJavaArchive(manifest, warDir, serverWarFile);
	}

	public void deployServerWar() throws IOException {
		File webappsDir = new File(runtimeDir, "webapps");
		Files.copy(serverWarFile, new File(webappsDir, serverWarFile.getName()));
	}

	public void undeployServerWar() {
		File webappsDir = new File(runtimeDir, "webapps");
		File deployedWarFile = new File(webappsDir, serverWarFile.getName());
		log.info("Undeploy WAR |%s|.", deployedWarFile);
		if (!deployedWarFile.delete()) {
			log.error("Fail to delete file |%s|.", deployedWarFile);
		}
	}

	public String compileClientSources() throws IOException {
		return compiler.compile(Files.clientSourceDir(projectDir), Files.clientClassDir(projectDir));
	}

	public void buildClientJar() throws IOException {
		Manifest manifest = new Manifest();
		manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
		manifest.getMainAttributes().putValue("Created-By", "Tiny Store");
		Files.createJavaArchive(manifest, Files.clientClassDir(projectDir), clientJarFile);
	}

	public void deployClientJar() throws IOException {
		IMavenClient.Coordinates coordinates = new Coordinates(store.getPackageName(), store.getName() + "-store", store.getVersion());
		maven.deploy(store.getMavenServer(), coordinates, clientJarFile);
	}
}
