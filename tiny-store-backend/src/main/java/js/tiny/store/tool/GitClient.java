package js.tiny.store.tool;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import js.log.Log;
import js.log.LogFactory;

class GitClient implements IGitClient {
	private static final Log log = LogFactory.getLog(GitClient.class);

	@Override
	public void commit(File projectDir, String message) throws IOException {
		try (Git git = Git.open(projectDir.getAbsoluteFile())) {
			Status status = git.status().call();
			boolean changed = false;

			// missing: files in index, but not file system (e.g. what you get if you call 'rm ...' on a existing file)
			for (String file : status.getMissing()) {
				changed = true;
				log.info("Missing file: %s.", file);
			}

			// modified: files modified on disk relative to the index (e.g. what you get if you modify an existing file without
			// adding it to the index)
			for (String file : status.getModified()) {
				changed = true;
				log.info("Modified file: %s.", file);
			}

			// untracked: files that are not ignored, and not in the index. (e.g. what you get if you create a new file without
			// adding it to the index)
			for (String file : status.getUntracked()) {
				changed = true;
				log.info("Untracked file: %s.", file);
			}

			if (!changed) {
				log.warn("Attempt to commit no changes.");
				return;
			}
			git.add().addFilepattern(".").call();
			git.commit().setAll(true).setMessage(message).call();
		} catch (RepositoryNotFoundException e) {
			log.warn(e);
			throw new IOException(e);
		} catch (NoWorkTreeException e) {
			log.error(e);
			throw new IOException(e);
		} catch (GitAPIException e) {
			log.error(e);
			throw new IOException(e);
		}
	}

	@Override
	public void push(File projectDir, String username, String password) throws IOException {
		CredentialsProvider credentials = new UsernamePasswordCredentialsProvider(username, password);
		try (Git git = Git.open(projectDir.getAbsoluteFile())) {
			git.push().setCredentialsProvider(credentials).call();
		} catch (RepositoryNotFoundException e) {
			log.warn(e);
			throw new IOException(e);
		} catch (InvalidRemoteException e) {
			log.error(e);
			throw new IOException(e);
		} catch (TransportException e) {
			log.error(e);
			throw new IOException(e);
		} catch (GitAPIException e) {
			log.error(e);
			throw new IOException(e);
		}
	}
}
