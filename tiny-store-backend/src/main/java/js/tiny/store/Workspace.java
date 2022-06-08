package js.tiny.store;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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
import js.tiny.store.meta.EntityField;
import js.tiny.store.meta.FieldFlag;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.meta.TypeDef;
import js.tiny.store.meta.Version;
import js.tiny.store.tool.Classes;
import js.tiny.store.tool.Files;
import js.tiny.store.tool.Project;
import js.tiny.store.tool.StoreDB;
import js.tiny.store.tool.Strings;

@ApplicationScoped
@Remote
@PermitAll
public class Workspace {
	private static final Log log = LogFactory.getLog(Workspace.class);

	private final Context context;
	private final Database db;

	@Inject
	public Workspace(Context context, Database db) {
		this.context = context;
		this.db = db;
	}

	public Store createStore(Store store) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		store.setOwner("irotaru");
		store.setVersion(new Version(1, 0));
		db.createStore(store);

		// by convention project name is the store name
		String projectName = store.getName();
		File projectDir = new File(context.getWorkspaceDir(), projectName);
		if (!projectDir.exists() && !projectDir.mkdirs()) {
			throw new IOException("Fail to create project directory " + projectDir);
		}
		log.debug("Create project directory |%s|.", projectDir);

		String gitURL = store.getGitURL();
		if (gitURL != null) {
			log.info("Clone store %s from Git repository %s.", store.getName(), gitURL);
			// TODO: extract server URL from git URL and retrieve credentials
			CredentialsProvider credentials = new UsernamePasswordCredentialsProvider("irotaru", "Mami1964!@#$");
			Git.cloneRepository().setURI(gitURL).setDirectory(projectDir).setCredentialsProvider(credentials).call();
		}

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
		String projectName = store.getName();

		Project project = new Project(context, store, db);
		project.undeployServerWar();

		File projectDir = new File(context.getWorkspaceDir(), projectName);
		Files.removeFilesHierarchy(projectDir).delete();

		db.deleteStore(storeId);
		return db.findStoresByOwner("irotaru");
	}

	public List<Store> getStores() throws IOException {
		return db.findStoresByOwner("irotaru");
	}

	public StoreEntity importStoreEntity(String storeId, StoreEntity entity) throws Exception {
		if (entity.id() == null) {
			db.createStoreEntity(storeId, entity);
			assert entity.id() != null;
			assert entity.getFields() != null;
		}

		List<String> existingColumnsName = entity.getFields().stream().map(field -> Strings.columnName(field)).collect(Collectors.toList());
		Store store = db.getStore(storeId);
		String tableName = Strings.tableName(entity);
		AtomicBoolean dirtyEntity = new AtomicBoolean(false);

		try (StoreDB storeDB = new StoreDB(store)) {
			storeDB.sql(connection -> {
				DatabaseMetaData dbmeta = connection.getMetaData();

				String pkColumnName = null;
				try (ResultSet rs = dbmeta.getPrimaryKeys(null, null, tableName)) {
					while (rs.next()) {
						if (pkColumnName != null) {
							throw new IllegalStateException("Multiple primary keys on table " + tableName);
						}
						pkColumnName = rs.getString("COLUMN_NAME");
					}
				}

				try (ResultSet rs = dbmeta.getColumns(null, null, tableName, null)) {
					while (rs.next()) {
						String columnName = rs.getString("COLUMN_NAME");
						if (existingColumnsName.contains(columnName)) {
							continue;
						}

						Class<?> columnType = Classes.sqlType(rs.getInt("DATA_TYPE"));
						if (columnType == null) {
							throw new IllegalStateException("Not mapped SQL type " + rs.getString("TYPE_NAME"));
						}

						EntityField field = new EntityField();
						field.setName(Strings.columnToMemberName(columnName));
						if (!field.getName().equals(columnName)) {
							field.setAlias(columnName);
						}
						field.setType(new TypeDef(columnType));
						if (columnName.equals(pkColumnName)) {
							field.setFlag(FieldFlag.IDENTITY_KEY);
						}

						dirtyEntity.set(true);
						entity.getFields().add(field);
					}
				}
			});
		}

		if (dirtyEntity.get()) {
			db.updateStoreEntity(entity);
		}
		return entity;
	}

	@Intercepted(MetaChangeListener.class)
	public DataService createDaoService(StoreEntity entity) throws IOException {
		Store store = db.getStore(entity.getStoreId());

		DataService service = new DataService();
		service.setClassName(entity.getClassName() + "DAO");
		service.setDescription(String.format("Data access for %s entity.", entity.getClassName()));
		service.setRestEnabled(store.getRestPath() != null);
		service.setRestPath(entity.getClassName().toLowerCase());
		DataService createdService = db.createDataService(entity.getStoreId(), service);

		Map<String, String> variables = new HashMap<>();
		variables.put("entity-class", Strings.concat(store.getPackageName(), '.', entity.getClassName()));
		variables.put("entity-name", entity.getClassName());
		variables.put("entity-parameter", entity.getClassName().toLowerCase());
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
		Project project = new Project(context, store, db);
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
		Project project = new Project(context, store, db);
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

		File projectDir = new File(context.getWorkspaceDir(), store.getName());
		try (Git git = Git.open(projectDir.getAbsoluteFile())) {
			git.push().setCredentialsProvider(credentials).call();
		} catch (RepositoryNotFoundException e) {
			log.warn(e);
			return false;
		}
		return true;
	}

	public Project getProject(String projectName) throws IOException {
		Store store = db.getStoreByName(projectName);
		return new Project(context, store, db);
	}
}
