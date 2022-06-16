package js.tiny.store;

import static java.lang.String.format;
import static js.tiny.store.tool.Strings.columnName;
import static js.tiny.store.tool.Strings.memberName;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Remote;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import js.tiny.store.dao.Database;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.EntityField;
import js.tiny.store.meta.OperationParameter;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.meta.TypeDef;
import js.tiny.store.tool.Classes;
import js.tiny.store.tool.StoreDB;
import js.tiny.store.tool.Strings;

@ApplicationScoped
@Remote
@PermitAll
public class Validator {
	private Database db;

	@Inject
	public Validator(Database db) {
		this.db = db;
	}

	public String assertCreateEntity(String storeId, StoreEntity entity) {
		StoreEntity existingEntity = db.getStoreEntityByClassName(storeId, entity.getClassName());
		if (existingEntity != null) {
			return format("Store entity %s already existing.", entity.getClassName());
		}

		try {
			assertEntityTable(storeId, entity);
		} catch (Exception e) {
			return e.getMessage();
		}

		return null;
	}

	public String assertEditEntity(StoreEntity model, StoreEntity entity) {
		StoreEntity existingEntity = db.getStoreEntityByClassName(model.getStoreId(), entity.getClassName());
		if (existingEntity != null) {
			// entity.id() is null for create operation
			if (!existingEntity.id().equals(model.id())) {
				return format("Store entity %s already existing.", entity.getClassName());
			}
		}

		try {
			assertEntityTable(model.getStoreId(), entity);
		} catch (Exception e) {
			return e.getMessage();
		}

		return null;
	}

	private void assertEntityTable(String storeId, StoreEntity entity) throws SQLException {
		Store store = db.getStore(storeId);
		try (StoreDB storeDB = new StoreDB(store)) {
			storeDB.sql(connection -> {
				String tableName = Strings.tableName(entity);
				DatabaseMetaData dbmeta = connection.getMetaData();
				ResultSet tables = dbmeta.getTables(null, null, tableName, null);
				if (!tables.next()) {
					throw new Fail("Missing table %s required by entity %s.", tableName, entity.getClassName());
				}
			});
		}
	}

	public String allowDeleteEntity(StoreEntity entity) {
		for (DataService service : db.getStoreServices(entity.getStoreId())) {
			for (ServiceOperation operation : db.getServiceOperations(service.id())) {
				if (entity.getClassName().equals(operation.getValue().getType().getName())) {
					return String.format("Entity is used by service %s as operation return value.", service.getClassName());
				}
				for (OperationParameter parameter : operation.getParameters()) {
					if(entity.getClassName().equals(parameter.getType().getName())) {
						return String.format("Entity is used by service %s as operation parameter.", service.getClassName());
					}
				}
			}
		}
		return null;
	}

	public String assertCreateField(StoreEntity model, EntityField field) {
		for (EntityField existingField : model.getFields()) {
			if (existingField.getName().equals(field.getName())) {
				return format("Entity field %s already exists.", memberName(model, field));
			}
		}

		try {
			assertFieldColumn(model, field);
		} catch (Exception e) {
			return e.getMessage();
		}
		return null;
	}

	public String assertEditField(StoreEntity model, int fieldIndex, EntityField field) {
		List<EntityField> fields = model.getFields();
		for (int i = 0; i < fields.size(); ++i) {
			if (i == fieldIndex) {
				continue;
			}
			if (fields.get(i).getName().equals(field.getName())) {
				return format("Entity field %s already exists.", memberName(model, field));
			}
		}

		try {
			assertFieldColumn(model, field);
		} catch (Exception e) {
			return e.getMessage();
		}
		return null;
	}

	private void assertFieldColumn(StoreEntity entity, EntityField field) throws SQLException {
		Store store = db.getStore(entity.getStoreId());
		try (StoreDB storeDB = new StoreDB(store)) {
			storeDB.sql(connection -> {
				String tableName = Strings.tableName(entity);
				String columnName = Strings.columnName(field);

				DatabaseMetaData dbmeta = connection.getMetaData();
				ResultSet rs = dbmeta.getColumns(null, null, tableName, columnName);
				if (!rs.next()) {
					throw new Fail("Missing table column %s required by field %s.", columnName(tableName, columnName), memberName(entity, field));
				}
				tableName = rs.getString("TABLE_NAME");

				Class<?> columnType = Classes.sqlType(rs.getInt("DATA_TYPE"));
				if (columnType == null) {
					throw new IllegalStateException("Not mapped SQL type " + rs.getString("TYPE_NAME"));
				}
				if (field.getType().getCollection() != null) {
					throw new Fail("Collection %s not supported on entity field type %s.", field.getType().getCollection(), field.getType());
				}

				Class<?> fieldType = Classes.forOptionalName(field.getType().getName());
				if (fieldType == null) {
					throw new Fail("Class not found for entity field type %s.", field.getType());
				}
				if (!js.util.Types.isKindOf(columnType, fieldType)) {
					throw new Fail("Incompatible type on table column %s. Table column type is %s while entity field type is %s.", columnName(tableName, columnName), columnType, fieldType);
				}
			});
		}
	}

	private static class Fail extends RuntimeException {
		private static final long serialVersionUID = -4580853622043149924L;

		private final String message;

		public Fail(String format, Object... arguments) {
			for (int i = 0; i < arguments.length; ++i) {
				if (arguments[i] instanceof Class) {
					arguments[i] = ((Class<?>) arguments[i]).getCanonicalName();
					continue;
				}
				if (arguments[i] instanceof TypeDef) {
					arguments[i] = ((TypeDef) arguments[i]).getName();
				}
			}
			this.message = format(format, arguments);
		}

		@Override
		public String getMessage() {
			return message;
		}
	}
}
