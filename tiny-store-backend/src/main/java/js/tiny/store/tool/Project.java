package js.tiny.store.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import js.tiny.store.dao.IDAO;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.template.SourceTemplate;

public class Project {
	private static final String SERVER_SOURCE_DIR = "server";
	private static final String CLIENT_SOURCE_DIR = "client";
	private static final String TARGET_DIR = "target";
	private static final String WAR_CLASSES_DIR = "/WEB-INF/classes";
	private static final String CLIENT_CLASSES_DIR = "target/client-classes";

	private final HttpClientBuilder clientBuilder;
	private final Store store;
	private final IDAO dao;

	private final File projectDir;
	private final File runtimeDir;

	private final File targetDir;
	private final File serverSourceDir;
	private final File clientSourceDir;

	private final File warFile;
	private final File clientJarFile;

	private File warDir;
	private File warClassesDir;

	private File clientClassesDir;

	public Project(File projectDir, File runtimeDir, Store store, IDAO dao) throws IOException {
		this.clientBuilder = HttpClientBuilder.create();
		this.store = store;
		this.dao = dao;

		this.projectDir = projectDir;
		this.runtimeDir = runtimeDir;

		this.targetDir = new File(projectDir, TARGET_DIR);
		if (!this.targetDir.exists() && !this.targetDir.mkdirs()) {
			throw new IOException("Fail to create target directory " + this.targetDir);
		}

		this.serverSourceDir = new File(projectDir, SERVER_SOURCE_DIR);
		if (!this.serverSourceDir.exists() && !this.serverSourceDir.mkdirs()) {
			throw new IOException("Fail to create source directory " + this.serverSourceDir);
		}

		this.clientSourceDir = new File(projectDir, CLIENT_SOURCE_DIR);
		if (!this.clientSourceDir.exists() && !this.clientSourceDir.mkdirs()) {
			throw new IOException("Fail to create client source directory " + this.clientSourceDir);
		}

		// Tomcat uses pound (#) for multi-level context path
		// it is replaced with path separator: app#1.0 -> app/1.0 used as http://api.server/app/1.0/service/operation
		String storeName = Strings.getSimpleName(store.getPackageName());
		this.warFile = new File(targetDir, Strings.concat(storeName, '#', store.getVersion(), ".war"));
		// client jar uses 'store' suffix: app-store-1.0.jar
		this.clientJarFile = new File(targetDir, Strings.concat(storeName, "-store-", store.getVersion(), ".jar"));

		createFileSystem();
	}

	public void clean() throws IOException {
		Files.removeFilesHierarchy(serverSourceDir);
		Files.removeFilesHierarchy(clientSourceDir);
		Files.removeFilesHierarchy(targetDir);
		createFileSystem();
	}

	private void createFileSystem() throws IOException {
		this.warDir = new File(targetDir, Strings.getSimpleName(store.getPackageName()));
		if (!this.warDir.exists() && !this.warDir.mkdirs()) {
			throw new IOException("Fail to create output directory " + this.warDir);
		}

		this.warClassesDir = new File(warDir, WAR_CLASSES_DIR);
		if (!this.warClassesDir.exists() && !this.warClassesDir.mkdirs()) {
			throw new IOException("Fail to create classes directory " + this.warClassesDir);
		}

		this.clientClassesDir = new File(projectDir, CLIENT_CLASSES_DIR);
		if (!this.clientClassesDir.exists() && !this.clientClassesDir.mkdirs()) {
			throw new IOException("Fail to create client classes directory " + this.clientClassesDir);
		}
	}

	public void generateSources() throws IOException {
		for (StoreEntity entity : dao.findEntitiesByStore(store.getPackageName())) {
			generate("/entity.java.vtl", serverSourceDir, entity);
			generate("/model.java.vtl", clientSourceDir, entity);
		}

		for (DataService service : dao.findServicesByStore(store.getPackageName())) {
			generate("/service-remote.java.vtl", Files.sourceFile(serverSourceDir, service.getInterfaceName()), service);
			generate("/service-implementation.java.vtl", Files.sourceFile(serverSourceDir, service.getClassName()), service);
			generate("/service-interface.java.vtl", Files.sourceFile(clientSourceDir, service.getInterfaceName()), service);
		}

		generate("/web.xml.vtl", Files.webDescriptorFile(warDir), "project", this);
		generate("/app.xml.vtl", Files.appDescriptorFile(warDir), "project", this);
		generate("/context.xml.vtl", Files.contextFile(warDir), "project", this);
		generate("/persistence.xml.vtl", Files.persistenceFile(warDir), "project", this);
	}

