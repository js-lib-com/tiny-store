package js.tiny.store.tool;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import js.tiny.store.dao.DaoFacade;
import js.tiny.store.dao.IDAO;
import js.tiny.store.dao.MongoDB;
import js.tiny.store.meta.Store;
import js.util.Classes;

@Ignore
public class WorkspaceTest {
	private IDAO dao;
	private Workspace workspace;

	@Before
	public void beforeTest() throws Exception {
		MongoDB mongo = new MongoDB();
		dao = new DaoFacade(mongo);
		workspace = new Workspace(dao);

		Classes.setFieldValue(workspace, "WORKSPACE_DIR", "D:\\runtime\\tiny-store\\workspace");
		Classes.setFieldValue(workspace, "RUNTIME_DIR", "D:\\runtime\\tiny-store\\");

		workspace.postConstruct();
	}

	@Test
	public void buildProject() throws IOException {
		Store store = dao.getStoreByName("call");
		Project project = workspace.getProject(store);
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
