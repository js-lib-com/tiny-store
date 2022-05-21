package js.tiny.store.tool;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import js.log.Log;
import js.log.LogFactory;
import js.tiny.store.dao.IDAO;
import js.tiny.store.meta.Store;

@ApplicationScoped
public class Workspace {
	private static final Log log = LogFactory.getLog(Workspace.class);

	@Resource(name = "workspace.dir")
	private String WORKSPACE_DIR;

	@Resource(name = "runtime.dir")
	private String RUNTIME_DIR;

	private File workspaceDir;

	private final Map<String, Project> projects = new HashMap<>();
	private final IDAO dao;

	@Inject
	public Workspace(IDAO dao) {
		this.dao = dao;
	}

	@PostConstruct
	void postConstruct() {
		workspaceDir = new File(WORKSPACE_DIR);

		File[] projectDirs = workspaceDir.listFiles();
		if (projectDirs != null) {
			for (File projectDir : projectDirs) {
				Store store = dao.getStoreByName(projectDir.getName());
				try {
					Project project = new Project(projectDir, new File(RUNTIME_DIR), store, dao);
					projects.put(projectDir.getName(), project);
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}

	public void createProject(String projectName) throws IOException {
		File projectDir = new File(workspaceDir, projectName);
		if (!projectDir.exists() && !projectDir.mkdirs()) {
			throw new IOException("Fail to create project directory " + projectDir);
		}
		Store store = dao.getStoreByName(projectDir.getName());
		Project project = new Project(projectDir, new File(RUNTIME_DIR), store, dao);
		projects.put(projectName, project);
	}

	public void deleteProject(String projectName) throws IOException {
		Project project = projects.get(projectName);
		if (project != null) {
			project.undeployWar();
		}

		File projectDir = new File(workspaceDir, projectName);
		Files.removeFilesHierarchy(projectDir).delete();
	}

	public Project getProject(Store store) throws IOException {
		// by convention project name is the store name
		File projectDir = new File(workspaceDir, store.getName());
		return new Project(projectDir, new File(RUNTIME_DIR), store, dao);
	}

	public Project getProject(String projectName) throws IOException {
		return projects.get(projectName);
	}
}
