package js.tiny.store.tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import js.log.Log;
import js.log.LogFactory;
import js.tiny.store.util.Files;

public class SourceCompiler implements ISourceCompiler {
	private static final Log log = LogFactory.getLog(SourceCompiler.class);

	private static final File[] EMPTY_LIBRARIES = new File[0];

	private Version version;

	public SourceCompiler() {
		version = Version.JAVA_11;
	}

	@Override
	public void setVersion(Version version) {
		this.version = version;
	}

	@Override
	public String compile(File sourceDir, File classDir) throws IOException {
		return compile(sourceDir, classDir, EMPTY_LIBRARIES);
	}

	@Override
	public String compile(File sourceDir, File classDir, File[] libraries) throws IOException {
		List<File> sourceFiles = new ArrayList<>();
		Files.scanSources(sourceDir, sourceFiles);

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StringBuilder diagnosticBuilder = new StringBuilder();
		DiagnosticListener<JavaFileObject> diagnosticListener = diagnostic -> {
			diagnosticBuilder.append(diagnostic);
		};
		List<String> options = Arrays.asList("-source", version.value(), "-target", version.value());

		try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
			if (libraries.length > 0) {
				fileManager.setLocation(StandardLocation.CLASS_PATH, Arrays.asList(libraries));
			}
			fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(classDir));

			Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(sourceFiles);
			Boolean result = compiler.getTask(null, fileManager, diagnosticListener, options, null, compilationUnits).call();
			if (result == null || !result) {
				log.warn("Compilation error on client sources: %s", sourceFiles);
				log.error(diagnosticBuilder.toString());
				return diagnosticBuilder.toString();
			}
			return null;
		}
	}

	@Override
	public File compile(File sourceFile, File[] libraries) throws IOException {
		StringBuilder diagnosticBuilder = new StringBuilder();
		DiagnosticListener<JavaFileObject> diagnosticListener = diagnostic -> {
			diagnosticBuilder.append(diagnostic);
		};

		List<File> sourceFiles = Arrays.asList(sourceFile); 
		File classDirectory = java.nio.file.Files.createTempDirectory("class").toFile();

		compile(sourceFiles, classDirectory, Arrays.asList(libraries), diagnosticListener);
	
		return null;
	}

	private boolean compile(List<File> sourceFiles, File classDirectory, List<File> libraries, DiagnosticListener<JavaFileObject> diagnosticListener) throws IOException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		List<String> options = Arrays.asList("-source", version.value(), "-target", version.value());
		try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
			if (!libraries.isEmpty()) {
				fileManager.setLocation(StandardLocation.CLASS_PATH, libraries);
			}
			fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(classDirectory));

			Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(sourceFiles);
			Boolean result = compiler.getTask(null, fileManager, diagnosticListener, options, null, compilationUnits).call();
			if (result == null || !result) {
				log.warn("Compilation error on client sources: %s", sourceFiles);
				return false;
			}
		}
		return true;
	}
}
