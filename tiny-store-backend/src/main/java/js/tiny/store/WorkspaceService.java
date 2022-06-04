package js.tiny.store;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Remote;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import js.json.Json;
import js.lang.GType;
import js.log.Log;
import js.log.LogFactory;
import js.tiny.container.interceptor.Intercepted;
import js.tiny.store.dao.Database;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.meta.Version;
import js.tiny.store.tool.Project;
import js.tiny.store.tool.Strings;
import js.tiny.store.tool.Workspace;
import js.util.Classes;

@ApplicationScoped
@Remote
@PermitAll
public class WorkspaceService {
	private static final Log log = LogFactory.getLog(WorkspaceService.class);

	@Inject
	private Workspace workspace;

	@Inject
	private Database db;

	public Store createStore(Store store) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		store.setOwner("irotaru");
		store.setVersion(new Version(1, 0));
		db.createStore(store);
		workspace.createProject(store);
		if (store.getGitURL() != null) {
			commitChanges(store.id(), "Initial import.");
			pushChanges(store.id());
		}
		return store;
	}

	@Intercepted(MetaChangeListener.class)
	public void updateStore(Store store) {
		store.setOwner("irotaru");
		db.updateStore(store);

		// update REST enabled state to all store services and their operations and parameters
		boolean restEnabled = store.getRestPath() != null;
		for (DataService service : db.getStoreServices(store.id())) {
			if (service.isRestEnabled() != restEnabled) {
				service.setRestEnabled(restEnabled);
				db.updateDataService(service);
				// if service REST enabled state was changed consider also service operations and related parameters
				for (ServiceOperation operation : db.getServiceOperations(service.id())) {
					operation.setRestEnabled(restEnabled);
					operation.getParameters().forEach(parameter -> parameter.setRestEnabled(restEnabled));
					db.updateServiceOperation(operation);
				}
			}
		}
	}

	public List<Store> deleteStore(String storeId) throws IOException {
		Store store = db.getStore(storeId);
		// by convention project name is the store name
		workspace.deleteProject(store.getName());

		db.deleteStore(storeId);
		return db.findStoresByOwner("irotaru");
	}

	public List<Store> getStores() throws IOException {
		return db.findStoresByOwner("irotaru");
	}

	@Intercepted(MetaChangeListener.class)
	public DataService createDaoService(String storeId, StoreEntity entity, DataService service) throws IOException {
		DataService createdService = db.createDataService(storeId, service);

		Map<String, String> variables = new HashMap<>();
		variables.put("entity-class", entity.getClassName());
		variables.put("entity-name", Strings.getSimpleName(entity.getClassName()));
		variables.put("entity-id-type", Strings.getParameterizedName(entity.getFields().get(0).getType()));
		String operationsJson = Strings.injectVariables(Strings.load(Classes.getResourceAsReader("/dao-operations.json")), variables);

		Json json = Classes.loadService(Json.class);
		List<ServiceOperation> operations = json.parse(operationsJson, new GType(List.class, ServiceOperation.class));
		operations.forEach(operation -> {
			operation.setServiceId(createdService.id());
			db.createOperation(operation);
		});

		return createdService;
	}

	public boolean testDataSource(Store store) throws PropertyVetoException {
		if (store.getConnectionString().startsWith("jdbc:")) {
			// jdbc data source
			ComboPooledDataSource datasource = new ComboPooledDataSource();
			datasource.setAcquireRetryAttempts(1);

			datasource.setJdbcUrl(store.getConnectionString());
			datasource.setUser(store.getUser());
			datasource.setPassword(store.getPassword());

			try {
				datasource.getConnection();
				return true;
			} catch (SQLException e) {
				log.error(e);
			} finally {
				try {
					DataSources.destroy(datasource);
				} catch (SQLException e) {
					log.error(e);
				}
			}
			return false;
		}

		// no-sql data source
		return false;
	}

	public boolean buildProject(String storeId) throws IOException {
		Store store = db.getStore(storeId);
		Project project = workspace.getProject(store.getName());
		project.clean();

		if (project.generateSources()) {
			if (!project.compileServerSources()) {
				// TODO: send compilation diagnostic to user interface
				return false;
			}
			if (!project.compileClientSources()) {
				return false;
			}
		}

		project.buildServerWar();
		project.deployServerWar();

		project.buildClientJar();
		project.deployClientJar();

		return true;
	}

	public boolean commitChanges(String storeId, String message) throws IOException, NoFilepatternException, GitAPIException {
		Store store = db.getStore(storeId);
		Project project = workspace.getProject(store.getName());
		project.clean();
		project.generateSources();

		try (Git git = Git.open(project.getProjectDir().getAbsoluteFile())) {
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
				return false;
			}
			git.add().addFilepattern(".").call();
			git.commit().setAll(true).setMessage(message).call();
		} catch (RepositoryNotFoundException e) {
			log.warn(e);
			return false;
		}

		db.deleteChangeLog(storeId);
		return true;
	}

	public boolean pushChanges(String storeId) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		Store store = db.getStore(storeId);
		// String gitURL = store.getGitURL();
		// TODO: extract server URL from git URL and retrieve credentials from servers configuration
		CredentialsProvider credentials = new UsernamePasswordCredentialsProvider("irotaru", "Mami1964!@#$");
		try (Git git = Git.open(workspace.getProjectDir(store.getName()).getAbsoluteFile())) {
			git.push().setCredentialsProvider(credentials).call();
		} catch (RepositoryNotFoundException e) {
			log.warn(e);
			return false;
		}
		return true;
	}
}
