package js.tiny.store;

import java.sql.Timestamp;
import java.time.Instant;

import jakarta.inject.Inject;
import js.tiny.container.interceptor.PostInvokeInterceptor;
import js.tiny.container.spi.IManagedMethod;
import js.tiny.store.dao.Database;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;

public class MetaChangeListener implements PostInvokeInterceptor {
	private final Database dao;

	@Inject
	public MetaChangeListener(Database dao) {
		this.dao = dao;
	}

	@Override
	public void postInvoke(IManagedMethod managedMethod, Object[] arguments, Object value) throws Exception {
		String methodName = managedMethod.getName();

		String storeId = getStoreId(methodName, arguments);
		String className = getClassName(methodName, arguments, value);
		String memberName = getMemberName(arguments);

		ChangeLog changeLog = new ChangeLog();
		changeLog.setStoreId(storeId);
		changeLog.setTimestamp(Timestamp.from(Instant.now()));
		changeLog.setChange(text(methodName, className, memberName));

		dao.createChangeLog(changeLog);
	}

	private String getStoreId(String methodName, Object[] arguments) {
		if (arguments.length == 0) {
			throw new IllegalArgumentException(String.format("Missing argument on method %s#%s.", WorkspaceService.class.getCanonicalName(), methodName));
		}

		if (arguments[0] instanceof String) {
			return (String) arguments[0];
		}

		if (arguments[0] instanceof DataService) {
			return ((DataService) arguments[0]).getStoreId();
		}
		if (arguments[0] instanceof StoreEntity) {
			return ((StoreEntity) arguments[0]).getStoreId();
		}

		if (arguments[0] instanceof Store) {
			return ((Store) arguments[0]).id();
		}

		if (arguments[0] instanceof ServiceOperation) {
			String serviceId = ((ServiceOperation) arguments[0]).getServiceId();
			DataService service = dao.getDataService(serviceId);
			return service.getStoreId();
		}

		throw new IllegalArgumentException(String.format("Invalid signature for method %s#%s. Cannot infer store ID from arguments.", WorkspaceService.class.getCanonicalName(), methodName));
	}

	private String getClassName(String methodName, Object[] arguments, Object value) {
		if (arguments.length == 0) {
			throw new IllegalArgumentException(String.format("Missing argument on method %s#%s.", WorkspaceService.class.getCanonicalName(), methodName));
		}

		if (arguments[0] instanceof DataService) {
			return ((DataService) arguments[0]).getInterfaceName();
		}
		if (arguments[0] instanceof StoreEntity) {
			return ((StoreEntity) arguments[0]).getClassName();
		}

		if (methodName.startsWith("create") && value instanceof DataService) {
			return ((DataService) value).getInterfaceName();
		}
		if (methodName.startsWith("create") && value instanceof StoreEntity) {
			return ((StoreEntity) value).getClassName();
		}

		for (Object argument : arguments) {
			if (argument instanceof DataService) {
				return ((DataService) argument).getInterfaceName();
			}
			if (argument instanceof StoreEntity) {
				return ((StoreEntity) argument).getClassName();
			}
		}

		for (Object argument : arguments) {
			if (argument instanceof ServiceOperation) {
				String serviceId = ((ServiceOperation) argument).getServiceId();
				return dao.getDataService(serviceId).getInterfaceName();
			}
		}

		return null;
	}

	private String getMemberName(Object[] arguments) {
		for (Object argument : arguments) {
			if (argument instanceof ServiceOperation) {
				return ((ServiceOperation) argument).getName();
			}
		}
		return null;
	}

	private static String text(String methodName, String className, String memberName) {
		final int length = methodName.length();
		StringBuilder message = new StringBuilder();

		message.append(Character.toUpperCase(methodName.charAt(0)));
		for (int i = 1; i < length; i++) {
			char c = methodName.charAt(i);
			if (Character.isLowerCase(c)) {
				message.append(c);
				continue;
			}
			message.append(' ');
			message.append(Character.toLowerCase(c));
		}

		if (className != null) {
			message.append(' ');
			message.append(className);
			if (memberName != null) {
				message.append('#');
				message.append(memberName);
			}
		}
		message.append('.');

		return message.toString();
	}
}
