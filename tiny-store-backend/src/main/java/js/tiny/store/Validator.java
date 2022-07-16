package js.tiny.store;

import static java.lang.String.format;
import static js.tiny.store.util.Strings.charCount;
import static js.tiny.store.util.Strings.columnName;
import static js.tiny.store.util.Strings.memberName;
import static js.tiny.store.util.Strings.operationName;

import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Remote;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.spi.PersistenceProvider;
import js.tiny.store.dao.Database;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.EntityField;
import js.tiny.store.meta.OperationParameter;
import js.tiny.store.meta.OperationValue;
import js.tiny.store.meta.ParameterFlag;
import js.tiny.store.meta.Server;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.meta.TypeDef;
import js.tiny.store.tool.Project;
import js.tiny.store.tool.StoreDB;
import js.tiny.store.util.Classes;
import js.tiny.store.util.FinalInteger;
import js.tiny.store.util.ProjectPersistenceUnitInfo;
import js.tiny.store.util.Strings;
import js.tiny.store.util.Types;
import js.tiny.store.util.URLs;

@ApplicationScoped
@Remote
@PermitAll
public class Validator {
	private final Context context;
	private final Database database;

	@Inject
	public Validator(Context context, Database database) {
		this.context = context;
		this.database = database;
	}

	public String assertCreateStore(Store store) {
		String gitURL = store.getGitURL();
		if (gitURL != null) {
			if (!URLs.isValid(gitURL)) {
				return format("Invalid Git URL: %s", gitURL);
			}
			String hostURL = URLs.hostURL(gitURL);
			Server server = database.getServerByHostURL(hostURL);
			if (server == null) {
				return format("Not registered Git server %s. Please define it on external servers.", hostURL);
			}
		}

		return null;
	}

	public String assertCreateService(String storeId, DataService service) {
		DataService existingService = database.getDataServiceByClassName(storeId, service.getClassName());
		if (existingService != null) {
			return format("Data service %s already existing.", service.getClassName());
		}

		return null;
	}

	public String assertEditService(DataService model, DataService service) {
		DataService existingService = database.getDataServiceByClassName(model.getStoreId(), service.getClassName());
		if (existingService != null) {
			if (!existingService.id().equals(model.id())) {
				return format("Data service %s already existing.", service.getClassName());
			}
		}

		return null;
	}

