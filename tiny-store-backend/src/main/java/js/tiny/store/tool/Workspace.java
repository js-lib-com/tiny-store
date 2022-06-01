package js.tiny.store.tool;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

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

	public void createProject(Store store) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		// by convention project name is the store name
		String projectName = store.getName();
		File projectDir = new File(context.getWorkspaceDir(), projectName);
		if (!projectDir.exists() && !projectDir.mkdirs()) {
			throw new IOException("Fail to create project directory " + projectDir);
		}
		log.debug("Create project directory |%s|.", projectDir);

		String gitURL = store.getGitURL();
		// TODO: extract server URL from git URL and retrieve credentials
		CredentialsProvider credentials = new UsernamePasswordCredentialsProvider("irotaru", "Mami1964!@#$");
		Git git = Git.cloneRepository().setURI(gitURL).setDirectory(projectDir).setCredentialsProvider(credentials).call();

		// create project and its initial files and push to git repository
		getProject(projectName);
		git.add().addFilepattern(".").call();
		git.commit().setAll(true).setMessage("Initial import.").call();
		git.push().setCredentialsProvider(credentials).call();
	}

	public void deleteProject(String projectName) throws IOException {
		Project project = getProject(projectName);
		project.undeployServerWar();

		File projectDir = new File(context.getWorkspaceDir(), projectName);
		Files.removeFilesHierarchy(projectDir).delete();
	}

	public Project getProject(String projectName) throws IOException {
		// by convention project name is the store name
		Store store = dao.getStoreByName(projectName);
		return new Project(context, store, dao);
	}

	public File getProjectDir(String projectName) {
		return new File(context.getWorkspaceDir(), projectName);
	}
}
