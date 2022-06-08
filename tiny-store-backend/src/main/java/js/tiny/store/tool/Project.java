package js.tiny.store.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import js.tiny.store.dao.Database;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.template.SourceTemplate;

public class Project {
	private static final Log log = LogFactory.getLog(Project.class);

	private static final String JAVA_COMPILER = "1.8";

	private final HttpClientBuilder clientBuilder;
	private final RequestConfig requestConfig;

	private final Store store;
	private final Database dao;

	private final File projectDir;
	private final File runtimeDir;

	private final File serverWarFile;
	private final File clientJarFile;

	public Project(Context context, Store store, Database dao) throws IOException {
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

		// Tomcat uses pound (#) for multi-level context path
		// it is replaced with path separator: app#1.0 -> app/1.0 used as http://api.server/app/1.0/service/operation
		this.serverWarFile = new File(Files.serverTargetDir(projectDir), Strings.concat(store.getName(), '#', store.getVersion(), ".war"));
		// client jar uses 'store' suffix: app-store-1.0.jar
		this.clientJarFile = new File(Files.clientTargetDir(projectDir), Strings.concat(store.getName(), "-store-", store.getVersion(), ".jar"));

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
		generate("/parent-pom.xml.vtl", Files.parentPomFile(projectDir), properties("store", store));
		generate("/server-pom.xml.vtl", Files.serverPomFile(projectDir), properties("store", store));
		generate("/client-pom.xml.vtl", Files.clientPomFile(projectDir), properties("store", store));
		generate("/gitignore.vtl", Files.gitIgnoreFile(projectDir), properties());
		generate("/README.md.vtl", Files.readmeFile(projectDir), properties("store", store));
	}

	public boolean generateSources() throws IOException {
		generateProjectFiles();

		List<StoreEntity> entities = dao.getStoreEntities(store.id());
		for (StoreEntity entity : entities) {
			String className = Strings.concat(store.getPackageName(), '.', entity.getClassName());
			generate("/entity.java.vtl", entity, Files.serverSourceFile(projectDir, className));
			generate("/model.java.vtl", entity, Files.clientSourceFile(projectDir, className));
		}

		List<DataService> services = dao.getStoreServices(store.id());
		for (DataService service : services) {
			String interfaceName = Strings.concat(store.getPackageName(), '.', 'I', service.getClassName());
			String className = Strings.concat(store.getPackageName(), '.', service.getClassName());
			generate("/service-server-interface.java.vtl", service, Files.serverSourceFile(projectDir, interfaceName));
			generate("/service-implementation.java.vtl", service, Files.serverSourceFile(projectDir, className));
			generate("/service-client-interface.java.vtl", service, Files.clientSourceFile(projectDir, interfaceName));
		}

		generate("/web.xml.vtl", Files.webDescriptorFile(projectDir), properties("store", store));
		generate("/app.xml.vtl", Files.appDescriptorFile(projectDir), properties("store", store, "services", services));
		// TODO: hack on connection string ampersand escape
		generate("/context.xml.vtl", Files.contextFile(projectDir), properties("store", store, "connectionString", Strings.escapeXML(store.getConnectionString())));
		generate("/persistence.xml.vtl", Files.persistenceFile(projectDir), properties("store", store, "entities", entities));

		return !entities.isEmpty() || !services.isEmpty();
	}

	private void generate(String template, StoreEntity entity, File targetFile) throws IOException {
		SourceTemplate sourceTemplate = new SourceTemplate(template);
		try (Writer writer = new FileWriter(targetFile)) {
			sourceTemplate.generate(store, entity, writer);
		}
	}

	private void generate(String template, DataService service, File targetFile) throws IOException {
		SourceTemplate sourceFile = new SourceTemplate(template);
		List<ServiceOperation> operations = dao.getServiceOperations(service.id());
		try (Writer writer = new FileWriter(targetFile)) {
			sourceFile.generate(store, service, operations, writer);
		}
	}

	private static void generate(String template, File targetFile, Map<String, Object> properties) throws IOException {
		SourceTemplate sourceFile = new SourceTemplate(template);
		try (Writer writer = new FileWriter(targetFile)) {
			sourceFile.generate(properties, writer);
		}
	}

	private static Map<String, Object> properties(Object... values) {
		Map<String, Object> properties = new HashMap<>();
		for (int i = 0; i + 1 < values.length; i += 2) {
			properties.put((String) values[i], values[i + 1]);
		}
		return properties;
	}

	// --------------------------------------------------------------------------------------------

	public boolean compileServerSources() throws IOException {
		File librariesDir = new File(runtimeDir, "libx");
		if (!librariesDir.exists()) {
			librariesDir = new File(runtimeDir, "lib");
		}
		File[] libraries = new File[] { //
				new File(librariesDir, "js-jee-api-1.1.jar"), //
				new File(librariesDir, "js-transaction-api-1.3.jar") //
		};
		return compile(Files.serverSourceDir(projectDir), Files.serverClassDir(projectDir), libraries);
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

	public boolean compileClientSources() throws IOException {
		return compile(Files.clientSourceDir(projectDir), Files.clientClassDir(projectDir));
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
		Files.createJavaArchive(manifest, Files.clientClassDir(projectDir), clientJarFile);
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
}
