package js.tiny.store.dao;

import java.util.List;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Remote;
import jakarta.enterprise.context.ApplicationScoped;
import js.tiny.container.interceptor.Intercepted;
import js.tiny.store.ChangeLog;
import js.tiny.store.EntityValidator;
import js.tiny.store.MetaChangeListener;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;

@ApplicationScoped
@Remote
@PermitAll
public interface Database {

	void createStore(Store store);

	void updateStore(Store store);

	void deleteStore(String storeId);

	Store getStore(String storeId);

	Store getStoreByName(String name);

	List<Store> findStoresByOwner(String ownerName);

	// --------------------------------------------------------------------------------------------

	@Intercepted({ EntityValidator.class, MetaChangeListener.class })
	StoreEntity createStoreEntity(String storeId, StoreEntity entity);

	@Intercepted({ EntityValidator.class, MetaChangeListener.class })
	void updateStoreEntity(StoreEntity entity);

	@Intercepted(MetaChangeListener.class)
	void deleteStoreEntity(StoreEntity entity);

	StoreEntity getStoreEntity(String entityId);

	List<StoreEntity> getStoreEntities(String storeId);

	// --------------------------------------------------------------------------------------------

	@Intercepted(MetaChangeListener.class)
	DataService createDataService(String storeId, DataService service);

	@Intercepted(MetaChangeListener.class)
	void updateDataService(DataService service);

	@Intercepted(MetaChangeListener.class)
	void deleteDataService(DataService service);

	DataService getDataService(String serviceId);

	List<DataService> getStoreServices(String storeId);

	// --------------------------------------------------------------------------------------------

	@Intercepted(MetaChangeListener.class)
	ServiceOperation createOperation(ServiceOperation operation);

	@Intercepted(MetaChangeListener.class)
	ServiceOperation createServiceOperation(DataService service, ServiceOperation operation);

	@Intercepted(MetaChangeListener.class)
	void updateServiceOperation(ServiceOperation operation);

	@Intercepted(MetaChangeListener.class)
	void deleteServiceOperation(ServiceOperation operation);

	List<ServiceOperation> getServiceOperations(String serviceId);

	ServiceOperation getServiceOperation(String operationId);

	// --------------------------------------------------------------------------------------------

	void createChangeLog(ChangeLog changeLog);

	void deleteChangeLog(String storeId);

	List<ChangeLog> getChangeLog(String storeId);

}
