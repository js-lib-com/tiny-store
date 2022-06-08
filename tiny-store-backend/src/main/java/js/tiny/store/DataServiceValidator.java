package js.tiny.store;

import jakarta.inject.Inject;
import js.tiny.container.interceptor.PreInvokeInterceptor;
import js.tiny.container.spi.IManagedMethod;
import js.tiny.store.dao.Database;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.Store;

public class DataServiceValidator implements PreInvokeInterceptor {
	private final Database database;
	private Store store;

	@Inject
	public DataServiceValidator(Database database) {
		this.database = database;
	}

	@Override
	public void preInvoke(IManagedMethod managedMethod, Object[] arguments) throws Exception {
		String storeId = storeId(managedMethod, arguments);
		store = database.getStore(storeId);

		DataService service = service(managedMethod, arguments);
		validateService(service);
	}

	public void validateService(DataService service) throws ValidatorException {
		assertUniqueClass(service);
	}
	
	private void assertUniqueClass(DataService service) throws ValidatorException {
		if (service.id() == null) {
			DataService existingService = database.getDataServiceByClassName(store.id(), service.getClassName());
			if (existingService != null) {
				throw new ValidatorException("Data service %s already existing.", service.getClassName());
			}
			return;
		}

		for (DataService existingService : database.findDataServiceByClassName(store.id(), service.getClassName())) {
			if (!existingService.id().equals(service.id())) {
				throw new ValidatorException("Data service %s already existing.", service.getClassName());
			}
		}
	}

	private String storeId(IManagedMethod managedMethod, Object[] arguments) {
		for (Object argument : arguments) {
			if(argument instanceof String) {
				return (String) argument;
			}
			if (argument instanceof DataService) {
				return ((DataService) argument).getStoreId();
			}
		}
		throw new IllegalArgumentException(String.format("Invalid method signature for |%s|. Cannot infer store ID.", managedMethod));
	}

	private DataService service(IManagedMethod managedMethod, Object[] arguments) {
		for (Object argument : arguments) {
			if (argument instanceof DataService) {
				return (DataService) argument;
			}
		}
		throw new IllegalArgumentException(String.format("Invalid method signature for |%s|. Missing data service argument.", managedMethod));
	}
}
