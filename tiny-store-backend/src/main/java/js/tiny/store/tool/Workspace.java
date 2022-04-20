package js.tiny.store.tool;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import js.json.Json;
import js.util.Strings;

@ApplicationScoped
public class Workspace {
	@Resource(name = "workspace.dir")
	private String WORKSPACE_DIR;

	@Resource(name = "meta.dir")
	private String META_DIR;

	@Resource(name = "project.file")
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
			Project project = json.parse(reader, Project.class);
			project.init(projectDir, new File(RUNTIME_DIR));
			return project;
		}
	}

	public File getRepositoryDir(String repositoryName) {
		return new File(repositoryName);
	}

	public File sourceFile(File repositoryDir, String className) {
		return new File(repositoryDir, Strings.concat("src", File.separatorChar, className.replace('.', File.separatorChar), ".java"));
	}

	public File classFile(File repositoryDir, String className) {
		return new File(repositoryDir, Strings.concat("bin", File.separatorChar, className.replace('.', File.separatorChar), ".class"));
	}

}
