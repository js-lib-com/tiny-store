package js.tiny.store.tool;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import js.json.Json;
import js.tiny.store.meta.ProjectMeta;
import js.util.Strings;

@ApplicationScoped
public class Workspace {
	@Resource(name = "workspace.dir")
	private String WORKSPACE_DIR;

	@Resource(name = "meta.dir")
	private String META_DIR;

	@Resource(name = "project.meta.file")
	private String PROJECT_FILE;

	@Resource(name = "runtime.dir")
	private String RUNTIME_DIR;

	@Inject
	private Json json;

	public Project getProject(String repositoryName) throws IOException {
		File workspaceDir = new File(WORKSPACE_DIR);
		File projectDir = new File(workspaceDir, repositoryName);
		if (!projectDir.exists() && !projectDir.mkdirs()) {
			throw new IOException("Fail to create project directory " + projectDir);
		}

		File metaDir = new File(projectDir, META_DIR);
		if (!metaDir.exists() && !metaDir.mkdirs()) {
			throw new IOException("Fail to create project meta directory " + metaDir);
		}

		File projectMetaFile = new File(metaDir, PROJECT_FILE);
		if (!projectMetaFile.exists()) {
			throw new IllegalStateException("Missing project file " + projectMetaFile);
		}

		try (Reader reader = new FileReader(projectMetaFile)) {
			ProjectMeta projectMeta = json.parse(reader, ProjectMeta.class);
			Project project = new Project(projectDir, new File(RUNTIME_DIR), projectMeta);
			return project;
		}
	}

	public File getRepositoryDirEOL(String repositoryName) {
		return new File(repositoryName);
	}

	public File sourceFileEOL(File repositoryDir, String className) {
		return new File(repositoryDir, Strings.concat("src", File.separatorChar, className.replace('.', File.separatorChar), ".java"));
	}

	public File classFileEOL(File repositoryDir, String className) {
		return new File(repositoryDir, Strings.concat("bin", File.separatorChar, className.replace('.', File.separatorChar), ".class"));
	}

}