	public String assertCreateEntity(String storeId, StoreEntity entity) {
		StoreEntity existingEntity = database.getStoreEntityByClassName(storeId, entity.getClassName());
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
		StoreEntity existingEntity = database.getStoreEntityByClassName(model.getStoreId(), entity.getClassName());
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
		Store store = database.getStore(storeId);
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
		for (DataService service : database.getStoreServices(entity.getStoreId())) {
			for (ServiceOperation operation : database.getServiceOperations(service.id())) {
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
		Store store = database.getStore(entity.getStoreId());
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

	public String assertCreateOperation(DataService service, ServiceOperation operation) throws IOException {
		operation.setServiceId(service.id());

		for (ServiceOperation existingOperation : database.getServiceOperations(service.id())) {
			if (existingOperation.getName().equals(operation.getName())) {
				return format("Data service operation %s already existing.", operationName(existingOperation));
			}
		}

		if (operation.getQuery() != null) {
			return assertOperationQuery(operation);
		}

		return null;
	}

	public String assertEditOperation(ServiceOperation model, ServiceOperation operation) throws IOException {
		operation.setServiceId(model.getServiceId());
		operation.setServiceClass(model.getServiceClass());
		operation.setParameters(model.getParameters());

		try {
			assertOperation(operation);
		} catch (Fail fail) {
			return fail.message;
		}

		if (operation.getQuery() != null) {
			return assertOperationQuery(operation);
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
			if (parameterIndex == i) {
				parameters.set(i, parameter);
				continue;
			}
			if (parameters.get(i).getName().equals(parameter.getName())) {
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
		DataService service = database.getDataService(operation.getServiceId());

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

	private String assertOperationQuery(ServiceOperation operation) throws IOException {
		DataService service = database.getDataService(operation.getServiceId());
		Store store = database.getStore(service.getStoreId());

		Project project = new Project(context, store, database);
		List<StoreEntity> entities = database.getStoreEntities(store.id());
		List<String> entitiesClasses = entities.stream().map(entity -> entity.getClassName()).collect(Collectors.toList());

		PersistenceProvider provider = Classes.loadService(PersistenceProvider.class);
		Map<String, Object> configuration = new HashMap<>();
		
		// it is critical to properly close entity manager and its factory
		// otherwise entity mappings are cached and entity byte code is not reloaded
		try ( //
				ProjectPersistenceUnitInfo info = new ProjectPersistenceUnitInfo(project, entitiesClasses);
				EntityManagerFactory factory = provider.createContainerEntityManagerFactory(info, configuration); //
				EntityManager em = factory.createEntityManager(); //
		) {
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();

			try {
				Query query = em.createQuery(operation.getQuery());

				int position = 0;
				for (OperationParameter parameter : operation.getParameters()) {
					if (parameter.getFlag() == ParameterFlag.FIRST_RESULT) {
						query.setFirstResult(0);
						continue;
					}
					if (parameter.getFlag() == ParameterFlag.MAX_RESULTS) {
						query.setMaxResults(1);
						continue;
					}
					query.setParameter(++position, parameter(parameter));
				}

				switch (operation.getDataOpcode()) {
				case CREATE:
					query.executeUpdate();
					break;

				case READ:
					TypeDef valueType = operation.getValue().getType();
					if (valueType.getCollection() != null) {
						query.getResultList();
					} else {
						query.getSingleResult();
					}
					break;

				case UPDATE:
					query.executeUpdate();
					break;

				case DELETE:
					query.executeUpdate();
					break;

				}

			} finally {
				transaction.rollback();
			}
		} catch (Throwable t) {
			return t.getMessage();
		}

		return null;
	}

	private static Object parameter(OperationParameter parameter) {
		switch (parameter.getType().getName()) {
		case "java.lang.String":
			return "string";

		case "java.lang.Boolean":
			return true;

		case "java.lang.Byte":
		case "java.lang.Short":
		case "java.lang.Integer":
		case "java.lang.Long":
			return 1;

		case "java.lang.Float":
		case "java.lang.Double":
			return 1.0;

		case "java.util.Date":
			return new Date();

		case "java.sql.Time":
			return new Time(new Date().getTime());

		case "java.sql.Timestamp":
			return new Timestamp(new Date().getTime());
		}

		return new Object();
	}

	private void assertUpdateParameters(String storeId, ServiceOperation operation) {
		List<OperationParameter> parameters = operation.getParameters();
		if (parameters == null) {
			// on operation creation parameters are null
			return;
		}

		if (parameters.size() != 1) {
			throw new Fail("Operation %s should have exactly one entity parameter.", operationName(operation));
		}

		OperationParameter parameter = parameters.get(0);
		if (parameter.getType().getCollection() != null) {
			throw new Fail("Operation %s does not support type '%s' as parameter.", operationName(operation), parameter.getType().getCollection());
		}

		String entityName = parameter.getType().getName();
		StoreEntity entity = database.getStoreEntityByClassName(storeId, entityName);
		if (entity == null) {
			throw new Fail("Operation %s requires an entity. Provided parameter type '%s' does not designate a defined entity.", operationName(operation), parameter.getType().getName());
		}
	}

	private void assertUpdateValue(ServiceOperation operation) {
		OperationValue value = operation.getValue();
		if (value == null || value.getType() == null) {
			return;
		}

		if (value.getType().getCollection() != null) {
			throw new Fail("Operation %s does not support type '%s' as return value.", operationName(operation), value.getType().getCollection());
		}
		if (value.getType().getName() == null) {
			return;
		}

		// at this point parameters are already validated
		OperationParameter parameter = operation.getParameters().get(0);
		if (!value.getType().equals(parameter.getType())) {
			throw new Fail("Operation %s should return type '%s'.", operationName(operation), parameter.getType().getName());
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
				throw new Fail("Operation %s should have primary key parameter. Provided type %s cannot be used as primary key.", operationName(operation), parameterType);
			}
			return;
		}

		if (parameters.size() == 2) {
			String parameterType = parameters.get(0).getType().getName();
			if (!parameterType.equals(String.class.getCanonicalName())) {
				throw new Fail("If operation %s has two parameters they should be of types %s and %s.", operationName(operation), String.class, Object.class);
			}

			parameterType = parameters.get(1).getType().getName();
			if (!parameterType.equals(Object.class.getCanonicalName())) {
				throw new Fail("If operation %s has two parameters they should be of types %s and %s.", operationName(operation), String.class, Object.class);
			}

			return;
		}

		throw new Fail("Bad parameters count (%d) for operation %s.", parameters.size(), operationName(operation));
	}

	private void assertReadValue(String storeId, ServiceOperation operation) {
		OperationValue value = operation.getValue();
		TypeDef valueType = value != null ? value.getType() : null;

		if (valueType.getName() == null) {
			throw new Fail("Operation %s should return a value.", operationName(operation));
		}

		String collectionType = valueType.getCollection();
		if (collectionType != null && !collectionType.equals(List.class.getCanonicalName())) {
			throw new Fail("Operation %s does support only collection type '%s' as return value.", operationName(operation), List.class.getCanonicalName());
		}

		StoreEntity entity = database.getStoreEntityByClassName(storeId, valueType.getName());
		if (entity == null) {
			throw new Fail("Operation %s should return an entity or a list of entities. Provided value type '%s' does not designate a defined entity.", operationName(operation), valueType.getName());
		}
	}

	private void assertDeleteParameters(String storeId, ServiceOperation operation) {
		List<OperationParameter> parameters = operation.getParameters();
		if (parameters == null) {
			// on operation creation parameters are null
			return;
		}

		if (parameters.size() != 1) {
			throw new Fail("Operation %s should have exactly one entity parameter.", operationName(operation));
		}

		OperationParameter parameter = parameters.get(0);
		if (parameter.getType().getCollection() != null) {
			throw new Fail("Operation %s does not support type '%s' as parameter.", operationName(operation), parameter.getType().getCollection());
		}

		String entityName = Strings.simpleName(parameter.getType().getName());
		StoreEntity entity = database.getStoreEntityByClassName(storeId, entityName);
		if (entity == null) {
			throw new Fail("Operation %s requires an entity. Provided parameter type '%s' does not designate a defined entity.", operationName(operation), parameter.getType().getName());
		}
	}

	private void assertDeleteValue(ServiceOperation operation) {
		OperationValue value = operation.getValue();
		if (value == null || value.getType() == null) {
			return;
		}

		if (value.getType().getName() != null || value.getType().getCollection() != null) {
			throw new Fail("Operation %s should be void.", operationName(operation));
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
