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
	public List<StoreEntity> findEntitiesByStore(String storePackage) {
		return entityDAO.find("storePackage", storePackage);
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
		Map<String, String> parameters = new HashMap<>();
		parameters.put("serviceInterface", serviceInterface);
		parameters.put("name", name);
		return operationDAO.filterAnd(parameters);
	}
}
