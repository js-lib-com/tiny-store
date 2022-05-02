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

	@Resource(name = "meta.dir")
	private String META_DIR;

	@Resource(name = "project.meta.file")
	private String PROJECT_FILE;

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

	public Project getStore(String packageName) throws IOException {
		String projectName = Strings.getSimpleName(packageName);
		File projectDir = new File(workspaceDir, projectName);
		if (!projectDir.exists() && !projectDir.mkdirs()) {
			throw new IOException("Fail to create project directory " + projectDir);
		}

		File metaDir = new File(projectDir, META_DIR);
		if (!metaDir.exists() && !metaDir.mkdirs()) {
			throw new IOException("Fail to create project meta directory " + metaDir);
		}

		Store store = dao.getStoreByPackage(packageName);
		return new Project(projectDir, new File(RUNTIME_DIR), store, dao);
	}

	public void createStore(Store store) throws IOException {
		File projectDir = new File(workspaceDir, Strings.getSimpleName(store.getPackageName()));
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
