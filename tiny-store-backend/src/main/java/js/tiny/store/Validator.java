package js.tiny.store;

import static java.lang.String.format;
import static js.tiny.store.tool.Strings.charCount;
import static js.tiny.store.tool.Strings.columnName;
import static js.tiny.store.tool.Strings.memberName;
import static js.tiny.store.tool.Strings.operationName;

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
import js.tiny.store.meta.OperationValue;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.meta.TypeDef;
import js.tiny.store.tool.Classes;
import js.tiny.store.tool.FinalInteger;
import js.tiny.store.tool.StoreDB;
import js.tiny.store.tool.Strings;
import js.tiny.store.tool.Types;

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
					if (entity.getClassName().equals(parameter.getType().getName())) {
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
		} catch (Fail fail) {
			return fail.message;
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
		} catch (Fail fail) {
			return fail.message;
		}
		return null;
	}

	private void assertFieldColumn(StoreEntity entity, EntityField field) {
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
		} catch (SQLException e) {
			throw new Fail("%s: %s", e.getClass(), e.getMessage());
		}
	}

	public String assertCreateOperation(DataService service, ServiceOperation operation) {
		for (ServiceOperation existingOperation : db.getServiceOperations(service.id())) {
			if (existingOperation.getName().equals(operation.getName())) {
				return format("Data service operation %s already existing.", operationName(existingOperation));
			}
		}

		return null;
	}

	public String assertEditOperation(ServiceOperation model, ServiceOperation operation) {
		operation.setServiceId(model.getServiceId());
		operation.setServiceClass(model.getServiceClass());
		operation.setParameters(model.getParameters());

		try {
			assertOperation(operation);
		} catch (Fail fail) {
			return fail.message;
		}

		return null;
	}

	public String assertCreateParameter(ServiceOperation operation, OperationParameter parameter) {
		for (OperationParameter existingParameter : operation.getParameters()) {
			if (existingParameter.getName().equals(parameter.getName())) {
				return format("Duplicated operation parameter %s:%s.", operationName(operation), parameter.getName());
			}
		}

		operation.getParameters().add(parameter);
		try {
			assertOperation(operation);
		} catch (Fail fail) {
			return fail.message;
		}

		return null;
	}

	public String assertEditParameter(ServiceOperation operation, int parameterIndex, OperationParameter parameter) {
		List<OperationParameter> parameters = operation.getParameters();
		for (int i = 0; i < parameters.size(); ++i) {
			if(parameterIndex == i) {
				parameters.set(i, parameter);
				continue;
			}
			if(parameters.get(i).getName().equals(parameter.getName())) {
				return format("Duplicated operation parameter %s:%s.", operationName(operation), parameter.getName());
			}
		}

		try {
			assertOperation(operation);
		} catch (Fail fail) {
			return fail.message;
		}

		return null;
	}

	private void assertOperation(ServiceOperation operation) {
		DataService service = db.getDataService(operation.getServiceId());

		if (operation.getQuery() != null) {
			int queryVariablesCount = charCount(operation.getQuery(), '?');

			FinalInteger queryParametersCount = new FinalInteger(0);
			operation.getParameters().forEach(parameter -> {
				String parameterType = parameter.getType().getName();

				if (parameter.getFlag() != null) {
					switch (parameter.getFlag()) {
					case FIRST_RESULT:
						if (!Types.isNumber(parameterType)) {
							throw new Fail("Operation %s parameter flaged as FIRST_RESULT should be a number but is %s.", operationName(operation), parameterType);
						}
						break;

					case MAX_RESULTS:
						if (!Types.isNumber(parameterType)) {
							throw new Fail("Operation %s parameter flaged as MAX_RESULTS should be a number but is %s.", operationName(operation), parameterType);
						}
						break;

					default:
					}
				} else {
					queryParametersCount.increment();
					if (!Types.isPrimitiveLike(parameterType)) {
						throw new Fail("Operation %s query parameter should be a primitive but is %s.", operationName(operation), parameterType);
					}
				}
			});

			if (queryVariablesCount != queryParametersCount.get()) {
				throw new Fail("Query variables count (%d) does not match provided query parameters count (%d).", queryVariablesCount, queryParametersCount.get());
			}
			return;
		}

		switch (operation.getDataOpcode()) {
		case CREATE:
		case UPDATE:
			assertUpdateParameters(service.getStoreId(), operation);
			assertUpdateValue(operation);
			break;

		case READ:
			assertReadParameters(operation);
			assertReadValue(service.getStoreId(), operation);
			break;

		case DELETE:
			assertDeleteParameters(service.getStoreId(), operation);
			assertDeleteValue(operation);
			break;

		default:
			break;
		}
	}

	private void assertUpdateParameters(String storeId, ServiceOperation operation) {
		List<OperationParameter> parameters = operation.getParameters();
		if (parameters == null) {
			// on operation creation parameters are null
			return;
		}

		if (parameters.size() != 1) {
			throw new Fail("CREATE operation %s should have exactly one entity parameter.", operationName(operation));
		}

		OperationParameter parameter = parameters.get(0);
		if (parameter.getType().getCollection() != null) {
			throw new Fail("CREATE operation %s does not support type '%s' as parameter.", operationName(operation), parameter.getType().getCollection());
		}

		String entityName = Strings.simpleName(parameter.getType().getName());
		StoreEntity entity = db.getStoreEntityByClassName(storeId, entityName);
		if (entity == null) {
			throw new Fail("CREATE operation %s requires an entity. Provided parameter type '%s' does not designate a defined entity.", operationName(operation), parameter.getType().getName());
		}
	}

	private void assertUpdateValue(ServiceOperation operation) {
		OperationValue value = operation.getValue();
		if (value == null || value.getType() == null) {
			return;
		}

		if (value.getType().getCollection() != null) {
			throw new Fail("CREATE operation %s does not support type '%s' as return value.", operationName(operation), value.getType().getCollection());
		}
		if (value.getType().getName() == null) {
			return;
		}

		// at this point parameters are already validated
		OperationParameter parameter = operation.getParameters().get(0);
		if (!value.getType().equals(parameter.getType())) {
			throw new Fail("CREATE operation %s should return type '%s'.", operationName(operation), parameter.getType().getName());
		}
	}

	private void assertReadParameters(ServiceOperation operation) {
		List<OperationParameter> parameters = operation.getParameters();
		if (parameters.isEmpty()) {
			return;
		}

		if (parameters.size() == 1) {
			String parameterType = parameters.get(0).getType().getName();
			if (!Types.isPrimitiveLike(parameterType)) {
				throw new Fail("READ operation %s should provide primary key. Parameter type %s cannot be used as primary key.", operationName(operation), parameterType);
			}
			return;
		}

		if (parameters.size() == 2) {
			String parameterType = parameters.get(0).getType().getName();
			if (!parameterType.equals(String.class.getCanonicalName())) {
				throw new Fail("If READ operation %s has two parameters they should be of types %s and %s.", operationName(operation), String.class, Object.class);
			}

			parameterType = parameters.get(1).getType().getName();
			if (!parameterType.equals(Object.class.getCanonicalName())) {
				throw new Fail("If READ operation %s has two parameters they should be of types %s and %s.", operationName(operation), String.class, Object.class);
			}

			return;
		}

		throw new Fail("Bad parameters count (%d) for READ operation %s.", parameters.size(), operationName(operation));
	}

	private void assertReadValue(String storeId, ServiceOperation operation) {
		OperationValue value = operation.getValue();
		TypeDef valueType = value != null ? value.getType() : null;

		if (valueType.getName() == null) {
			throw new Fail("READ operation %s should return a value.", operationName(operation));
		}

		String collectionType = valueType.getCollection();
		if (collectionType != null && !collectionType.equals(List.class.getCanonicalName())) {
			throw new Fail("READ operation %s does support only collection type '%s' as return value.", operationName(operation), List.class.getCanonicalName());
		}

		StoreEntity entity = db.getStoreEntityByClassName(storeId, valueType.getName());
		if (entity == null) {
			throw new Fail("READ operation %s should return an entity or a list of entities. Provided value type '%s' does not designate a defined entity.", operationName(operation), valueType.getName());
		}
	}

	private void assertDeleteParameters(String storeId, ServiceOperation operation) {
		List<OperationParameter> parameters = operation.getParameters();
		if (parameters == null) {
			// on operation creation parameters are null
			return;
		}

		if (parameters.size() != 1) {
			throw new Fail("DELETE operation %s should have exactly one entity parameter.", operationName(operation));
		}

		OperationParameter parameter = parameters.get(0);
		if (parameter.getType().getCollection() != null) {
			throw new Fail("DELETE operation %s does not support type '%s' as parameter.", operationName(operation), parameter.getType().getCollection());
		}

		String entityName = Strings.simpleName(parameter.getType().getName());
		StoreEntity entity = db.getStoreEntityByClassName(storeId, entityName);
		if (entity == null) {
			throw new Fail("DELETE operation %s requires an entity. Provided parameter type '%s' does not designate a defined entity.", operationName(operation), parameter.getType().getName());
		}
	}

	private void assertDeleteValue(ServiceOperation operation) {
		OperationValue value = operation.getValue();
		if (value == null || value.getType() == null) {
			return;
		}

		if (value.getType().getName() != null || value.getType().getCollection() != null) {
			throw new Fail("DELETE operation %s should be void.", operationName(operation));
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
