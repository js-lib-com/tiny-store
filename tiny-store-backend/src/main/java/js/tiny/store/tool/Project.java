package js.tiny.store.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import js.log.Log;
import js.log.LogFactory;
import js.tiny.store.Context;
import js.tiny.store.dao.IDAO;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.template.SourceTemplate;

public class Project {
	private static final Log log = LogFactory.getLog(Project.class);

	private static final String SERVER_SOURCE_DIR = "server";
	private static final String CLIENT_SOURCE_DIR = "client";
	private static final String TARGET_DIR = "target";
	private static final String WAR_CLASSES_DIR = "/WEB-INF/classes";
	private static final String CLIENT_CLASSES_DIR = "target/client-classes";
	private static final String JAVA_COMPILER = "1.8";

	private final HttpClientBuilder clientBuilder;
	private final RequestConfig requestConfig;

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

	public Project(Context context, Store store, IDAO dao) throws IOException {
		HttpClientBuilder clientBuilder = HttpClients.custom();
		RequestConfig.Builder configBuilder = RequestConfig.custom();
		if (context.hasProxy()) {
			HttpHost proxy = new HttpHost(context.getProxyHost(), context.getProxyPort(), context.getProxyProtocol());
			configBuilder.setProxy(proxy);
		}
		this.requestConfig = configBuilder.build();

		if (context.isProxySecure()) {
			CredentialsProvider credentials = new BasicCredentialsProvider();
			credentials.setCredentials(new AuthScope(context.getProxyHost(), context.getProxyPort()), new UsernamePasswordCredentials(context.getProxyUser(), context.getProxyPassword()));
			clientBuilder = clientBuilder.setDefaultCredentialsProvider(credentials);
		}
		this.clientBuilder = clientBuilder;

		this.store = store;
		this.dao = dao;

		// by convention project name is the store name
		this.projectDir = new File(context.getWorkspaceDir(), store.getName());
		this.runtimeDir = context.getRuntimeDir();

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
		this.warFile = new File(targetDir, Strings.concat(store.getName(), '#', store.getVersion(), ".war"));
		// client jar uses 'store' suffix: app-store-1.0.jar
		this.clientJarFile = new File(targetDir, Strings.concat(store.getName(), "-store-", store.getVersion(), ".jar"));

		createFileSystem();
	}

	public void clean() throws IOException {
		Files.removeFilesHierarchy(serverSourceDir);
		Files.removeFilesHierarchy(clientSourceDir);
		Files.removeFilesHierarchy(targetDir);
		createFileSystem();
	}

	private void createFileSystem() throws IOException {
		this.warDir = new File(targetDir, store.getName());
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

	public boolean generateSources() throws IOException {
		List<StoreEntity> entities = dao.findEntitiesByStore(store.getId().toHexString());
		for (StoreEntity entity : entities) {
			generate("/entity.java.vtl", serverSourceDir, entity);
			generate("/model.java.vtl", clientSourceDir, entity);
		}

		List<DataService> services = dao.findServicesByStore(store.getId().toHexString());
		for (DataService service : services) {
			generate("/service-remote.java.vtl", Files.sourceFile(serverSourceDir, service.getInterfaceName()), store.getName(), service);
			generate("/service-implementation.java.vtl", Files.sourceFile(serverSourceDir, service.getClassName()), store.getName(), service);
			generate("/service-interface.java.vtl", Files.sourceFile(clientSourceDir, service.getInterfaceName()), store.getName(), service);
		}

		generate("/web.xml.vtl", Files.webDescriptorFile(warDir));
		generate("/app.xml.vtl", Files.appDescriptorFile(warDir));
		// TODO: hack on connection string ampersand escape
		generate("/context.xml.vtl", Files.contextFile(warDir), properties("store", store, "connectionString", Strings.escapeXML(store.getConnectionString())));
		generate("/persistence.xml.vtl", Files.persistenceFile(warDir), properties("store", store, "entities", entities));

		return !entities.isEmpty() || !services.isEmpty();
	}

	private static Map<String, Object> properties(Object... values) {
		Map<String, Object> properties = new HashMap<>();
		for (int i = 0; i < values.length; i += 2) {
			properties.put((String) values[i], values[i + 1]);
		}
		return properties;
	}

	private void generate(String template, File targetDir, StoreEntity entity) throws IOException {
		SourceTemplate sourceTemplate = new SourceTemplate(template);
		File sourceFile = Files.sourceFile(targetDir, entity.getClassName());
		try (Writer writer = new FileWriter(sourceFile)) {
			sourceTemplate.generate(entity, writer);
		}
	}

	private void generate(String template, File targetFile, String repositoryName, DataService service) throws IOException {
		SourceTemplate sourceFile = new SourceTemplate(template);
		List<ServiceOperation> operations = dao.findServiceOperations(service.getId().toHexString());
		try (Writer writer = new FileWriter(targetFile)) {
			sourceFile.generate(repositoryName, service, operations, writer);
		}
	}

	private void generate(String template, File targetFile) throws IOException {
		SourceTemplate sourceFile = new SourceTemplate(template);

		List<DataService> services = dao.findServicesByStore(store.getId().toHexString());

		try (Writer writer = new FileWriter(targetFile)) {
			sourceFile.generate(services, writer);
		}
	}

	private void generate(String template, File targetFile, Map<String, Object> properties) throws IOException {
		SourceTemplate sourceFile = new SourceTemplate(template);
		try (Writer writer = new FileWriter(targetFile)) {
			sourceFile.generate(properties, writer);
		}
	}

	public boolean compileSources() throws IOException {
		File librariesDir = new File(runtimeDir, "libx");
		if (!librariesDir.exists()) {
			librariesDir = new File(runtimeDir, "lib");
		}
		return compile(serverSourceDir, warClassesDir, new File(librariesDir, "js-jee-api-1.1.jar"), new File(librariesDir, "js-transaction-api-1.3.jar"));
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

	public void undeployWar() {
		File webappsDir = new File(runtimeDir, "webapps");
		File deployedWarFile = new File(webappsDir, warFile.getName());
		log.info("Undeploy WAR |%s|.", deployedWarFile);
		if (!deployedWarFile.delete()) {
			log.error("Fail to delete file |%s|.", deployedWarFile);
		}
	}

	public boolean compileClientSources() throws IOException {
		return compile(clientSourceDir, clientClassesDir);
	}

	private static boolean compile(File sourceDir, File classDir, File... libraries) throws IOException {
		List<File> sourceFiles = new ArrayList<>();
		Files.scanSources(sourceDir, sourceFiles);

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticListener<JavaFileObject> diagnosticListener = diagnostic -> {
			System.out.println(diagnostic);
		};
		List<String> options = Arrays.asList("-source", JAVA_COMPILER, "-target", JAVA_COMPILER);

		try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
			if (libraries.length > 0) {
				fileManager.setLocation(StandardLocation.CLASS_PATH, Arrays.asList(libraries));
			}
			fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(classDir));

			Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(sourceFiles);
			Boolean result = compiler.getTask(null, fileManager, diagnosticListener, options, null, compilationUnits).call();
			if (result == null || !result) {
				log.warn("Compilation error on client sources: %s", sourceFiles);
				return false;
			}
			return true;
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
		String url = String.format("https://maven.js-lib.com/%s/%s-store/%s/%s", store.getPackageName().replace('.', '/'), store.getName(), store.getVersion(), clientJarFile.getName());
		log.debug("Deploy client JAR to |%s|.", url);

		try (CloseableHttpClient client = clientBuilder.build()) {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
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
