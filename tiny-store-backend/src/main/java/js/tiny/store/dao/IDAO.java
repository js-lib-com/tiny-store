package js.tiny.store.dao;

import java.util.List;

import js.tiny.store.meta.DataService;
import js.tiny.store.meta.Repository;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;

public interface IDAO {

	Store getStore(String storeId);

	Store getStoreByPackage(String packageName);

	List<Store> findStoresByOwner(String ownerName);

	void createStore(Store store);

	void saveStore(Store store);

	void deleteStore(String id);

	void createRepository(Repository repository);

	void saveRepository(Repository repository);

	void deleteRepository(String id);

	List<Repository> findRepositoriesByStore(String storeId);

	Repository getRepository(String repositoryId);

	StoreEntity getStoreEntity(String entityId);

	DataService getDataService(String serviceId);

	List<ServiceOperation> findServiceOperations(String serviceId);

	ServiceOperation getServiceOperation(String operationId);

	void saveServiceOperation(ServiceOperation operation);

	void createEntity(StoreEntity entity);

	void saveEntity(StoreEntity entity);

	void deleteEntity(String entityId);

	List<StoreEntity> findEntitiesByStore(String storeId);

	void createService(DataService service);

	void saveService(DataService service);

	void deleteService(String serviceId);

	List<DataService> findServicesByStore(String storePackage);

	List<DataService> findServicesByRepository(String repositoryId);

}
