package js.tiny.store.dao;

import jakarta.ejb.Asynchronous;
import jakarta.inject.Inject;
import js.tiny.container.interceptor.PostInvokeInterceptor;
import js.tiny.container.interceptor.PreInvokeInterceptor;
import js.tiny.container.spi.IManagedMethod;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.OperationParameter;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.tool.Strings;

public class StoreEntityUpdateListener implements PreInvokeInterceptor, PostInvokeInterceptor {
	private final Database database;

	private String oldClassName;

	@Inject
	public StoreEntityUpdateListener(Database database) {
		this.database = database;
	}

	@Override
	public void preInvoke(IManagedMethod managedMethod, Object[] arguments) {
		StoreEntity entity = database.getStoreEntity(entity(managedMethod, arguments).id());
		oldClassName = entity.getClassName();
	}

	@Asynchronous
	@Override
	public void postInvoke(IManagedMethod managedMethod, Object[] arguments, Object returnValue) {
		StoreEntity entity = entity(managedMethod, arguments);

		for (DataService service : database.getStoreServices(entity.getStoreId())) {
			for (ServiceOperation operation : database.getServiceOperations(service.id())) {
				boolean operationDirty = false;
				if (Strings.replaceClass(operation.getValue().getType(), oldClassName, entity.getClassName())) {
					operationDirty = true;
				}

				for (OperationParameter parameter : operation.getParameters()) {
					if (Strings.replaceClass(parameter.getType(), oldClassName, entity.getClassName())) {
						operationDirty = true;
					}
				}

				if (operationDirty) {
					database.updateServiceOperation(operation);
				}
			}
		}
	}

	private static StoreEntity entity(IManagedMethod managedMethod, Object[] arguments) {
		for (Object argument : arguments) {
			if (argument instanceof StoreEntity) {
				return (StoreEntity) argument;
			}
		}
		throw new IllegalArgumentException(String.format("Invalid method signature for |%s|. Missing store entity argument.", managedMethod));
	}
}
