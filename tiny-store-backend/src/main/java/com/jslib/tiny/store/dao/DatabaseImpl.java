package com.jslib.tiny.store.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jslib.tiny.store.ChangeLog;
import com.jslib.tiny.store.meta.DataService;
import com.jslib.tiny.store.meta.Server;
import com.jslib.tiny.store.meta.ServerType;
import com.jslib.tiny.store.meta.ServiceOperation;
import com.jslib.tiny.store.meta.Store;
import com.jslib.tiny.store.meta.StoreEntity;

import jakarta.inject.Inject;

public class DatabaseImpl implements Database {
	private final DAO<Store> storeDAO;
	private final DAO<StoreEntity> entityDAO;
	private final DAO<DataService> serviceDAO;
	private final DAO<ServiceOperation> operationDAO;
	private final DAO<Server> serverDAO;
	private final DAO<ChangeLog> changeLogDAO;

	@Inject
	public DatabaseImpl(MongoDB mongo) {
		this.storeDAO = new DAO<>(mongo, Store.class);
		this.entityDAO = new DAO<>(mongo, StoreEntity.class);
		this.serviceDAO = new DAO<>(mongo, DataService.class);
		this.operationDAO = new DAO<>(mongo, ServiceOperation.class);
		this.serverDAO = new DAO<>(mongo, Server.class);
		this.changeLogDAO = new DAO<>(mongo, ChangeLog.class);
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
	public void updateStore(Store store) {
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
	public ServiceOperation createServiceOperation(DataService service, ServiceOperation operation) {
		operation.setServiceId(service.id());
		operation.setServiceClass(service.getClassName());
		operation.setRestEnabled(service.isRestEnabled());
		operation.setParameters(new ArrayList<>());
		operation.setExceptions(new ArrayList<>());
		return operationDAO.create(operation);
	}

	@Override
	public void updateServiceOperation(ServiceOperation operation) {
		operationDAO.update(operation);
	}

	@Override
	public void updateOperationsServiceClass(String serviceId, String serviceClass) {
		// update Operation set serviceClass=$1 where serviceId=$2
		operationDAO.update("serviceClass", serviceClass, "serviceId", serviceId);
	}

	@Override
	public void deleteServiceOperation(ServiceOperation operation) {
		operationDAO.delete(operation.id());
	}

	@Override
	public List<ServiceOperation> getServiceOperations(String serviceId) {
		return operationDAO.find("serviceId", serviceId);
	}

	@Override
	public ServiceOperation getServiceOperation(String operationId) {
		return operationDAO.get(operationId);
	}

	@Override
	public StoreEntity createStoreEntity(String storeId, StoreEntity entity) {
		entity.setStoreId(storeId);
		entity.setFields(new ArrayList<>(0));
		return entityDAO.create(entity);
	}

	@Override
	public void updateStoreEntity(StoreEntity entity) {
		entityDAO.update(entity);
	}

	@Override
	public void deleteStoreEntity(StoreEntity entity) {
		entityDAO.delete(entity.id());
	}

	@Override
	public List<StoreEntity> getStoreEntities(String storeId) {
		return entityDAO.find("storeId", storeId);
	}

	@Override
	public StoreEntity getStoreEntityByClassName(String storeId, String className) {
		Map<String, Object> filter = new HashMap<>();
		filter.put("storeId", storeId);
		filter.put("className", className);
		return entityDAO.get(filter);
	}

	@Override
	public List<StoreEntity> findStoreEntityByClassName(String storeId, String className) {
		Map<String, Object> filter = new HashMap<>();
		filter.put("storeId", storeId);
		filter.put("className", className);
		return entityDAO.find(filter);
	}

	@Override
	public DataService createDataService(String storeId, DataService service) {
		service.setStoreId(storeId);
		return serviceDAO.create(service);
	}

	@Override
	public void updateDataService(DataService service) {
		serviceDAO.update(service);
	}

	@Override
	public void deleteDataService(DataService service) {
		serviceDAO.delete(service.id());
	}

	@Override
	public List<DataService> getStoreServices(String storeId) {
		return serviceDAO.find("storeId", storeId);
	}

	@Override
	public DataService getDataServiceByClassName(String storeId, String className) {
		Map<String, Object> filter = new HashMap<>();
		filter.put("storeId", storeId);
		filter.put("className", className);
		return serviceDAO.get(filter);
	}

	@Override
	public List<DataService> findDataServiceByClassName(String storeId, String className) {
		Map<String, Object> filter = new HashMap<>();
		filter.put("storeId", storeId);
		filter.put("className", className);
		return serviceDAO.find(filter);
	}

	@Override
	public void createServer(Server server) {
		serverDAO.create(server);
	}

	@Override
	public void updateServer(Server server) {
		serverDAO.update(server);
	}

	@Override
	public void deleteServer(Server server) {
		serverDAO.delete(server.id());
	}

	@Override
	public List<Server> getServers() {
		return serverDAO.getAll();
	}

	@Override
	public Server getServerByHostURL(String hostURL) {
		return serverDAO.get("hostURL", hostURL);
	}

	@Override
	public List<Server> findServersByType(ServerType type) {
		return serverDAO.find("type", type);
	}

	@Override
	public void createChangeLog(ChangeLog changeLog) {
		changeLogDAO.create(changeLog);
	}

	@Override
	public void updateChangeLog(ChangeLog changeLog) {
		changeLogDAO.update(changeLog);
	}

	@Override
	public List<ChangeLog> getChangeLog(String storeId) {
		return changeLogDAO.find("storeId", storeId);
	}

	@Override
	public void deleteChangeLog(String storeId) {
		changeLogDAO.delete("storeId", storeId);
	}

	@Override
	public ChangeLog getChangeLogByText(String storeId, String text) {
		Map<String, Object> filters = new HashMap<>();
		filters.put("storeId", storeId);
		filters.put("change", text);
		return changeLogDAO.get(filters);
	}
}
