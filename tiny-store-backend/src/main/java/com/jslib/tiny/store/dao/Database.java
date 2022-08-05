package com.jslib.tiny.store.dao;

import java.util.List;

import com.jslib.container.interceptor.Intercepted;
import com.jslib.tiny.store.ChangeLog;
import com.jslib.tiny.store.ChangeLogListener;
import com.jslib.tiny.store.SourceCodeBuildListener;
import com.jslib.tiny.store.meta.DataService;
import com.jslib.tiny.store.meta.Server;
import com.jslib.tiny.store.meta.ServerType;
import com.jslib.tiny.store.meta.ServiceOperation;
import com.jslib.tiny.store.meta.Store;
import com.jslib.tiny.store.meta.StoreEntity;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Remote;
import jakarta.enterprise.context.ApplicationScoped;

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

	@Intercepted({ ChangeLogListener.class, SourceCodeBuildListener.class })
	StoreEntity createStoreEntity(String storeId, StoreEntity entity);

	@Intercepted({ StoreEntityUpdateListener.class, ChangeLogListener.class, SourceCodeBuildListener.class })
	void updateStoreEntity(StoreEntity entity);

	@Intercepted({ ChangeLogListener.class, SourceCodeBuildListener.class })
	void deleteStoreEntity(StoreEntity entity);

	StoreEntity getStoreEntity(String entityId);

	List<StoreEntity> getStoreEntities(String storeId);

	StoreEntity getStoreEntityByClassName(String storeId, String className);

	List<StoreEntity> findStoreEntityByClassName(String storeId, String className);

	// --------------------------------------------------------------------------------------------

	@Intercepted(ChangeLogListener.class)
	DataService createDataService(String storeId, DataService service);

	@Intercepted({ DataServiceUpdateListener.class, ChangeLogListener.class })
	void updateDataService(DataService service);

	@Intercepted(ChangeLogListener.class)
	void deleteDataService(DataService service);

	DataService getDataService(String serviceId);

	List<DataService> getStoreServices(String storeId);

	DataService getDataServiceByClassName(String storeId, String className);

	List<DataService> findDataServiceByClassName(String storeId, String className);

	// --------------------------------------------------------------------------------------------

	@Intercepted(ChangeLogListener.class)
	ServiceOperation createOperation(ServiceOperation operation);

	@Intercepted(ChangeLogListener.class)
	ServiceOperation createServiceOperation(DataService service, ServiceOperation operation);

	@Intercepted(ChangeLogListener.class)
	void updateServiceOperation(ServiceOperation operation);

	void updateOperationsServiceClass(String serviceId, String serviceClass);

	@Intercepted(ChangeLogListener.class)
	void deleteServiceOperation(ServiceOperation operation);

	List<ServiceOperation> getServiceOperations(String serviceId);

	ServiceOperation getServiceOperation(String operationId);

	// --------------------------------------------------------------------------------------------

	void createServer(Server server);

	void updateServer(Server server);

	void deleteServer(Server server);

	List<Server> getServers();

	Server getServerByHostURL(String hostURL);

	List<Server> findServersByType(ServerType type);

	// --------------------------------------------------------------------------------------------

	void createChangeLog(ChangeLog changeLog);

	void updateChangeLog(ChangeLog changeLog);

	void deleteChangeLog(String storeId);

	List<ChangeLog> getChangeLog(String storeId);
	
	ChangeLog getChangeLogByText(String storeId, String text);

}
