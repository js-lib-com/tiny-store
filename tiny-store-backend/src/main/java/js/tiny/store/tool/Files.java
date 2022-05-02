package js.tiny.store.tool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class Files extends js.util.Files {
	private static final String META_INF_DIR = "META-INF";
	private static final String WEB_INF_DIR = "WEB-INF";
	private static final String APP_DESCRIPTOR_FILE = "app.xml";
	private static final String WEB_DESCRIPTOR_FILE = "web.xml";
	private static final String CONTEXT_FILE = "context.xml";
	private static final String PERSISTENCE_FILE = "persistence.xml";

	public static void scanSources(File sourceDir, List<File> sourceFiles) {
		File[] files = sourceDir.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				scanSources(file, sourceFiles);
				continue;
			}
			if (file.getName().endsWith(".java")) {
				sourceFiles.add(file);
			}
		}
	}

	public static File sourceFile(File targetDir, String qualifiedName) throws IOException {
		File packageDir = new File(targetDir, Strings.getPackageName(qualifiedName).replace('.', File.separatorChar));
		if (!packageDir.exists() && !packageDir.mkdirs()) {
			throw new IOException("Fail to create package directory " + packageDir);
		}

		String className = Strings.getSimpleName(qualifiedName);
		return new File(packageDir, className + ".java");
	}

	public static File appDescriptorFile(File targetDir) throws IOException {
		return new File(webInfDir(targetDir), APP_DESCRIPTOR_FILE);
	}

	public static File webDescriptorFile(File targetDir) throws IOException {
		return new File(webInfDir(targetDir), WEB_DESCRIPTOR_FILE);
	}

	private static File webInfDir(File targetDir) throws IOException {
		File webInfDir = new File(targetDir, WEB_INF_DIR);
		if (!webInfDir.exists() && !webInfDir.mkdirs()) {
			throw new IOException("Fail to create WEB-INF directory " + webInfDir);
		}
		return webInfDir;
	}

	public static File contextFile(File targetDir) throws IOException {
		File metaInfDir = new File(targetDir, META_INF_DIR);
		if (!metaInfDir.exists() && !metaInfDir.mkdirs()) {
			throw new IOException("Fail to create META-INF directory " + metaInfDir);
		}
		return new File(metaInfDir, CONTEXT_FILE);
	}

	public static File persistenceFile(File targetDir) throws IOException {
		File metaInfDir = new File(targetDir, "WEB-INF/classes/META-INF");
		if (!metaInfDir.exists() && !metaInfDir.mkdirs()) {
			throw new IOException("Fail to create META-INF directory " + metaInfDir);
		}
		return new File(metaInfDir, PERSISTENCE_FILE);
	}

	/**
	 * Append file bytes to requested output stream and leave it not closed.
	 * 
	 * @param file
	 * @param outputStream
	 * @throws IOException
	 */
	public static void append(File file, OutputStream outputStream) throws IOException {
		byte[] buffer = new byte[4096];
		int length;
		try (InputStream fileStream = new BufferedInputStream(new FileInputStream(file))) {
			while ((length = fileStream.read(buffer, 0, buffer.length)) != -1) {
				outputStream.write(buffer, 0, length);
			}
		}
	}
}
