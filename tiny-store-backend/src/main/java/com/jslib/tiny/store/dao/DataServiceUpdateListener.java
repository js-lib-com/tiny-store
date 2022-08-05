package com.jslib.tiny.store.dao;

import com.jslib.container.interceptor.PostInvokeInterceptor;
import com.jslib.container.spi.IManagedMethod;
import com.jslib.tiny.store.meta.DataService;

import jakarta.inject.Inject;

public class DataServiceUpdateListener implements PostInvokeInterceptor {
	private final Database database;

	@Inject
	public DataServiceUpdateListener(Database database) {
		this.database = database;
	}

	@Override
	public void postInvoke(IManagedMethod managedMethod, Object[] arguments, Object returnValue) throws Exception {
		DataService service = service(managedMethod, arguments);
		database.updateOperationsServiceClass(service.id(), service.getClassName());
	}

	private static DataService service(IManagedMethod managedMethod, Object[] arguments) {
		for (Object argument : arguments) {
			if (argument instanceof DataService) {
				return (DataService) argument;
			}
		}
		throw new IllegalArgumentException(String.format("Invalid method signature for |%s|. Missing data service argument.", managedMethod));
	}
}
