package com.jslib.tiny.store.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class Files extends js.util.Files {
	private static final String PROJECT_POM_FILE = "pom.xml";
	private static final String GIT_IGNORE_FILE = ".gitignore";
	private static final String README_FILE = "README.md";
	private static final String MANUAL_FILE = "manual.md";

	private static final String CLIENT_MODULE_DIR = "client/";
	private static final String CLIENT_POM_FILE = "client/pom.xml";
	private static final String CLIENT_SOURCE_DIR = "client/src/main/java";
	private static final String CLIENT_TARGET_DIR = "client/target/";
	private static final String CLIENT_CLASS_DIR = "client/target/classes/";

	private static final String SERVER_MODULE_DIR = "server/";
	private static final String SERVER_POM_FILE = "server/pom.xml";
	private static final String SERVER_SOURCE_DIR = "server/src/main/java";
	private static final String SERVER_TARGET_DIR = "server/target/";
	private static final String SERVER_CLASS_DIR = "server/target/classes/";

	private static final String PERSISTENCE_FILE = "server/src/main/resources/META-INF/persistence.xml";
	private static final String CONTEXT_FILE = "server/src/main/webapp/META-INF/context.xml";
	private static final String APP_DESCRIPTOR_FILE = "server/src/main/webapp/WEB-INF/app.xml";
	private static final String WEB_DESCRIPTOR_FILE = "server/src/main/webapp/WEB-INF/web.xml";
	private static final String WEB_CLASS_DIR = "server/src/main/webapp/WEB-INF/classes/";

	private static final String WAR_CONTEXT_FILE = "META-INF/context.xml";
	private static final String WAR_APP_DESCRIPTOR_FILE = "WEB-INF/app.xml";
	private static final String WAR_WEB_DESCRIPTOR_FILE = "WEB-INF/web.xml";
	private static final String WAR_PERSISTENCE_FILE = "WEB-INF/classes/META-INF/persistence.xml";
	private static final String WAR_CLASS_DIR = "WEB-INF/classes/";

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

	public static File projectPomFile(File projectDir) throws IOException {
		return file(projectDir, PROJECT_POM_FILE);
	}

	public static File clientPomFile(File projectDir) throws IOException {
		return file(projectDir, CLIENT_POM_FILE);
	}

	public static File serverPomFile(File projectDir) throws IOException {
		return file(projectDir, SERVER_POM_FILE);
	}

	public static File gitIgnoreFile(File projectDir) throws IOException {
		return file(projectDir, GIT_IGNORE_FILE);
	}

	public static File readmeFile(File projectDir) throws IOException {
		return file(projectDir, README_FILE);
	}

	public static File manualFile(File projectDir) throws IOException {
		return file(projectDir, MANUAL_FILE);
	}

	public static File clientModuleDir(File projectDir) throws IOException {
		return dir(projectDir, CLIENT_MODULE_DIR);
	}

	public static File clientSourceDir(File projectDir) throws IOException {
		return dir(projectDir, CLIENT_SOURCE_DIR);
	}

	public static File clientTargetDir(File projectDir) throws IOException {
		return dir(projectDir, CLIENT_TARGET_DIR);
	}

	public static File clientSourceFile(File projectDir, String className) throws IOException {
		return file(clientSourceDir(projectDir), className.replace('.', '/') + ".java");
	}

	public static File serverModuleDir(File projectDir) throws IOException {
		return dir(projectDir, SERVER_MODULE_DIR);
	}

	public static File serverSourceDir(File projectDir) throws IOException {
		return dir(projectDir, SERVER_SOURCE_DIR);
	}

	public static File serverTargetDir(File projectDir) throws IOException {
		return dir(projectDir, SERVER_TARGET_DIR);
	}

	public static File serverSourceFile(File projectDir, String className) throws IOException {
		return file(serverSourceDir(projectDir), className.replace('.', '/') + ".java");
	}

	public static File clientClassesDir(File projectDir) throws IOException {
		return dir(projectDir, CLIENT_CLASS_DIR);
	}

	public static File clientClassFile(File projectDir, String className) throws IOException {
		return file(clientClassesDir(projectDir), className.replace('.', '/') + ".class");
	}

	public static File serverClassesDir(File projectDir) throws IOException {
		return dir(projectDir, SERVER_CLASS_DIR);
	}

	public static File serverClassFile(File projectDir, String className) throws IOException {
		return file(serverClassesDir(projectDir), className.replace('.', '/') + ".class");
	}

	/**
	 * Sub-directory from server target directory where exploded WAR is built. By convention WAR directory has the WAR file base
	 * name.
	 * 
	 * @param projectDir
	 * @param warFile
	 * @return
	 * @throws IOException
	 */
	public static File serverWarDir(File projectDir, File warFile) throws IOException {
		return dir(serverTargetDir(projectDir), Files.basename(warFile));
	}

	public static File persistenceFile(File projectDir) throws IOException {
		return file(projectDir, PERSISTENCE_FILE);
	}

	public static File contextFile(File projectDir) throws IOException {
		return file(projectDir, CONTEXT_FILE);
	}

	public static File appDescriptorFile(File projectDir) throws IOException {
		return file(projectDir, APP_DESCRIPTOR_FILE);
	}

	public static File webDescriptorFile(File projectDir) throws IOException {
		return file(projectDir, WEB_DESCRIPTOR_FILE);
	}

	public static File webClassDir(File projectDir) throws IOException {
		return dir(projectDir, WEB_CLASS_DIR);
	}

	public static File warWebDescriptorFile(File warDir) throws IOException {
		return file(warDir, WAR_WEB_DESCRIPTOR_FILE);
	}

	public static File warAppDescriptorFile(File warDir) throws IOException {
		return file(warDir, WAR_APP_DESCRIPTOR_FILE);
	}

	public static File warContextFile(File warDir) throws IOException {
		return file(warDir, WAR_CONTEXT_FILE);
	}

	public static File warPersistenceFile(File warDir) throws IOException {
		return file(warDir, WAR_PERSISTENCE_FILE);
	}

	public static File warClassDir(File warDir) throws IOException {
		return dir(warDir, WAR_CLASS_DIR);
	}

	private static File dir(File projectDir, String path) throws IOException {
		File dir = new File(projectDir, path);
		if (!dir.exists() && !dir.mkdirs()) {
			throw new IOException("Fail to create directory " + dir);
		}
		return dir;
	}

	private static File file(File projectDir, String path) throws IOException {
		File file = new File(projectDir, path);
		File parentDir = file.getParentFile();
		if (!parentDir.exists() && !parentDir.mkdirs()) {
			throw new IOException("Fail to create directory " + parentDir);
		}
		return file;
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

	public static void copyFiles(File sourceDir, File targetDir) throws IOException {
		copyFiles(sourceDir, sourceDir, targetDir);
	}

	private static void copyFiles(File baseDir, File currentSourceDir, File targetDir) throws IOException {
		File[] sourceFiles = currentSourceDir.listFiles();
		if (sourceFiles == null) {
			throw new IOException("Fail to list source directory " + currentSourceDir);
		}

		for (File sourceFile : sourceFiles) {
			if (sourceFile.isDirectory()) {
				copyFiles(baseDir, sourceFile, targetDir);
				continue;
			}
			File targetFile = new File(targetDir, Files.getRelativePath(baseDir, sourceFile));
			targetFile.getParentFile().mkdirs();
			Files.copy(sourceFile, targetFile);
		}
	}

	public static void createJavaArchive(Manifest manifest, File explodedSourceArchiveDir, File targetArchiveFile) throws IOException {
		final File baseDir = explodedSourceArchiveDir;
		final File currentDir = explodedSourceArchiveDir;
		try (JarOutputStream archiveOutputStream = new JarOutputStream(new FileOutputStream(targetArchiveFile), manifest)) {
			addArchiveEntries(archiveOutputStream, baseDir, currentDir);
		}
	}

	/**
	 * Recursively add files from source directory as entry to target Java archive.
	 * 
	 * @param archive archive output stream,
	 * @param baseDir base directory for source files tree,
	 * @param currentDir current directory from source files tree.
	 * @throws IOException
	 */
	private static void addArchiveEntries(JarOutputStream archive, File baseDir, File currentDir) throws IOException {
		File[] files = currentDir.listFiles();
		if (files == null) {
			return;
		}

		for (File file : files) {
			if (file.isDirectory()) {
				addArchiveEntries(archive, baseDir, file);
				continue;
			}
			// uses base directory to create relative paths for archive entry
			JarEntry entry = new JarEntry(Files.getRelativePath(baseDir, file, true));
			try {
				archive.putNextEntry(entry);
				Files.append(file, archive);
			} finally {
				archive.closeEntry();
			}
		}
	}

	public static File classFile(File classesDir, String className) {
		return new File(classesDir, Strings.concat(className.replace('.', File.separatorChar), ".class"));
	}
}
