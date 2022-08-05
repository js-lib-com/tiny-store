package com.jslib.tiny.store;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.jslib.tiny.store.dao.Database;
import com.jslib.tiny.store.dao.StoreUpdateListener;
import com.jslib.tiny.store.meta.DataService;
import com.jslib.tiny.store.meta.EntityField;
import com.jslib.tiny.store.meta.FieldFlag;
import com.jslib.tiny.store.meta.Server;
import com.jslib.tiny.store.meta.ServerType;
import com.jslib.tiny.store.meta.ServiceOperation;
import com.jslib.tiny.store.meta.Store;
import com.jslib.tiny.store.meta.StoreEntity;
import com.jslib.tiny.store.meta.TypeDef;
import com.jslib.tiny.store.meta.Version;
import com.jslib.tiny.store.tool.IGitClient;
import com.jslib.tiny.store.tool.Project;
import com.jslib.tiny.store.tool.StoreDB;
import com.jslib.tiny.store.util.Classes;
import com.jslib.tiny.store.util.Files;
import com.jslib.tiny.store.util.Strings;
import com.jslib.tiny.store.util.URLs;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Remote;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.jslib.api.json.Json;
import com.jslib.lang.GType;
import com.jslib.api.log.Log;
import com.jslib.api.log.LogFactory;
import com.jslib.container.interceptor.Intercepted;
import com.jslib.util.Params;

@ApplicationScoped
@Remote
@PermitAll
public class Workspace {
	private static final Log log = LogFactory.getLog(Workspace.class);

	private final Context context;
	private final Database db;
	private final IGitClient git;

	@Inject
	public Workspace(Context context, Database db, IGitClient git) {
		Params.notNull(context.getWorkspaceDir(), "Workspace directory");
		this.context = context;
		this.db = db;
		this.git = git;
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
			Server server = db.getServerByHostURL(URLs.hostURL(gitURL));
			assert server != null;
			CredentialsProvider credentials = new UsernamePasswordCredentialsProvider(server.getUsername(), server.getPassword());
			Git.cloneRepository().setURI(gitURL).setDirectory(projectDir).setCredentialsProvider(credentials).call();
		}

		if (store.getGitURL() != null) {
			commitChanges(store.id(), "Initial import.");
			pushChanges(store.id());
		}
		return store;
	}

	@Intercepted({ ChangeLogListener.class, StoreUpdateListener.class })
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

	@Intercepted({ ChangeLogListener.class, SourceCodeBuildListener.class })
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
						field.setName(Strings.databaseToMemberName(columnName));
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

	@Intercepted(ChangeLogListener.class)
	public DataService createDaoService(StoreEntity entity) throws IOException {
		Store store = db.getStore(entity.getStoreId());

		DataService service = new DataService();
		service.setClassName(entity.getClassName() + "DAO");
		service.setDescription(String.format("Data access for %s entity.", entity.getClassName()));
		service.setRestEnabled(store.getRestPath() != null);
		service.setRestPath(Strings.simpleName(entity.getClassName()).toLowerCase());
		DataService createdService = db.createDataService(entity.getStoreId(), service);

		Map<String, String> variables = new HashMap<>();
		variables.put("entity-class", entity.getClassName());
		variables.put("entity-name", Strings.simpleName(entity.getClassName()));
		variables.put("entity-parameter", Strings.simpleName(entity.getClassName()).toLowerCase());
		variables.put("entity-id-type", entity.getFields().get(0).getType().getName());
		variables.put("rest-enabled", Boolean.toString(store.getRestPath() != null));
		String operationsJson = Strings.injectVariables(Strings.load(Classes.getResourceAsReader("/dao-operations.json")), variables);

		Json json = Classes.loadService(Json.class);
		List<ServiceOperation> operations = json.parse(operationsJson, new GType(List.class, ServiceOperation.class));
		operations.forEach(operation -> {
			operation.setServiceId(createdService.id());
			db.createOperation(operation);
		});

		return createdService;
	}

	public String testDataSource(Store store) throws PropertyVetoException {
		if (store.getDatabaseURL().startsWith("jdbc:")) {
			// jdbc data source
			ComboPooledDataSource datasource = new ComboPooledDataSource();
			datasource.setAcquireRetryAttempts(1);

			datasource.setJdbcUrl(store.getDatabaseURL());
			datasource.setUser(store.getDatabaseUser());
			datasource.setPassword(store.getDatabasePassword());

			try {
				datasource.getConnection();
				return "Database connection is working properly.";
			} catch (SQLException e) {
				log.error(e);
				return e.getMessage();
			} finally {
				try {
					DataSources.destroy(datasource);
				} catch (SQLException e) {
					log.error(e);
					return e.getMessage();
				}
			}
		}

		// no-sql data source
		return "NoSQL database not implemented yet.";
	}

	public String buildProject(String storeId) throws IOException {
		Store store = db.getStore(storeId);
		Project project = new Project(context, store, db);
		project.clean();

		if (project.generateSources()) {
			String result = project.compileServerSources();
			if (result != null) {
				return result;
			}
			result = project.compileClientSources();
			if (result != null) {
				return result;
			}
		}

		project.buildServerWar();
		project.deployServerWar();

		project.buildClientJar();
		project.deployClientJar();

		return "Success";
	}

	public boolean commitChanges(String storeId, String message) throws IOException {
		Store store = db.getStore(storeId);
		Project project = new Project(context, store, db);
		project.clean();
		project.generateSources();

		git.commit(project.getProjectDir(), message);
		db.deleteChangeLog(storeId);
		return true;
	}

	public boolean pushChanges(String storeId) throws IOException {
		Store store = db.getStore(storeId);
		File projectDir = new File(context.getWorkspaceDir(), store.getName());

		Server server = db.getServerByHostURL(URLs.hostURL(store.getGitURL()));
		git.push(projectDir, server.getUsername(), server.getPassword());
		return true;
	}

	public Project getProject(String projectName) throws IOException {
		Store store = db.getStoreByName(projectName);
		return new Project(context, store, db);
	}

	public List<String> getTypeOptionsByService(String serviceId) {
		DataService service = db.getDataService(serviceId);
		return getTypeOptionsByStore(service.getStoreId());
	}

	public List<String> getTypeOptionsByStore(String storeId) {
		List<StoreEntity> entities = db.getStoreEntities(storeId);

		List<String> options = new ArrayList<>();
		entities.forEach(entity -> options.add(entity.getClassName()));

		options.add(BigDecimal.class.getCanonicalName());
		options.add(Boolean.class.getCanonicalName());
		options.add(Byte.class.getCanonicalName());
		options.add(Date.class.getCanonicalName());
		options.add(Double.class.getCanonicalName());
		options.add(Float.class.getCanonicalName());
		options.add(Integer.class.getCanonicalName());
		options.add(Long.class.getCanonicalName());
		options.add(Object.class.getCanonicalName());
		options.add(Short.class.getCanonicalName());
		options.add(String.class.getCanonicalName());
		options.add(Time.class.getCanonicalName());
		options.add(Timestamp.class.getCanonicalName());

		return options;
	}

	public List<String> getMavenOptions() {
		return db.findServersByType(ServerType.MAVEN).stream().map(server -> server.getHostURL()).collect(Collectors.toList());
	}
}
