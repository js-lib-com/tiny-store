package js.tiny.store.tool;

import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import js.log.Log;
import js.log.LogFactory;
import js.tiny.store.meta.Store;

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

	public void sql(WorkUnit workUnit) throws Exception {
		workUnit.execute(datasource.getConnection());
	}

	@Override
	public void close() throws Exception {
		try {
			DataSources.destroy(datasource);
		} catch (SQLException e) {
			log.error(e);
		}
	}
}
