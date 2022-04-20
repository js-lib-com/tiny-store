package js.tiny.store;

import java.io.IOException;

import jakarta.inject.Inject;
import js.tiny.store.tool.Project;
import js.tiny.store.tool.Workspace;

public class Service {
	@Inject
	private Workspace workspace;

	public void buildClientJar(String repositoryName) throws IOException {
		Project project = workspace.getProject(repositoryName);
		project.generateSources();
		project.compileSources();
	}
}
