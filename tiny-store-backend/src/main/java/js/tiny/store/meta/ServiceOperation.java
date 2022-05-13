package js.tiny.store.meta;

import java.util.List;

public class ServiceOperation {
	private String serviceInterface;
	private String name;
	private String description;
	private String restMethod;
	private String restPath;
	
	private List<OperationParameter> parameters;
	private OperationValue value;
	private List<OperationException> exceptions;

	private DataOpcode dataOpcode;
	private String query;

	public String getServiceInterface() {
		return serviceInterface;
	}

	public void setServiceInterface(String serviceInterface) {
		this.serviceInterface = serviceInterface;
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

	public String getRestMethod() {
		return restMethod;
	}

	public void setRestMethod(String restMethod) {
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
