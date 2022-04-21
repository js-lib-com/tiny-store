package js.tiny.store.tool.it;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import js.json.impl.JsonImpl;
import js.tiny.store.tool.Project;
import js.tiny.store.tool.Workspace;
import js.util.Classes;

public class WorkspaceTest {
	private Workspace workspace;

	@Before
	public void beforeTest() throws Exception {
		workspace = new Workspace();

		Classes.setFieldValue(workspace, "WORKSPACE_DIR", "D:\\runtime\\tiny-store\\workspace");
		Classes.setFieldValue(workspace, "META_DIR", ".meta");
		Classes.setFieldValue(workspace, "PROJECT_FILE", "project.json");
		Classes.setFieldValue(workspace, "RUNTIME_DIR", "D:\\runtime\\tiny-store\\");

		Classes.setFieldValue(workspace, "json", new JsonImpl());
	}
	
	@Test
	public void buildProject() throws IOException {
		Project project = workspace.getProject("omsx");
		project.clean();
		project.generateSources();
		
		project.compileSources();
		project.buildWar();
		project.deployWar();
		
		project.compileClientSources();
		project.buildClientJar();
		project.deployClientJar();
	}
}
