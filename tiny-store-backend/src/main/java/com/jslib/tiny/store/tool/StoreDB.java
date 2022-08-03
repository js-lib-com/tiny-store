package com.jslib.tiny.store.tool;

import java.sql.SQLException;

import com.jslib.tiny.store.meta.Store;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import js.log.Log;
import js.log.LogFactory;

public class StoreDB implements AutoCloseable {
	private static final Log log = LogFactory.getLog(StoreDB.class);

	private final ComboPooledDataSource datasource;

	public StoreDB(Store store) {
		this.datasource = new ComboPooledDataSource();
		datasource.setAcquireRetryAttempts(1);

		datasource.setJdbcUrl(store.getDatabaseURL());
		datasource.setUser(store.getDatabaseUser());
		datasource.setPassword(store.getDatabasePassword());
	}

	public void sql(WorkUnit workUnit) throws SQLException {
		workUnit.execute(datasource.getConnection());
	}

	@Override
	public void close() {
		try {
			DataSources.destroy(datasource);
		} catch (SQLException e) {
			log.error(e);
		}
	}
}
