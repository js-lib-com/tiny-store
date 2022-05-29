package js.tiny.store.tool;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class JGitTest {
	private static final File REPO_DIR = new File("d:/tmp/repo");
	
	public void cloneRepository() throws InvalidRemoteException, TransportException, GitAPIException {
		CredentialsProvider credentials = new UsernamePasswordCredentialsProvider("irotaru", "Mami1964!@#$");
		Git git = Git.cloneRepository().setURI("https://git.gnotis.ro/xp/store.git").setDirectory(REPO_DIR).setCredentialsProvider(credentials).call();
		System.out.println(git);
	}

	public void listBranches() throws IllegalStateException, GitAPIException {
		Git git = Git.init().setDirectory(REPO_DIR).call();
		System.out.println(git);

		List<Ref> branches = git.branchList().call();
		System.out.println(branches);

		git.pull().call();
	}

	public void pull() throws IllegalStateException, GitAPIException {
		Git git = Git.init().setDirectory(REPO_DIR).call();
		System.out.println(git);

		git.pull().call();
	}

	@Test
	public void push() throws IllegalStateException, GitAPIException, IOException {
		Git git = Git.open(REPO_DIR);
		System.out.println(git);

		git.add().addFilepattern("store-store").call();
		git.commit().setAll(true).setMessage("Update project properties.").call();

		CredentialsProvider credentials = new UsernamePasswordCredentialsProvider("irotaru", "Mami1964!@#$");
		git.push().setCredentialsProvider(credentials).call().forEach(System.out::println);
	}
	
	public void repository() throws GitAPIException, URISyntaxException {
		Git git = Git.init().setDirectory(REPO_DIR).call();
		System.out.println(git);

		git.remoteSetUrl().setRemoteName("origin").setRemoteUri(new URIish("https://git-old.gnotis.ro/xp/test.git")).call();

		List<Ref> branches = git.branchList().call();
		System.out.println(branches);

		git.branchCreate().setName("master").call();

		// git.pull().call();

		git.add().addFilepattern("src").call();

		git.push().call();
	}
}
