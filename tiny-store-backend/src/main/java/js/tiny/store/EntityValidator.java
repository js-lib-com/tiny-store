package js.tiny.store;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class EntityValidator implements PreInvokeInterceptor {
	private static final Log log = LogFactory.getLog(EntityValidator.class);

	private final Database database;

	@Inject
	public EntityValidator(Database database) {
		this.database = database;
	}

	@Override
	public void preInvoke(IManagedMethod managedMethod, Object[] arguments) throws Exception {
		String storeId = storeId(managedMethod, arguments);
		Store store = database.getStore(storeId);
		StoreEntity entity = entity(managedMethod, arguments);

		assertUniqueField(entity);
		assertTableExists(store, entity);
		assertColumnsExist(store, entity);
	}

	private static void assertUniqueField(StoreEntity entity) throws ValidatorException {
		List<EntityField> fields = entity.getFields();
		if (fields == null) {
			return;
		}
		// the number of fields is reasonable small so brute force solution is acceptable
		for (int i = 0; i < fields.size(); i++) {
			for (int j = i + 1; j < fields.size(); j++) {
				if (fields.get(i).getName().equals(fields.get(j).getName())) {
					throw new ValidatorException("Duplicated store entity field %s:%s.", entity.getClassName(), fields.get(i).getName());
				}
			}
		}
	}

	private static void assertTableExists(Store store, StoreEntity entity) throws ValidatorException {
		sql(store, connection -> {
			String tableName = tableName(entity);
			DatabaseMetaData dbmeta = connection.getMetaData();
			ResultSet tables = dbmeta.getTables(null, null, tableName, null);
			if (!tables.next()) {
				throw new ValidatorException("Missing table %s required by entity %s.", tableName, entity.getClassName());
			}
		});
	}

	private static void assertColumnsExist(Store store, StoreEntity entity) throws ValidatorException {
		sql(store, connection -> {
			if (entity.getFields() == null) {
				return;
			}
			for (EntityField field : entity.getFields()) {
				String tableName = tableName(entity);
				String fieldName = fieldName(field);

				DatabaseMetaData dbmeta = connection.getMetaData();
				ResultSet rs = dbmeta.getColumns(null, null, tableName, fieldName);
				if (!rs.next()) {
					throw new ValidatorException("Missing table column %s:%s required by field %s.%s.", tableName, fieldName, entity.getClassName(), field.getName());
				}

				Class<?> columnType = SQL_TYPES.get(rs.getInt("DATA_TYPE"));
				if (columnType == null) {
					throw new IllegalStateException("Not mapped SQL type " + rs.getString("TYPE_NAME"));
				}
				Class<?> fieldType = Classes.forOptionalName(field.getType().getName());
				if (fieldType == null) {
					throw new ValidatorException("Class not found for entity field type %s.", field.getType().getName());
				}
				if (field.getType().getCollection() != null) {
					throw new ValidatorException("Collection %s not supported on entity field type %s.", field.getType().getCollection(), field.getType().getName());
				}
				if (!js.util.Types.isKindOf(columnType, fieldType)) {
					throw new ValidatorException("Incompatible type on table column %s:%s. Table column type is %s while entity field type is %s.", tableName, fieldName, columnType.getCanonicalName(), fieldType.getCanonicalName());
				}
			}
		});
	}

	private static String tableName(StoreEntity entity) {
		String alias = entity.getAlias();
		if (alias != null) {
			return alias;
		}
		return Strings.getSimpleName(entity.getClassName()).toLowerCase();
	}

	private static String fieldName(EntityField field) {
		String alias = field.getAlias();
		if (alias != null) {
			return alias;
		}
		return Strings.memberToUndescroreCase(field.getName());
	}

	@FunctionalInterface
	private interface WorkUnit {
		void execute(Connection connection) throws Exception;
	}

	private static void sql(Store store, WorkUnit workUnit) throws ValidatorException {
		ComboPooledDataSource datasource = new ComboPooledDataSource();
		datasource.setAcquireRetryAttempts(1);

		datasource.setJdbcUrl(store.getConnectionString());
		datasource.setUser(store.getUser());
		datasource.setPassword(store.getPassword());

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

	private static Map<Integer, Class<?>> SQL_TYPES = new HashMap<>();
	static {
		SQL_TYPES.put(Types.BIGINT, long.class);
		SQL_TYPES.put(Types.BIT, byte.class);
		SQL_TYPES.put(Types.BLOB, byte[].class);
		SQL_TYPES.put(Types.BOOLEAN, boolean.class);
		SQL_TYPES.put(Types.CHAR, String.class);
		SQL_TYPES.put(Types.CLOB, byte[].class);
		SQL_TYPES.put(Types.DATE, Date.class);
		SQL_TYPES.put(Types.DECIMAL, BigDecimal.class);
		SQL_TYPES.put(Types.DOUBLE, double.class);
		SQL_TYPES.put(Types.FLOAT, float.class);
		SQL_TYPES.put(Types.INTEGER, int.class);
		SQL_TYPES.put(Types.LONGNVARCHAR, String.class);
		SQL_TYPES.put(Types.LONGVARBINARY, byte[].class);
		SQL_TYPES.put(Types.LONGVARCHAR, String.class);
		SQL_TYPES.put(Types.NCHAR, String.class);
		SQL_TYPES.put(Types.NUMERIC, BigDecimal.class);
		SQL_TYPES.put(Types.NVARCHAR, String.class);
		SQL_TYPES.put(Types.REAL, double.class);
		SQL_TYPES.put(Types.SMALLINT, short.class);
		SQL_TYPES.put(Types.TIME, Timestamp.class);
		SQL_TYPES.put(Types.TIMESTAMP, Timestamp.class);
		SQL_TYPES.put(Types.TINYINT, byte.class);
		SQL_TYPES.put(Types.VARBINARY, byte[].class);
		SQL_TYPES.put(Types.VARCHAR, String.class);
	}
}
