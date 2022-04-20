package js.tiny.store.tool;

import java.io.File;
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

import js.tiny.store.meta.Repository;
import js.tiny.store.meta.RepositoryEntity;
import js.tiny.store.meta.RepositoryService;

public class Project {
	private static final String SOURCE_DIR = "src";
	private static final String OUTPUT_DIR = "bin";
	private static final String CLASSES_DIR = OUTPUT_DIR + "/WEB-INF/classes";

	private String name;
	private String display;
	private String description;
	private String author;
	private Repository[] repositories;

	private transient File sourceDir;
	private transient File outputDir;
	private transient File classesDir;
	private transient File warFile;
	private transient File runtimeDir;

	public void init(File projectDir, File runtimeDir) throws IOException {
		this.runtimeDir = runtimeDir;
		this.sourceDir = new File(projectDir, SOURCE_DIR);

		if (!this.sourceDir.exists() && !this.sourceDir.mkdirs()) {
			throw new IOException("Fail to create source directory " + this.sourceDir);
		}

		this.outputDir = new File(projectDir, OUTPUT_DIR);
		if (!this.outputDir.exists() && !this.outputDir.mkdirs()) {
			throw new IOException("Fail to create output directory " + this.outputDir);
		}

		this.classesDir = new File(projectDir, CLASSES_DIR);
		if (!this.classesDir.exists() && !this.classesDir.mkdirs()) {
			throw new IOException("Fail to create classes directory " + this.classesDir);
		}

		this.warFile = new File(projectDir, name + ".war");
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

	public void generateSources() throws IOException {
		for (Repository repository : repositories) {
			for (RepositoryEntity entity : repository.getEntities()) {
				entity.setAuthor(author);
				generate("/entity.java.vtl", Files.sourceFile(sourceDir, entity.getType()), "entity", entity);
			}

			for (RepositoryService service : repository.getServices()) {
				service.setRepositoryName(repository.getName());
				service.setAuthor(author);
				generate("/service-remote.java.vtl", Files.sourceFile(sourceDir, service.getType(), true), "service", service);
				generate("/service-implementation.java.vtl", Files.sourceFile(sourceDir, service.getType()), "service", service);
			}

			generate("/web.xml.vtl", Files.webDescriptorFile(outputDir), "project", this);
			generate("/app.xml.vtl", Files.appDescriptorFile(outputDir), "project", this);
			generate("/context.xml.vtl", Files.contextFile(outputDir), "project", this);
			generate("/persistence.xml.vtl", Files.persistenceFile(outputDir), "project", this);
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
		Files.scanSources(sourceDir, sourceFiles);

		File librariesDir = new File(runtimeDir, "libx");
		List<File> libraries = new ArrayList<>();
		libraries.add(new File(librariesDir, "js-jee-api-1.1.jar"));
		libraries.add(new File(librariesDir, "js-transaction-api-1.3.jar"));

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
			fileManager.setLocation(StandardLocation.CLASS_PATH, libraries);
			fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(classesDir));

			Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(sourceFiles);
			compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();
		}
	}

	public void buildWar() throws IOException {
		Manifest manifest = new Manifest();
		// manifest version is critical; without it war file is properly generated but Tomcat refuses to process it
		manifest.getMainAttributes().putValue("Manifest-Version", "1.0");

		try (JarOutputStream war = new JarOutputStream(new FileOutputStream(warFile), manifest)) {
			addWarFiles(war, outputDir);
		}
	}

	private void addWarFiles(JarOutputStream war, File dir) throws IOException {
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}

		for (File file : files) {
			if (file.isDirectory()) {
				addWarFiles(war, file);
				continue;
			}
			JarEntry entry = new JarEntry(Files.getRelativePath(outputDir, file, true));
			war.putNextEntry(entry);
			Files.append(file, war);
			// do not bother to close entry on exception since project build is aborted anyway
			war.closeEntry();
		}
	}

	public void deployWar() throws IOException {
		File webappsDir = new File(runtimeDir, "webapps");
		Files.copy(warFile, new File(webappsDir, warFile.getName()));
	}
}
