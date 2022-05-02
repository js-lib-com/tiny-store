package js.tiny.store.template;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

import js.tiny.store.meta.DataOpcode;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.TypeDef;

public class ServiceOperationTemplate {
	private final ServiceOperation serviceOperation;

	private final SortedSet<String> imports;
	private final List<OperationParameterTemplate> parameters;
	/**
	 * Flag true only if this operation has an entity parameter. An entity parameter is a parameter not annotated
	 * with @PathParam in which case parameter value is loaded from request body - also know as HTTP entity.
	 */
	private final boolean entityParameter;
	private final OperationValueTemplate value;
	private final List<OperationExceptionTemplate> exceptions;

	public ServiceOperationTemplate(ServiceOperation serviceOperation) {
		this.serviceOperation = serviceOperation;

		this.imports = new TreeSet<>();
		this.parameters = new ArrayList<>();
		this.exceptions = new ArrayList<>();
		this.value = new OperationValueTemplate(serviceOperation.getValue());

		final AtomicBoolean entityParameter = new AtomicBoolean(false);
		if (serviceOperation.getParameters() != null) {
			serviceOperation.getParameters().forEach(parameter -> {
				addImport(parameter.getType());
				this.parameters.add(new OperationParameterTemplate(parameter));
				if (!parameter.isPathParam()) {
					entityParameter.set(true);
				}
			});
		}
		this.entityParameter = entityParameter.get();

		if (serviceOperation.getValue() != null) {
			addImport(serviceOperation.getValue().getType());
		}

		if (serviceOperation.getExceptions() != null) {
			serviceOperation.getExceptions().forEach(exception -> {
				addImport(exception.getType());
				this.exceptions.add(new OperationExceptionTemplate(exception));
			});
		}

	}

	private void addImport(TypeDef typedef) {
		if (typedef.isCollection()) {
			imports.add(typedef.getCollection());
		}
		if (typedef.isDefaultPackage()) {
			return;
		}
		if (typedef.isPrimitive()) {
			return;
		}
		imports.add(typedef.getName());
	}

	public String getName() {
		return serviceOperation.getName();
	}

	public String getRestMethod() {
		return serviceOperation.getRestMethod();
	}

	public String getRestPath() {
		return serviceOperation.getRestPath();
	}

	public String getDescription() {
		return serviceOperation.getDescription();
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

	public boolean isEntityParameter() {
		return entityParameter;
	}

	public OperationValueTemplate getValue() {
		return value;
	}

	public List<OperationExceptionTemplate> getExceptions() {
		return exceptions;
	}
}
