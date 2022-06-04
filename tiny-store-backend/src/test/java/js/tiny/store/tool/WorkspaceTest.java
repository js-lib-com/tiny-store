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
import js.tiny.store.Workspace;
import js.tiny.store.dao.Database;
import js.tiny.store.dao.DatabaseImpl;
import js.tiny.store.dao.MongoDB;

@RunWith(MockitoJUnitRunner.class)
public class WorkspaceTest {
	@Mock
	private Context context;
	
	private Database database;
	private Workspace workspace;

	@Before
	public void beforeTest() throws Exception {
		when(context.getMongoURL()).thenReturn("mongodb://10.138.44.35:27017");
		when(context.getMongoDatabase()).thenReturn("tiny-store");
		when(context.getWorkspaceDir()).thenReturn(new File("D:/runtime/tiny-store/workspace/"));
		when(context.getRuntimeDir()).thenReturn(new File("D:/runtime/tiny-store/"));
		
		MongoDB mongo = new MongoDB(context);
		database = new DatabaseImpl(mongo);
		workspace = new Workspace(context, database);
	}

	@Test
	@Ignore
	public void buildProject() throws IOException, InterruptedException {
		Project project = workspace.getProject("test");
		project.clean();
		project.generateSources();

		project.compileServerSources();
		project.buildServerWar();
		project.deployServerWar();

		project.compileClientSources();
		project.buildClientJar();
		project.deployClientJar();
		
		Thread.sleep(10000);
	}
}
