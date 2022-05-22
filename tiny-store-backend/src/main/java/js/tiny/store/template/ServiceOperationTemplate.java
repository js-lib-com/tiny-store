package js.tiny.store.template;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

import js.tiny.store.meta.DataOpcode;
import js.tiny.store.meta.OperationException;
import js.tiny.store.meta.RestParameter;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.TypeDef;
import js.tiny.store.tool.Strings;

public class ServiceOperationTemplate {
	private final ServiceOperation serviceOperation;

	private final SortedSet<String> imports;
	private final List<OperationParameterTemplate> parameters;
	/**
	 * Flag true only if this operation has an entity parameter. An entity parameter is a parameter not annotated
	 * with {@literal}PathParam in which case parameter value is loaded from request body - also know as HTTP entity.
	 */
	private final boolean entityParam;
	/** Flag true if at least one parameter is annotated with {@literal}PathParam. */
	private final boolean pathParam;
	private final OperationValueTemplate value;

	public ServiceOperationTemplate(ServiceOperation serviceOperation) {
		this.serviceOperation = serviceOperation;

		this.imports = new TreeSet<>();
		this.parameters = new ArrayList<>();
		this.value = new OperationValueTemplate(serviceOperation.getValue());

		final AtomicBoolean entityParam = new AtomicBoolean(false);
		final AtomicBoolean pathParam = new AtomicBoolean(false);
		if (serviceOperation.getParameters() != null) {
			serviceOperation.getParameters().forEach(parameter -> {
				addImport(parameter.getType());
				this.parameters.add(new OperationParameterTemplate(parameter));
				if(parameter.getRestParameter() == RestParameter.PATH_PARAM) {
					pathParam.set(true);
				}
				if (parameter.getRestParameter() == RestParameter.ENTITY_PARAM) {
					entityParam.set(true);
				}
			});
		}
		this.entityParam = entityParam.get();
		this.pathParam = pathParam.get();

		if (serviceOperation.getValue().getType().getName() != null) {
			addImport(serviceOperation.getValue().getType());
		}

		if (serviceOperation.getExceptions() != null) {
			serviceOperation.getExceptions().forEach(exception -> {
				addImport(exception.getType());
			});
		}
	}

	private void addImport(TypeDef typedef) {
		if (typedef.getCollection() != null) {
			addImport(typedef.getCollection());
		}
		addImport(typedef.getName());
	}

	private void addImport(String className) {
		if (Strings.isDefaultPackage(className)) {
			return;
		}
		if (Strings.isPrimitive(className)) {
			return;
		}
		imports.add(className);
	}

	public String getName() {
		return serviceOperation.getName();
	}

	public String getRestMethod() {
		return serviceOperation.getRestMethod().name();
	}

	public String getRestPath() {
		return serviceOperation.getRestPath();
	}

	public String getDescription() {
		return serviceOperation.getDescription();
	}

	public List<OperationException> getExceptions() {
		return serviceOperation.getExceptions();
	}

	public DataOpcode getDataOpcode() {
		return serviceOperation.getDataOpcode();
	}

	public String getQuery() {
		return serviceOperation.getQuery();
	}

	public SortedSet<String> getImports() {
		return imports;
	}

	public OperationParameterTemplate getParameter() {
		return getParameters().get(0);
	}

	public List<OperationParameterTemplate> getParameters() {
		return parameters;
	}
	
	public int getParametersCount() {
		return parameters.size();
	}

	public boolean isEntityParam() {
		return entityParam;
	}
	
	public boolean isPathParam() {
		return pathParam;
	}

	public OperationValueTemplate getValue() {
		return value;
	}
}
