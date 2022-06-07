package js.tiny.store;

import jakarta.inject.Inject;
import js.tiny.container.interceptor.PreInvokeInterceptor;
import js.tiny.container.spi.IManagedMethod;
import js.tiny.store.dao.Database;
import js.tiny.store.meta.DataService;

public class DataServiceValidator implements PreInvokeInterceptor {
	private final Database database;

	@Inject
	public DataServiceValidator(Database database) {
		this.database = database;
	}

	@Override
	public void preInvoke(IManagedMethod managedMethod, Object[] arguments) throws Exception {
		DataService service = service(managedMethod, arguments);
		validateService(service);
	}

	public void validateService(DataService service) throws ValidatorException {
		assertUniqueClass(service);
	}
	
	private void assertUniqueClass(DataService service) throws ValidatorException {
		if (service.id() == null) {
			DataService existingService = database.getDataServiceByClassName(service.getClassName());
			if (existingService != null) {
				throw new ValidatorException("Data service %s already existing.", service.getClassName());
			}

			existingService = database.getDataServiceByInterfaceName(service.getInterfaceName());
			if (existingService != null) {
				throw new ValidatorException("Data service interface %s already existing.", service.getInterfaceName());
			}
			return;
		}

		for (DataService existingService : database.findDataServiceByClassName(service.getClassName())) {
			if (!existingService.id().equals(service.id())) {
				throw new ValidatorException("Data service %s already existing.", service.getClassName());
			}
		}

		for (DataService existingService : database.findDataServiceByInterfaceName(service.getInterfaceName())) {
			if (!existingService.id().equals(service.id())) {
				throw new ValidatorException("Data service interface %s already existing.", service.getInterfaceName());
			}
		}
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
