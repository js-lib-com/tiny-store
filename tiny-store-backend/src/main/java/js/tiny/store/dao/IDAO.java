package js.tiny.store.dao;

import java.util.List;

import js.tiny.store.meta.DataService;
import js.tiny.store.meta.Repository;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;

public interface IDAO {

	List<StoreEntity> findEntitiesByStore(String storePackage);

	Store getStoreByPackage(String packageName);

	List<Store> findStoresByOwner(String ownerName);

	void createStore(Store store);

	void deleteStore(String packageName);

	List<DataService> findServicesByStore(String storePackage);
	
	List<Repository> findRepositoryByStore(String storePackage);
	
}
