package js.tiny.store.tool;

import java.io.File;
import java.io.IOException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import js.log.Log;
import js.log.LogFactory;
import js.tiny.store.Context;
import js.tiny.store.dao.IDAO;
import js.tiny.store.meta.Store;

@ApplicationScoped
public class Workspace {
	private static final Log log = LogFactory.getLog(Workspace.class);

	private final Context context;
	private final IDAO dao;

	@Inject
	public Workspace(Context context, IDAO dao) {
		this.context = context;
		this.dao = dao;
	}

	public void createProject(String projectName) throws IOException {
		File projectDir = new File(context.getWorkspaceDir(), projectName);
		if (!projectDir.exists() && !projectDir.mkdirs()) {
			throw new IOException("Fail to create project directory " + projectDir);
		}
		log.debug("Create project directory |%s|.", projectDir);
	}

	public void deleteProject(String projectName) throws IOException {
		Project project = getProject(projectName);
		project.undeployWar();

		File projectDir = new File(context.getWorkspaceDir(), projectName);
		Files.removeFilesHierarchy(projectDir).delete();
	}

	public Project getProject(String projectName) throws IOException {
		// by convention project name is the store name
		Store store = dao.getStoreByName(projectName);
		return new Project(context, store, dao);
	}
}
