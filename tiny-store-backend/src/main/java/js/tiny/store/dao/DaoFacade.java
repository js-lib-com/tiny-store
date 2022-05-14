package js.tiny.store.dao;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.Repository;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;

@ApplicationScoped
public class DaoFacade implements IDAO {
	private DAO<Store> storeDAO;
	private DAO<Repository> repositoryDAO;
	private DAO<StoreEntity> entityDAO;
	private DAO<DataService> serviceDAO;
	private DAO<ServiceOperation> operationDAO;

	@Inject
	public DaoFacade(MongoDB mongo) {
		this.storeDAO = new DAO<>(mongo, Store.class);
		this.repositoryDAO = new DAO<>(mongo, Repository.class);
		this.entityDAO = new DAO<>(mongo, StoreEntity.class);
		this.serviceDAO = new DAO<>(mongo, DataService.class);
		this.operationDAO = new DAO<>(mongo, ServiceOperation.class);
	}

	@Override
	public Store getStore(String id) {
		return storeDAO.get(id);
	}

	@Override
	public Store getStoreByPackage(String packageName) {
		return storeDAO.get("packageName", packageName);
	}

	@Override
	public List<Store> findStoresByOwner(String ownerName) {
		return storeDAO.find("owner", ownerName);
	}

	@Override
	public void createStore(Store store) {
		storeDAO.create(store);
	}

	@Override
	public void saveStore(Store store) {
		storeDAO.update(store);
	}

	@Override
	public void deleteStore(String id) {
		storeDAO.delete(id);
	}

	@Override
	public List<DataService> findServicesByStore(String storePackage) {
		return serviceDAO.find("storePackage", storePackage);
	}

	@Override
	public List<DataService> findServicesByRepository(String repositoryId) {
		return serviceDAO.find("repositoryId", repositoryId);
	}

	@Override
	public void createRepository(Repository repository) {
		repositoryDAO.create(repository);
	}

	@Override
	public void saveRepository(Repository repository) {
		repositoryDAO.update(repository);
	}

	@Override
	public void deleteRepository(String id) {
		repositoryDAO.delete(id);
	}

	@Override
	public List<Repository> findRepositoriesByStore(String storeId) {
		return repositoryDAO.find("storeId", storeId);
	}

	@Override
	public Repository getRepository(String repositoryId) {
		return repositoryDAO.get(repositoryId);
	}

	@Override
	public StoreEntity getStoreEntity(String entityId) {
		return entityDAO.get(entityId);
	}

	@Override
	public DataService getDataService(String serviceId) {
		return serviceDAO.get(serviceId);
	}

	@Override
	public List<ServiceOperation> findServiceOperations(String serviceId) {
		return operationDAO.find("serviceId", serviceId);
	}

	@Override
	public ServiceOperation getServiceOperation(String operationId) {
		return operationDAO.get(operationId);
	}

	@Override
	public void saveServiceOperation(ServiceOperation operation) {
		operationDAO.update(operation);
	}

	@Override
	public void createEntity(StoreEntity entity) {
		entityDAO.create(entity);
	}

	@Override
	public void saveEntity(StoreEntity entity) {
		entityDAO.update(entity);
	}

	@Override
	public void deleteEntity(String id) {
		entityDAO.delete(id);
	}

	@Override
	public List<StoreEntity> findEntitiesByStore(String storeId) {
		return entityDAO.find("storeId", storeId);
	}
}
