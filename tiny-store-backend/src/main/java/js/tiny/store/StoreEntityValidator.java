package js.tiny.store;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import jakarta.inject.Inject;
import js.log.Log;
import js.log.LogFactory;
import js.tiny.container.interceptor.PreInvokeInterceptor;
import js.tiny.container.spi.IManagedMethod;
import js.tiny.store.dao.Database;
import js.tiny.store.meta.EntityField;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.tool.Classes;
import js.tiny.store.tool.Strings;
import js.tiny.store.tool.WorkUnit;

public class StoreEntityValidator implements PreInvokeInterceptor {
	private static final Log log = LogFactory.getLog(StoreEntityValidator.class);

	private final Database database;
	private Store store;

	@Inject
	public StoreEntityValidator(Database database) {
		this.database = database;
	}

	@Override
	public void preInvoke(IManagedMethod managedMethod, Object[] arguments) throws Exception {
		String storeId = storeId(managedMethod, arguments);
		store = database.getStore(storeId);

		StoreEntity entity = entity(managedMethod, arguments);

		assertUniqueClass(entity);
		assertUniqueField(entity);
		// TODO: add primary key validation
		assertTableExists(entity);
		assertColumnsExist(entity);
	}

	private String className(StoreEntity entity) {
		return Strings.concat(store.getPackageName(), '.', entity.getClassName());
	}

	private void assertUniqueClass(StoreEntity entity) throws ValidatorException {
		StoreEntity existingEntity = database.getStoreEntityByClassName(store.id(), entity.getClassName());
		if (existingEntity == null) {
			return;
		}
		// entity.id() is null for create operation
		if (!existingEntity.id().equals(entity.id())) {
			throw new ValidatorException("Store entity %s already existing.", className(entity));
		}
	}

	private void assertUniqueField(StoreEntity entity) throws ValidatorException {
		List<EntityField> fields = entity.getFields();
		if (fields == null) {
			return;
		}
		// the number of fields is reasonable small so brute force solution is acceptable
		for (int i = 0; i < fields.size(); i++) {
			for (int j = i + 1; j < fields.size(); j++) {
				if (fields.get(i).getName().equals(fields.get(j).getName())) {
					throw new ValidatorException("Duplicated store entity field %s#%s.", className(entity), fields.get(i).getName());
				}
			}
		}
	}

	private void assertTableExists(StoreEntity entity) throws ValidatorException {
		sql(store, connection -> {
			String tableName = Strings.tableName(entity);
			DatabaseMetaData dbmeta = connection.getMetaData();
			ResultSet tables = dbmeta.getTables(null, null, tableName, null);
			if (!tables.next()) {
				throw new ValidatorException("Missing table %s required by entity %s.", tableName, entity.getClassName());
			}
		});
	}

	private void assertColumnsExist(StoreEntity entity) throws ValidatorException {
		sql(store, connection -> {
			if (entity.getFields() == null) {
				return;
			}
			for (EntityField field : entity.getFields()) {
				String tableName = Strings.tableName(entity);
				String fieldName = Strings.columnName(field);

				DatabaseMetaData dbmeta = connection.getMetaData();
				ResultSet rs = dbmeta.getColumns(null, null, tableName, fieldName);
				if (!rs.next()) {
					throw new ValidatorException("Missing table column %s:%s required by field %s#%s.", tableName, fieldName, className(entity), field.getName());
				}

				Class<?> columnType = Classes.sqlType(rs.getInt("DATA_TYPE"));
				if (columnType == null) {
					throw new IllegalStateException("Not mapped SQL type " + rs.getString("TYPE_NAME"));
				}
				if (field.getType().getCollection() != null) {
					throw new ValidatorException("Collection %s not supported on entity field type %s.", field.getType().getCollection(), field.getType().getName());
				}
				Class<?> fieldType = Classes.forOptionalName(field.getType().getName());
				if (fieldType == null) {
					throw new ValidatorException("Class not found for entity field type %s.", field.getType().getName());
				}
				if (!js.util.Types.isKindOf(columnType, fieldType)) {
					throw new ValidatorException("Incompatible type on table column %s:%s. Table column type is %s while entity field type is %s.", tableName, fieldName, columnType.getCanonicalName(), fieldType.getCanonicalName());
				}
			}
		});
	}

	private static void sql(Store store, WorkUnit workUnit) throws ValidatorException {
		ComboPooledDataSource datasource = new ComboPooledDataSource();
		datasource.setAcquireRetryAttempts(1);

		datasource.setJdbcUrl(store.getDatabaseURL());
		datasource.setUser(store.getDatabaseUser());
		datasource.setPassword(store.getDatabasePassword());

		try {
			workUnit.execute(datasource.getConnection());
		} catch (Exception e) {
			log.error(e);
			throw new ValidatorException(e);
		} finally {
			try {
				DataSources.destroy(datasource);
			} catch (SQLException e) {
				log.error(e);
			}
		}
	}

	private static String storeId(IManagedMethod managedMethod, Object[] arguments) {
		if (arguments.length == 2) {
			return (String) arguments[0];
		}
		if (arguments[0] instanceof StoreEntity) {
			return ((StoreEntity) arguments[0]).getStoreId();
		}
		throw new IllegalArgumentException(String.format("Invalid method signature for |%s|. Cannot infer store ID from arguments.", managedMethod));
	}

	private static StoreEntity entity(IManagedMethod managedMethod, Object[] arguments) {
		for (Object argument : arguments) {
			if (argument instanceof StoreEntity) {
				return (StoreEntity) argument;
			}
		}
		throw new IllegalArgumentException(String.format("Invalid method signature for |%s|. Missing store entity argument.", managedMethod));
	}
}
