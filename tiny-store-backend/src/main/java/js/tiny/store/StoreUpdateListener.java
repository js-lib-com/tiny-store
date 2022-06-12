package js.tiny.store;

import jakarta.ejb.Asynchronous;
import jakarta.inject.Inject;
import js.tiny.container.interceptor.PostInvokeInterceptor;
import js.tiny.container.spi.IManagedMethod;
import js.tiny.store.dao.Database;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;
import js.util.Strings;

public class StoreUpdateListener implements PostInvokeInterceptor {
	private final Database database;

	@Inject
	public StoreUpdateListener(Database database) {
		this.database = database;
	}

	@Override
	@Asynchronous
	public void postInvoke(IManagedMethod managedMethod, Object[] arguments, Object returnValue) {
		Store store = store(managedMethod, arguments);
		final String packageName = store.getPackageName();

		for (DataService service : database.getStoreServices(store.id())) {
			service.setPackageName(packageName);
			database.updateDataService(service);

			final String serviceClass = Strings.concat(store.getPackageName(), '.', service.getClassName());
			database.updateOperationsServiceClass(service.id(), serviceClass);
		}

		for (StoreEntity entity : database.getStoreEntities(store.id())) {
			entity.setPackageName(packageName);
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
