package js.tiny.store.template;

import js.tiny.store.meta.OperationParameter;
import js.tiny.store.meta.ParameterFlag;
import js.tiny.store.meta.RestParameter;
import js.tiny.store.tool.Strings;

public class OperationParameterTemplate {
	private final OperationParameter operationParameter;

	private final String type;
	private final boolean primitive;

	public OperationParameterTemplate(OperationParameter operationParameter) {
		this.operationParameter = operationParameter;
		this.type = Strings.getParameterizedName(operationParameter.getType());
		this.primitive = Strings.isPrimitive(operationParameter.getType().getName());
	}

	public String getName() {
		return operationParameter.getName();
	}

	public String getDescription() {
		return operationParameter.getDescription();
	}

	public boolean isPathParam() {
		return operationParameter.getRestParameter() == RestParameter.PATH_PARAM;
	}

	public boolean isEntityParam() {
		return operationParameter.getRestParameter() == RestParameter.ENTITY_PARAM;
	}

	public ParameterFlag getFlag() {
		return operationParameter.getFlag();
	}

	public String getType() {
		return type;
	}

	public boolean isPrimitive() {
		return primitive;
	}
}
