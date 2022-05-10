package js.tiny.store.dao;

import java.util.List;

import js.tiny.store.meta.DataService;
import js.tiny.store.meta.Repository;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;

public interface IDAO {

	List<StoreEntity> findEntitiesByStore(String storePackage);

	Store getStoreByPackage(String packageName);

	List<Store> findStoresByOwner(String ownerName);

	void createStore(Store store);

	void deleteStore(String packageName);

	List<DataService> findServicesByStore(String storePackage);

	List<DataService> findServicesByRepository(String repositoryName);
	
	List<Repository> findRepositoriesByStore(String storePackage);

	Repository getRepository(String name);

	StoreEntity getStoreEntity(String className);

	DataService getDataService(String interfaceName);

	List<ServiceOperation> findServiceOperations(String serviceInterface);

	ServiceOperation getServiceOperation(String serviceInterface, String methodName);
	
}
