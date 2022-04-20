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

		Classes.setFieldValue(workspace, "BASE_DIR", "D:\\\\runtime\\\\tiny-store\\workspace");
		Classes.setFieldValue(workspace, "META_DIR", ".meta");
		Classes.setFieldValue(workspace, "PROJECT_FILE", "project.json");

		Classes.setFieldValue(workspace, "json", new JsonImpl());
		
		Classes.invoke(workspace, "postConstruct");
	}
	
	@Test
	public void buildProject() throws IOException {
		Project project = workspace.getProject("omsx");
		project.generateSources();
		project.compileSources();
		project.buildWar();
		project.deployWar();
	}
}
