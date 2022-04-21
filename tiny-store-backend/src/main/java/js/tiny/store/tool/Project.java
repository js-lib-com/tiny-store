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

import js.tiny.store.meta.Repository;
import js.tiny.store.meta.RepositoryEntity;
import js.tiny.store.meta.RepositoryService;
import js.util.Strings;

public class Project {
	private static final String SERVER_SOURCE_DIR = "server";
	private static final String CLIENT_SOURCE_DIR = "client";
	private static final String TARGET_DIR = "target";
	private static final String WAR_CLASSES_DIR = "/WEB-INF/classes";
	private static final String CLIENT_CLASSES_DIR = "target/client-classes";

	private String name;
	private String display;
	private String description;
	private String groupId;
	private String version;
	private String author;
	private Repository[] repositories;

	private transient final HttpClientBuilder clientBuilder;

	private transient File projectDir;
	private transient File runtimeDir;

	private transient File targetDir;

	private transient File serverSourceDir;
	private transient File warDir;
	private transient File warFile;
	private transient File warClassesDir;

	private transient File clientSourceDir;
	private transient File clientClassesDir;
	private transient File clientJarFile;

	public Project() {
		this.clientBuilder = HttpClientBuilder.create();
	}
	
	public void init(File projectDir, File runtimeDir) throws IOException {
		this.projectDir = projectDir;
		this.runtimeDir = runtimeDir;
		createFileSystem();
	}

	public String getName() {
		return name;
	}

	public String getDisplay() {
		return display;
	}

	public String getDescription() {
		return description;
	}

	public Repository[] getRepositories() {
		return repositories;
	}

	public void clean() throws IOException {
		Files.removeFilesHierarchy(serverSourceDir);
		Files.removeFilesHierarchy(clientSourceDir);
		Files.removeFilesHierarchy(targetDir);
		createFileSystem();
	}
	
	private void createFileSystem() throws IOException {
		this.serverSourceDir = new File(projectDir, SERVER_SOURCE_DIR);
		if (!this.serverSourceDir.exists() && !this.serverSourceDir.mkdirs()) {
			throw new IOException("Fail to create source directory " + this.serverSourceDir);
		}

		this.clientSourceDir = new File(projectDir, CLIENT_SOURCE_DIR);
		if (!this.clientSourceDir.exists() && !this.clientSourceDir.mkdirs()) {
			throw new IOException("Fail to create client source directory " + this.clientSourceDir);
		}

		this.targetDir = new File(projectDir, TARGET_DIR);
		this.warDir = new File(targetDir, name);
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

		// Tomcat uses pound (#) for multi-level context path 
		// it is replaced with path separator: app#1.0 -> app/1.0 used as http://api.server/app/1.0/service/operation
		this.warFile = new File(targetDir, Strings.concat(name, '#', version, ".war"));
		// client jar uses 'store' suffix: app-store-1.0.jar
		this.clientJarFile = new File(targetDir, Strings.concat(name, "-store-", version, ".jar"));
	}

	public void generateSources() throws IOException {
		for (Repository repository : repositories) {
			for (RepositoryEntity entity : repository.getEntities()) {
				entity.setAuthor(author);
				generate("/entity.java.vtl", Files.sourceFile(serverSourceDir, entity.getType()), "entity", entity);
				generate("/model.java.vtl", Files.sourceFile(clientSourceDir, entity.getType()), "entity", entity);
			}

			for (RepositoryService service : repository.getServices()) {
				service.setRepositoryName(repository.getName());
				service.setAuthor(author);
				generate("/service-remote.java.vtl", Files.sourceFile(serverSourceDir, service.getType(), true), "service", service);
				generate("/service-implementation.java.vtl", Files.sourceFile(serverSourceDir, service.getType()), "service", service);
				generate("/service-interface.java.vtl", Files.sourceFile(clientSourceDir, service.getType(), true), "service", service);
			}

			generate("/web.xml.vtl", Files.webDescriptorFile(warDir), "project", this);
			generate("/app.xml.vtl", Files.appDescriptorFile(warDir), "project", this);
			generate("/context.xml.vtl", Files.contextFile(warDir), "project", this);
			generate("/persistence.xml.vtl", Files.persistenceFile(warDir), "project", this);
		}
	}

	private static void generate(String template, File targetFile, String contextName, Object contextValue) throws IOException {
		SourceFile sourceFile = new SourceFile(template);
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
		String url = String.format("https://maven.js-lib.com/%s/%s-store/%s/%s", groupId.replace('.', '/'), name, version, clientJarFile.getName());
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
