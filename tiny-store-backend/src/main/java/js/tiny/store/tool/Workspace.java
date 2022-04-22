package js.tiny.store.tool;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import js.json.Json;
import js.tiny.store.ProjectItem;
import js.tiny.store.meta.ProjectMeta;

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

	private File workspaceDir;

	@PostConstruct
	void postConstruct() {
		workspaceDir = new File(WORKSPACE_DIR);
	}

	public Set<ProjectItem> getProjects() throws IOException {
		File[] projectDirs = workspaceDir.listFiles();
		if (projectDirs == null) {
			return Collections.emptySet();
		}
		Set<ProjectItem> projects = new HashSet<ProjectItem>();
		for (File projectDir : projectDirs) {
			File metaDir = new File(projectDir, META_DIR);
			try(Reader reader = new FileReader(new File(metaDir, PROJECT_FILE))) {
				projects.add(json.parse(reader, ProjectItem.class));
			}
		}
		return projects;
	}

	public Project getProject(String repositoryName) throws IOException {
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
}
