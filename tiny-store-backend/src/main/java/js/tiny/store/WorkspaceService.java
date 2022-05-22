package js.tiny.store;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Remote;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import js.log.Log;
import js.log.LogFactory;
import js.tiny.store.dao.IDAO;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.meta.Version;
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
		store.setVersion(new Version(1, 0));
		dao.createStore(store);

		// by convention project name is the store name
		workspace.createProject(store.getName());

		return dao.findStoresByOwner("irotaru");
	}

	public void saveStore(Store store) {
		store.setOwner("irotaru");
		dao.saveStore(store);
	}

	public List<Store> deleteStore(String storeId) throws IOException {
		Store store = dao.getStore(storeId);
		// by convention project name is the store name
		workspace.deleteProject(store.getName());

		dao.deleteStore(storeId);
		return dao.findStoresByOwner("irotaru");
	}

	public StoreEntity createEntity(String storeId, StoreEntity entity) {
		entity.setStoreId(storeId);
		entity.setFields(new ArrayList<>(0));
		return dao.createEntity(entity);
	}

	public void saveEntity(StoreEntity entity) {
		dao.saveEntity(entity);
	}

	public void deleteEntity(String entityId) {
		dao.deleteEntity(entityId);
	}

	public DataService createService(String storeId, DataService service) {
		service.setStoreId(storeId);
		return dao.createService(service);
	}

	public void saveService(DataService service) {
		dao.saveService(service);
	}

	public void deleteService(String serviceId) {
		dao.deleteService(serviceId);
	}

	public List<Store> getStores() throws IOException {
		return dao.findStoresByOwner("irotaru");
	}

	public Store getStore(String id) {
		return dao.getStore(id);
	}

	public List<DataService> getStoreServices(String storeId) {
		return dao.findServicesByStore(storeId);
	}

	public StoreEntity getEntity(String className) {
		return dao.getStoreEntity(className);
	}

	public List<StoreEntity> getStoreEntities(String storeId) {
		return dao.findEntitiesByStore(storeId);
	}

	public DataService getService(String serviceId) {
		return dao.getDataService(serviceId);
	}

	public ServiceOperation createOperation(String serviceId, ServiceOperation operation) {
		operation.setServiceId(serviceId);
		operation.setParameters(new ArrayList<>());
		operation.setExceptions(new ArrayList<>());
		return dao.createOperation(operation);
	}

	public void saveOperation(ServiceOperation operation) {
		dao.saveServiceOperation(operation);
	}

	public void deleteOperation(String operationId) {
		dao.deleteOperation(operationId);
	}

	public List<ServiceOperation> getServiceOperations(String serviceId) {
		return dao.findServiceOperations(serviceId);
	}

	public ServiceOperation getOperation(String operationId) {
		return dao.getServiceOperation(operationId);
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
		Store store = dao.getStore(storeId);
		Project project = workspace.getProject(store.getName());
		project.clean();

		if (project.generateSources()) {
			project.compileSources();
			project.compileClientSources();
		}

		project.buildWar();
		project.deployWar();

		project.buildClientJar();
		project.deployClientJar();

		return true;
	}
}
