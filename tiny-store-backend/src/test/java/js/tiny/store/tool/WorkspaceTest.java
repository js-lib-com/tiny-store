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

import com.jslib.tiny.store.Context;
import com.jslib.tiny.store.Workspace;
import com.jslib.tiny.store.dao.Database;
import com.jslib.tiny.store.dao.DatabaseImpl;
import com.jslib.tiny.store.dao.MongoDB;
import com.jslib.tiny.store.tool.IGitClient;
import com.jslib.tiny.store.tool.Project;

@RunWith(MockitoJUnitRunner.class)
public class WorkspaceTest {
	@Mock
	private Context context;
	@Mock
	private IGitClient git;
	
	private Database database;
	private Workspace workspace;

	@Before
	public void beforeTest() throws Exception {
		when(context.getMongoURL()).thenReturn("mongodb://10.138.44.35:27017");
		when(context.getMongoDatabaseName()).thenReturn("tiny-store");
		when(context.getWorkspaceDir()).thenReturn(new File("D:/runtime/tiny-store/workspace/"));
		when(context.getRuntimeDir()).thenReturn(new File("D:/runtime/tiny-store/"));
		
		MongoDB mongo = new MongoDB(context);
		database = new DatabaseImpl(mongo);
		workspace = new Workspace(context, database, git);
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
