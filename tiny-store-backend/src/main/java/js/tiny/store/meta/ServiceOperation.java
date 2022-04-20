package js.tiny.store.meta;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class ServiceOperation {
	private String name;
	private String restMethod;
	private String restPath;
	private String description;
	private List<OperationParameter> parameters;
	private OperationValue value;
	private List<OperationException> exceptions;

	private DatabaseOpcode databaseOpcode;
	private String jpql;

	private SortedSet<TypeDef> imports;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public OperationParameter getParameter() {
		return parameters.get(0);
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

	public DatabaseOpcode getDatabaseOpcode() {
		return databaseOpcode;
	}

	public void setDatabaseOpcode(DatabaseOpcode databaseOpcode) {
		this.databaseOpcode = databaseOpcode;
	}

	public String getJpql() {
		return jpql;
	}

	public void setJpql(String jpql) {
		this.jpql = jpql;
	}

	public void setImports(SortedSet<TypeDef> imports) {
		this.imports = imports;
	}

	public SortedSet<TypeDef> getImports() {
		if (imports == null) {
			imports = new TreeSet<>();
			parameters.forEach(parameter -> addImport(parameter.getType()));
			addImport(value.getType());
			exceptions.forEach(exception -> exception.getType());
		}
		return imports;
	}

	private void addImport(TypeDef typedef) {
		if (typedef.isCollection()) {
			imports.add(typedef.getCollection());
		}
		if (typedef.isDefaultPackage()) {
			return;
		}
		if(typedef.isPrimitive()) {
			return;
		}
		imports.add(typedef);
	}
}
