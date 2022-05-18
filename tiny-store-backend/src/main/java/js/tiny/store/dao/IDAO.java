package js.tiny.store.dao;

import java.util.List;

import js.tiny.store.meta.DataService;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;

public interface IDAO {

	Store getStore(String storeId);

	Store getStoreByName(String name);

	List<Store> findStoresByOwner(String ownerName);

	void createStore(Store store);

	void saveStore(Store store);

	void deleteStore(String storeId);

	StoreEntity getStoreEntity(String entityId);

	DataService getDataService(String serviceId);

	ServiceOperation createOperation(ServiceOperation operation);

	void saveServiceOperation(ServiceOperation operation);

	void deleteOperation(String operationId);

	List<ServiceOperation> findServiceOperations(String serviceId);

	ServiceOperation getServiceOperation(String operationId);

	StoreEntity createEntity(StoreEntity entity);

	void saveEntity(StoreEntity entity);

	void deleteEntity(String entityId);

	List<StoreEntity> findEntitiesByStore(String storeId);

	DataService createService(DataService service);

	void saveService(DataService service);

	void deleteService(String serviceId);

	List<DataService> findServicesByStore(String storeId);

}
