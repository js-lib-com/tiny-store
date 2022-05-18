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

	public Project getStore(String name) throws IOException {
		File projectDir = new File(workspaceDir, name);
		if (!projectDir.exists() && !projectDir.mkdirs()) {
			throw new IOException("Fail to create project directory " + projectDir);
		}

		Store store = dao.getStoreByName(name);
		return new Project(projectDir, new File(RUNTIME_DIR), store, dao);
	}

	public void createStore(Store store) throws IOException {
		File projectDir = new File(workspaceDir, store.getName());
		if (!projectDir.exists() && !projectDir.mkdirs()) {
			throw new IOException("Fail to create project directory " + projectDir);
		}
		dao.createStore(store);
	}

	public void deleteStore(String projectName) throws IOException {
		File projectDir = new File(workspaceDir, projectName);
		Files.removeFilesHierarchy(projectDir).delete();
	}
}
