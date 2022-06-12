package js.tiny.store.meta;

import java.util.List;

import org.bson.types.ObjectId;

import js.tiny.store.dao.IPersistedObject;

/**
 * Service operation.
 * 
 * Service operation has a list of parameters, a returned value and a list of exceptions; all optional with default to empty
 * parameters list, empty value object and empty exceptions list.
 * 
 * @author Iulian Rotaru
 */
public class ServiceOperation implements IPersistedObject {
	private ObjectId id;
	private String serviceId;
	private String serviceClass;

	private String name;
	private String description;
	private boolean restEnabled;
	private RestMethod restMethod;
	private String restPath;

	private List<OperationParameter> parameters;
	private OperationValue value;
	private List<OperationException> exceptions;

	private DataOpcode dataOpcode;
	private String query;

	@Override
	public ObjectId getId() {
		return id;
	}

	@Override
	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isRestEnabled() {
		return restEnabled;
	}

	public void setRestEnabled(boolean restEnabled) {
		this.restEnabled = restEnabled;
	}

	public RestMethod getRestMethod() {
		return restMethod;
	}

	public void setRestMethod(RestMethod restMethod) {
		this.restMethod = restMethod;
	}

	public String getRestPath() {
		return restPath;
	}

	public void setRestPath(String restPath) {
		this.restPath = restPath;
	}

	public List<OperationParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<OperationParameter> parameters) {
		this.parameters = parameters;
	}

	public OperationValue getValue() {
		return value;
	}

	public void setValue(OperationValue value) {
		this.value = value;
	}

	public List<OperationException> getExceptions() {
		return exceptions;
	}

	public void setExceptions(List<OperationException> exceptions) {
		this.exceptions = exceptions;
	}

	public DataOpcode getDataOpcode() {
		return dataOpcode;
	}

	public void setDataOpcode(DataOpcode databaseOpcode) {
		this.dataOpcode = databaseOpcode;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
