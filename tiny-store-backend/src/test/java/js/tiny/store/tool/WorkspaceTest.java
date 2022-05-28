package js.tiny.store.tool;

import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import js.tiny.store.Context;
import js.tiny.store.dao.DaoFacade;
import js.tiny.store.dao.IDAO;
import js.tiny.store.dao.MongoDB;

@RunWith(MockitoJUnitRunner.class)
public class WorkspaceTest {
	@Mock
	private Context context;
	
	private IDAO dao;
	private Workspace workspace;

	@Before
	public void beforeTest() throws Exception {
		when(context.getMongoURL()).thenReturn("mongodb://10.138.44.35:27017");
		when(context.getMongoDatabase()).thenReturn("tiny-store");
		when(context.getWorkspaceDir()).thenReturn(new File("D:/runtime/tiny-store/workspace/"));
		when(context.getRuntimeDir()).thenReturn(new File("D:/runtime/tiny-store/"));
		
		MongoDB mongo = new MongoDB(context);
		dao = new DaoFacade(mongo);
		workspace = new Workspace(context, dao);
	}

	@Test
	@Ignore
	public void buildProject() throws IOException, InterruptedException {
		Project project = workspace.getProject("test");
		project.clean();
		project.generateSources();

		project.compileSources();
		project.buildWar();
		project.deployWar();

		project.compileClientSources();
		project.buildClientJar();
		project.deployClientJar();
		
		Thread.sleep(10000);
	}
}
