package com.jslib.tiny.store;

import java.io.IOException;

import com.jslib.tiny.container.interceptor.PostInvokeInterceptor;
import com.jslib.tiny.container.spi.IManagedMethod;
import com.jslib.tiny.store.dao.Database;
import com.jslib.tiny.store.meta.Store;
import com.jslib.tiny.store.meta.StoreEntity;
import com.jslib.tiny.store.tool.Project;

import jakarta.inject.Inject;

public class SourceCodeBuildListener implements PostInvokeInterceptor {
	private final Context context;
	private final Database database;

	@Inject
	public SourceCodeBuildListener(Context context, Database database) {
		this.context = context;
		this.database = database;
	}

	@Override
	public void postInvoke(IManagedMethod managedMethod, Object[] arguments, Object returnValue) throws Exception {
		IProcessor processor = null;
		switch (managedMethod.getName()) {
		case "createStoreEntity":
		case "importStoreEntity":
			processor = this::createStoreEntity;
			break;

		case "updateStoreEntity":
			processor = this::updateStoreEntity;
			break;

		case "deleteStoreEntity":
			processor = this::deleteStoreEntity;
			break;

		default:
			throw new IllegalStateException("No processor for method " + managedMethod.getName());
		}

		processor.process(managedMethod, arguments, returnValue);
	}

	private void createStoreEntity(IManagedMethod managedMethod, Object[] arguments, Object returnValue) throws IOException {
		String storeId = (String) arguments[0];
		StoreEntity entity = (StoreEntity) arguments[1];
		entity.setStoreId(storeId);
		
		Project project = project(entity);
		project.generateSource(entity);
		project.compileSources();
	}

	private void updateStoreEntity(IManagedMethod managedMethod, Object[] arguments, Object returnValue) throws IOException {
		StoreEntity entity = (StoreEntity) arguments[0];
		
		Project project = project(entity);
		project.generateSource(entity);
		project.compileSources();
	}

	private void deleteStoreEntity(IManagedMethod managedMethod, Object[] arguments, Object returnValue) throws IOException {
		StoreEntity entity = (StoreEntity) arguments[0];
		
		Project project = project(entity);
		project.deleteSource(entity.getClassName());
		project.deleteClass(entity.getClassName());
		project.compileSources();
	}

	private Project project(StoreEntity entity) throws IOException {
		Store store = database.getStore(entity.getStoreId());
		return new Project(context, store, database);
	}

	@FunctionalInterface
	private interface IProcessor {
		void process(IManagedMethod managedMethod, Object[] arguments, Object returnValue) throws Exception;
	}
}