	private static void generate(String template, File targetDir, StoreEntity entity) throws IOException {
		SourceTemplate sourceTemplate = new SourceTemplate(template);
		File sourceFile = Files.sourceFile(targetDir, entity.getClassName());
		try (Writer writer = new FileWriter(sourceFile)) {
			sourceTemplate.generate(entity, writer);
		}
	}

	private static void generate(String template, File targetFile, DataService service) throws IOException {
		SourceTemplate sourceFile = new SourceTemplate(template);
		try (Writer writer = new FileWriter(targetFile)) {
			sourceFile.generate(service, writer);
		}
	}

	private static void generate(String template, File targetFile, String contextName, Object contextValue) throws IOException {
		SourceTemplate sourceFile = new SourceTemplate(template);
		try (Writer writer = new FileWriter(targetFile)) {
			sourceFile.generate(contextName, contextValue, writer);
		}
	}

	public void compileSources() throws IOException {
		List<File> sourceFiles = new ArrayList<>();
		Files.scanSources(serverSourceDir, sourceFiles);

		File librariesDir = new File(runtimeDir, "libx");
		List<File> libraries = new ArrayList<>();
		libraries.add(new File(librariesDir, "js-jee-api-1.1.jar"));
		libraries.add(new File(librariesDir, "js-transaction-api-1.3.jar"));

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
			fileManager.setLocation(StandardLocation.CLASS_PATH, libraries);
			fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(warClassesDir));

			Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(sourceFiles);
			compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();
		}
	}

	public void buildWar() throws IOException {
		Manifest manifest = new Manifest();
		// manifest version is critical; without it war file is properly generated but Tomcat refuses to process it
		manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
		manifest.getMainAttributes().putValue("Created-By", "Tiny Store");

		try (JarOutputStream war = new JarOutputStream(new FileOutputStream(warFile), manifest)) {
			addArchiveEntries(war, warDir, warDir);
		}
	}

	public void deployWar() throws IOException {
		File webappsDir = new File(runtimeDir, "webapps");
		Files.copy(warFile, new File(webappsDir, warFile.getName()));
	}

	public void compileClientSources() throws IOException {
		List<File> sourceFiles = new ArrayList<>();
		Files.scanSources(clientSourceDir, sourceFiles);

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
			fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(clientClassesDir));

			Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(sourceFiles);
			compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();
		}
	}

	public void buildClientJar() throws IOException {
		Manifest manifest = new Manifest();
		manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
		manifest.getMainAttributes().putValue("Created-By", "Tiny Store");

		try (JarOutputStream war = new JarOutputStream(new FileOutputStream(clientJarFile), manifest)) {
			addArchiveEntries(war, clientClassesDir, clientClassesDir);
		}
	}

	public void deployClientJar() throws IOException {
		String url = String.format("https://maven.js-lib.com/%s-store/%s/%s", store.getPackageName().replace('.', '/'), store.getVersion(), clientJarFile.getName());
		try (CloseableHttpClient client = clientBuilder.build()) {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/octet-stream");
			httpPost.setEntity(new InputStreamEntity(new FileInputStream(clientJarFile)));

			try (CloseableHttpResponse response = client.execute(httpPost)) {
				if (response.getStatusLine().getStatusCode() != 200) {
					throw new IOException(String.format("Fail to upload file %s", clientJarFile));
				}
			}
		}
	}

	private void addArchiveEntries(JarOutputStream archive, File binariesDir, File dir) throws IOException {
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}

		for (File file : files) {
			if (file.isDirectory()) {
				addArchiveEntries(archive, binariesDir, file);
				continue;
			}
			JarEntry entry = new JarEntry(Files.getRelativePath(binariesDir, file, true));
			archive.putNextEntry(entry);
			Files.append(file, archive);
			// do not bother to close entry on exception since project build is aborted anyway
			archive.closeEntry();
		}
	}
}
