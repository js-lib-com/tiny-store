package js.tiny.store;

import java.io.IOException;
import java.util.Set;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Remote;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import js.tiny.store.tool.Project;
import js.tiny.store.tool.Workspace;

@ApplicationScoped
@Remote
@PermitAll
public class WorkspaceService {
	@Inject
	private Workspace workspace;

	public Set<ProjectItem> getProjects() throws IOException {
		return workspace.getProjects();
	}

	public void buildClientJar(String repositoryName) throws IOException {
		Project project = workspace.getProject(repositoryName);
		project.generateSources();
		project.compileSources();
	}
}
