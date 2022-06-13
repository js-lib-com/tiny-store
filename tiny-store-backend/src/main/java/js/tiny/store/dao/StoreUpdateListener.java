package js.tiny.store.dao;

import jakarta.ejb.Asynchronous;
import jakarta.inject.Inject;
import js.tiny.container.interceptor.PostInvokeInterceptor;
import js.tiny.container.interceptor.PreInvokeInterceptor;
import js.tiny.container.spi.IManagedMethod;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.OperationParameter;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.tool.Strings;

public class StoreUpdateListener implements PreInvokeInterceptor, PostInvokeInterceptor {
	private final Database database;

	private String oldPackageName;

	@Inject
	public StoreUpdateListener(Database database) {
		this.database = database;
	}

	@Override
	public void preInvoke(IManagedMethod managedMethod, Object[] arguments) {
		Store store = database.getStore(store(managedMethod, arguments).id());
		oldPackageName = store.getPackageName();
	}

	@Asynchronous
	@Override
	public void postInvoke(IManagedMethod managedMethod, Object[] arguments, Object returnValue) {
		Store store = store(managedMethod, arguments);
		final String newPackageName = store.getPackageName();
		if (oldPackageName.equals(newPackageName)) {
			return;
		}

		for (DataService service : database.getStoreServices(store.id())) {
			final String serviceClass = Strings.qualifiedName(newPackageName, service.getClassName());
			service.setClassName(serviceClass);
			database.updateDataService(service);
			database.updateOperationsServiceClass(service.id(), serviceClass);

			for (ServiceOperation operation : database.getServiceOperations(service.id())) {
				boolean operationDirty = false;
				if (Strings.replacePackage(operation.getValue().getType(), oldPackageName, newPackageName)) {
					operationDirty = true;
				}

				for (OperationParameter parameter : operation.getParameters()) {
					if (Strings.replacePackage(parameter.getType(), oldPackageName, newPackageName)) {
						operationDirty = true;
					}
				}

				if (operationDirty) {
					database.updateServiceOperation(operation);
				}
			}
		}

		for (StoreEntity entity : database.getStoreEntities(store.id())) {
			entity.setClassName(Strings.qualifiedName(newPackageName, entity.getClassName()));
			database.updateStoreEntity(entity);
		}
	}

	private static Store store(IManagedMethod managedMethod, Object[] arguments) {
		for (Object argument : arguments) {
			if (argument instanceof Store) {
				return (Store) argument;
			}
		}
		throw new IllegalArgumentException(String.format("Invalid method signature for |%s|. Missing store argument.", managedMethod));
	}
}
