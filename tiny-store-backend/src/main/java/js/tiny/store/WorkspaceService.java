package js.tiny.store;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Remote;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import js.log.Log;
import js.log.LogFactory;
import js.tiny.store.dao.IDAO;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.Repository;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.tool.Project;
import js.tiny.store.tool.Workspace;

@ApplicationScoped
@Remote
@PermitAll
public class WorkspaceService {
	private static final Log log = LogFactory.getLog(WorkspaceService.class);

	@Inject
	private Workspace workspace;

	@Inject
	private IDAO dao;

	public List<Store> createStore(Store store) throws IOException {
		store.setOwner("irotaru");
		workspace.createStore(store);
		return dao.findStoresByOwner("irotaru");
	}

	public List<Store> deleteStore(Store store) throws IOException {
		dao.deleteStore(store.getPackageName());
		return dao.findStoresByOwner("irotaru");
	}

	public void updateStore(String projectName) throws IOException {
		Project project = workspace.getStore(projectName);

		project.clean();
		project.generateSources();

		project.compileSources();
		project.buildWar();
		project.deployWar();

		project.compileClientSources();
		project.buildClientJar();
		project.deployClientJar();
	}

	public List<Store> getStores() throws IOException {
		return dao.findStoresByOwner("irotaru");
	}

	public Store getStore(String packageName) {
		return dao.getStoreByPackage(packageName);
	}

	public List<Repository> getRepositories(String storePackage) {
		return dao.findRepositoriesByStore(storePackage);
	}

	public Repository getRepository(String name) {
		return dao.getRepository(name);
	}

	public List<DataService> getServices(String repositoryName) {
		return dao.findServicesByRepository(repositoryName);
	}

	public StoreEntity getEntity(String className) {
		return dao.getStoreEntity(className);
	}

	public DataService getService(String interfaceName) {
		return dao.getDataService(interfaceName);
	}

	public List<ServiceOperation> getOperations(String serviceInterface) {
		return dao.findServiceOperations(serviceInterface);
	}

	public ServiceOperation getOperation(String serviceInterface, String name) {
		return dao.getServiceOperation(serviceInterface, name);
	}

	public void saveOperation(ServiceOperation operation) {
		dao.saveServiceOperation(operation);
	}

	public boolean testDataSource(Repository meta) throws PropertyVetoException {
		if (meta.getConnectionString().startsWith("jdbc:")) {
			// jdbc data source
			ComboPooledDataSource datasource = new ComboPooledDataSource();
			datasource.setAcquireRetryAttempts(1);

			datasource.setJdbcUrl(meta.getConnectionString());
			datasource.setUser(meta.getUser());
			datasource.setPassword(meta.getPassword());

			try (Connection connection = datasource.getConnection()) {
				return true;
			} catch (SQLException e) {
				log.error(e);
			}
			return false;
		}

		// no-sql data source
		return false;
	}

	public List<StoreEntity> getStoreEntities(String storePackage) {
		return dao.findEntitiesByStore(storePackage);
	}
}
