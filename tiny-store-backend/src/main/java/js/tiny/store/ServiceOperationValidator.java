package js.tiny.store;

import java.util.List;

import jakarta.inject.Inject;
import js.tiny.container.interceptor.PreInvokeInterceptor;
import js.tiny.container.spi.IManagedMethod;
import js.tiny.store.dao.Database;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.OperationParameter;
import js.tiny.store.meta.OperationValue;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.tool.Strings;

public class ServiceOperationValidator implements PreInvokeInterceptor {
	private final Database database;
	private Store store;

	@Inject
	public ServiceOperationValidator(Database database) {
		this.database = database;
	}

	@Override
	public void preInvoke(IManagedMethod managedMethod, Object[] arguments) throws Exception {
		String storeId = storeId(managedMethod, arguments);
		store = database.getStore(storeId);

		DataService service = service(managedMethod, arguments);
		ServiceOperation operation = operation(managedMethod, arguments);

		assertUniqueOperation(service, operation);

		switch (operation.getDataOpcode()) {
		case CREATE:
			assertCreateParameters(service, operation);
			assertCreateValue(service, operation);
			break;

		case READ:
			break;

		case UPDATE:
			break;

		case DELETE:
			assertDeleteParameters(service, operation);
			assertDeleteValue(service, operation);
			break;

		default:
			break;
		}

	}

	private String className(DataService service) {
		return Strings.concat(store.getPackageName(), '.', service.getClassName());
	}

	private void assertCreateParameters(DataService service, ServiceOperation operation) throws ValidatorException {
		List<OperationParameter> parameters = operation.getParameters();
		if (parameters == null) {
			// on operation creation parameters are null
			return;
		}

		if (parameters.size() != 1) {
			throw new ValidatorException("Create service operation %s#%s should have exactly one entity parameter.", className(service), operation.getName());
		}

		OperationParameter parameter = parameters.get(0);
		if (parameter.getType().getCollection() != null) {
			throw new ValidatorException("Create service operation %s#%s does not support %s as parameter.", className(service), operation.getName(), parameter.getType().getCollection());
		}

		String entityName = Strings.getSimpleName(parameter.getType().getName());
		StoreEntity entity = database.getStoreEntityByClassName(store.id(), entityName);
		if (entity == null) {
			throw new ValidatorException("Entity %s is not defined.", parameter.getType().getName());
		}
	}

	private void assertCreateValue(DataService service, ServiceOperation operation) throws ValidatorException {
		OperationValue value = operation.getValue();
		if (value == null || value.getType() == null) {
			return;
		}

		if (value.getType().getCollection() != null) {
			throw new ValidatorException("Create service operation %s#%s does not support %s as return value.", service.getClassName(), operation.getName(), value.getType().getCollection());
		}
		if (value.getType().getName() == null) {
			return;
		}

		// at this point parameters are already validated
		OperationParameter parameter = operation.getParameters().get(0);
		if (!value.getType().equals(parameter.getType())) {
			throw new ValidatorException("Create service operation %s#%s should return type %s.", service.getClassName(), operation.getName(), parameter.getType().getName());
		}
	}

	private void assertDeleteParameters(DataService service, ServiceOperation operation) throws ValidatorException {
		List<OperationParameter> parameters = operation.getParameters();
		if (parameters == null) {
			// on operation creation parameters are null
			return;
		}

		if (parameters.size() != 1) {
			throw new ValidatorException("Delete service operation %s#%s should have exactly one entity parameter.", className(service), operation.getName());
		}

		OperationParameter parameter = parameters.get(0);
		if (parameter.getType().getCollection() != null) {
			throw new ValidatorException("Delete service operation %s#%s does not support %s as parameter.", className(service), operation.getName(), parameter.getType().getCollection());
		}

		String entityName = Strings.getSimpleName(parameter.getType().getName());
		StoreEntity entity = database.getStoreEntityByClassName(store.id(), entityName);
		if (entity == null) {
			throw new ValidatorException("Entity %s is not defined.", parameter.getType().getName());
		}
	}

	private void assertDeleteValue(DataService service, ServiceOperation operation) throws ValidatorException {
		OperationValue value = operation.getValue();
		if (value == null || value.getType() == null) {
			return;
		}

		if (value.getType().getName() != null || value.getType().getCollection() != null) {
			throw new ValidatorException("Delete service operation %s#%s should be void.", className(service), operation.getName());
		}
	}

	private void assertUniqueOperation(DataService service, ServiceOperation operation) throws ValidatorException {
		List<ServiceOperation> operations = database.getServiceOperations(service.id());
		if (operation.getId() == null) {
			// current operation is not yet on database and need to add it explicitly
			operations.add(operation);
		} else {
			// replace existing service operation name with that updated from user interface
			for (int i = 0; i < operations.size(); i++) {
				if (operations.get(i).id().equals(operation.id())) {
					operations.get(i).setName(operation.getName());
					break;
				}
			}
		}

		// the number of operations is reasonable small so brute force solution is acceptable
		for (int i = 0; i < operations.size(); i++) {
			for (int j = i + 1; j < operations.size(); j++) {
				if (operations.get(i).getName().equals(operations.get(j).getName())) {
					throw new ValidatorException("Data service operation %s#%s already existing.", className(service), operations.get(i).getName());
				}
			}
		}
	}

	private String storeId(IManagedMethod managedMethod, Object[] arguments) {
		for (Object argument : arguments) {
			if (argument instanceof DataService) {
				return ((DataService) argument).getStoreId();
			}
			if (argument instanceof ServiceOperation) {
				String serviceId = ((ServiceOperation) argument).getServiceId();
				DataService service = database.getDataService(serviceId);
				return service.getStoreId();
			}
		}
		throw new IllegalArgumentException(String.format("Invalid method signature for |%s|. Cannot infer store ID.", managedMethod));
	}

	private ServiceOperation operation(IManagedMethod managedMethod, Object[] arguments) {
		if (arguments.length == 0) {
			throw new IllegalArgumentException(String.format("Invalid method signature for |%s|. Missing arguments.", managedMethod));
		}
		for (Object argument : arguments) {
			if (argument instanceof ServiceOperation) {
				return (ServiceOperation) argument;
			}
		}
		throw new IllegalArgumentException(String.format("Invalid method signature for |%s|. Missing data service operation argument.", managedMethod));
	}

	private DataService service(IManagedMethod managedMethod, Object[] arguments) {
		for (Object argument : arguments) {
			if (argument instanceof DataService) {
				return (DataService) argument;
			}
			if (argument instanceof ServiceOperation) {
				String serviceId = ((ServiceOperation) argument).getServiceId();
				return database.getDataService(serviceId);
			}
		}
		throw new IllegalArgumentException(String.format("Invalid method signature for |%s|. Missing data service argument.", managedMethod));
	}
}
