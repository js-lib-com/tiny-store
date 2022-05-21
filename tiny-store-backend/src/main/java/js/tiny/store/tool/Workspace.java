package js.tiny.store.tool;

import java.io.File;
import java.io.IOException;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import js.tiny.store.dao.IDAO;
import js.tiny.store.meta.Store;

@ApplicationScoped
public class Workspace {
	@Resource(name = "workspace.dir")
	private String WORKSPACE_DIR;

	@Resource(name = "runtime.dir")
	private String RUNTIME_DIR;

	private File workspaceDir;

	private final IDAO dao;

	@Inject
	public Workspace(IDAO dao) {
		this.dao = dao;
	}

	@PostConstruct
	void postConstruct() {
		workspaceDir = new File(WORKSPACE_DIR);
	}

	public void createProject(String projectName) throws IOException {
		File projectDir = new File(workspaceDir, projectName);
		if (!projectDir.exists() && !projectDir.mkdirs()) {
			throw new IOException("Fail to create project directory " + projectDir);
		}
	}

	public void deleteProject(String projectName) throws IOException {
		File projectDir = new File(workspaceDir, projectName);
		Files.removeFilesHierarchy(projectDir).delete();
	}

	public Project getProject(Store store) throws IOException {
		// by convention project name is the store name
		File projectDir = new File(workspaceDir, store.getName());
		return new Project(projectDir, new File(RUNTIME_DIR), store, dao);
	}
}
