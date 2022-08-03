package com.jslib.tiny.store;

import java.sql.Timestamp;
import java.time.Instant;

import com.jslib.tiny.store.dao.Database;
import com.jslib.tiny.store.meta.DataService;
import com.jslib.tiny.store.meta.ServiceOperation;
import com.jslib.tiny.store.meta.Store;
import com.jslib.tiny.store.meta.StoreEntity;

import jakarta.inject.Inject;
import js.tiny.container.interceptor.PostInvokeInterceptor;
import js.tiny.container.spi.IManagedMethod;

public class ChangeLogListener implements PostInvokeInterceptor {
	private final Database database;

	@Inject
	public ChangeLogListener(Database database) {
		this.database = database;
	}

	@Override
	public void postInvoke(IManagedMethod managedMethod, Object[] arguments, Object value) throws Exception {
		String methodName = managedMethod.getName();

		String storeId = storeId(methodName, arguments);
		String className = className(methodName, arguments, value);
		String memberName = getMemberName(arguments);

		String text = text(methodName, className, memberName);
		ChangeLog changeLog = database.getChangeLogByText(storeId, text);
		if (changeLog == null) {
			changeLog = new ChangeLog();
			changeLog.setStoreId(storeId);
			changeLog.setChange(text);
		}

		changeLog.setTimestamp(Timestamp.from(Instant.now()));
		if (changeLog.id() == null) {
			database.createChangeLog(changeLog);
		} else {
			database.updateChangeLog(changeLog);
		}
	}

	private String storeId(String methodName, Object[] arguments) {
		if (arguments.length == 0) {
			throw new IllegalArgumentException(String.format("Missing argument on method %s#%s.", Workspace.class.getCanonicalName(), methodName));
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
			DataService service = database.getDataService(serviceId);
			return service.getStoreId();
		}

		throw new IllegalArgumentException(String.format("Invalid signature for method %s#%s. Cannot infer store ID from arguments.", Workspace.class.getCanonicalName(), methodName));
	}

	private String className(String methodName, Object[] arguments, Object value) {
		if (arguments.length == 0) {
			throw new IllegalArgumentException(String.format("Missing argument on method %s#%s.", Workspace.class.getCanonicalName(), methodName));
		}

		if (arguments[0] instanceof DataService) {
			return ((DataService) arguments[0]).getClassName();
		}
		if (arguments[0] instanceof StoreEntity) {
			return ((StoreEntity) arguments[0]).getClassName();
		}

		if (methodName.startsWith("create") && value instanceof DataService) {
			return ((DataService) value).getClassName();
		}
		if (methodName.startsWith("create") && value instanceof StoreEntity) {
			return ((StoreEntity) value).getClassName();
		}

		for (Object argument : arguments) {
			if (argument instanceof DataService) {
				return ((DataService) argument).getClassName();
			}
			if (argument instanceof StoreEntity) {
				return ((StoreEntity) argument).getClassName();
			}
		}

		for (Object argument : arguments) {
			if (argument instanceof ServiceOperation) {
				String serviceId = ((ServiceOperation) argument).getServiceId();
				return database.getDataService(serviceId).getClassName();
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
