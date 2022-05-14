package js.tiny.store.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		storeDAO.update("packageName", store.getPackageName(), store);
	}

	@Override
	public void deleteStore(String packageName) {
		storeDAO.delete("packageName", packageName);
	}

	@Override
	public List<DataService> findServicesByStore(String storePackage) {
		return serviceDAO.find("storePackage", storePackage);
	}

	@Override
	public List<DataService> findServicesByRepository(String repositoryName) {
		return serviceDAO.find("repositoryName", repositoryName);
	}

	@Override
	public void createRepository(Repository repository) {
		repositoryDAO.create(repository);
	}

	@Override
	public void saveRepository(Repository repository) {
		repositoryDAO.update("name", repository.getName(), repository);
	}

	@Override
	public void deleteRepository(Repository repository) {
		repositoryDAO.delete("name", repository.getName());
	}

	@Override
	public List<Repository> findRepositoriesByStore(String storePackage) {
		return repositoryDAO.find("storePackage", storePackage);
	}

	@Override
	public Repository getRepository(String name) {
		return repositoryDAO.get("name", name);
	}

	@Override
	public StoreEntity getStoreEntity(String className) {
		return entityDAO.get("className", className);
	}

	@Override
	public DataService getDataService(String interfaceName) {
		return serviceDAO.get("interfaceName", interfaceName);
	}

	@Override
	public List<ServiceOperation> findServiceOperations(String serviceInterface) {
		return operationDAO.find("serviceInterface", serviceInterface);
	}

	@Override
	public ServiceOperation getServiceOperation(String serviceInterface, String name) {
		Map<String, String> filters = new HashMap<>();
		filters.put("serviceInterface", serviceInterface);
		filters.put("name", name);
		return operationDAO.filterAnd(filters);
	}

	@Override
	public void saveServiceOperation(ServiceOperation operation) {
		Map<String, String> filters = new HashMap<>();
		filters.put("serviceInterface", operation.getServiceInterface());
		filters.put("name", operation.getName());
		operationDAO.update(filters, operation);
	}

	@Override
	public void createEntity(StoreEntity entity) {
		entityDAO.create(entity);
	}

	@Override
	public void saveEntity(StoreEntity entity) {
		entityDAO.update("className", entity.getClassName(), entity);
	}

	@Override
	public void deleteEntity(StoreEntity entity) {
		entityDAO.delete("className", entity.getClassName());
	}

	@Override
	public List<StoreEntity> findEntitiesByStore(String storePackage) {
		return entityDAO.find("storePackage", storePackage);
	}
}
