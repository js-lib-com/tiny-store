package js.tiny.store.dao;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;

@ApplicationScoped
public class DaoFacade implements IDAO {
	private DAO<Store> storeDAO;
	private DAO<StoreEntity> entityDAO;
	private DAO<DataService> serviceDAO;
	private DAO<ServiceOperation> operationDAO;

	@Inject
	public DaoFacade(MongoDB mongo) {
		this.storeDAO = new DAO<>(mongo, Store.class);
		this.entityDAO = new DAO<>(mongo, StoreEntity.class);
		this.serviceDAO = new DAO<>(mongo, DataService.class);
		this.operationDAO = new DAO<>(mongo, ServiceOperation.class);
	}

	@Override
	public Store getStore(String id) {
		return storeDAO.get(id);
	}

	@Override
	public Store getStoreByName(String name) {
		return storeDAO.get("name", name);
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
	public void deleteStore(String storeId) {
		storeDAO.delete(storeId);
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
	public ServiceOperation createOperation(ServiceOperation operation) {
		return operationDAO.create(operation);
	}

	@Override
	public void saveServiceOperation(ServiceOperation operation) {
		operationDAO.update(operation);
	}

	@Override
	public void deleteOperation(String operationId) {
		operationDAO.delete(operationId);
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
	public StoreEntity createEntity(StoreEntity entity) {
		return entityDAO.create(entity);
	}

	@Override
	public void saveEntity(StoreEntity entity) {
		entityDAO.update(entity);
	}

	@Override
	public void deleteEntity(String entityId) {
		entityDAO.delete(entityId);
	}

	@Override
	public List<StoreEntity> findEntitiesByStore(String storeId) {
		return entityDAO.find("storeId", storeId);
	}

	@Override
	public DataService createService(DataService service) {
		return serviceDAO.create(service);
	}

	@Override
	public void saveService(DataService service) {
		serviceDAO.update(service);
	}

	@Override
	public void deleteService(String serviceId) {
		serviceDAO.delete(serviceId);
	}

	@Override
	public List<DataService> findServicesByStore(String storeId) {
		return serviceDAO.find("storeId", storeId);
	}
}
