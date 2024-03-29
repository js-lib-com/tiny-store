package com.jslib.tiny.store.template;

import com.jslib.tiny.store.meta.OperationParameter;
import com.jslib.tiny.store.meta.ParameterFlag;
import com.jslib.tiny.store.meta.RestParameter;
import com.jslib.tiny.store.util.Strings;

public class OperationParameterTemplate {
	private final OperationParameter operationParameter;

	private final String type;
	private final boolean primitive;

	public OperationParameterTemplate(OperationParameter operationParameter) {
		this.operationParameter = operationParameter;
		this.type = Strings.simpleParameterizedName(operationParameter.getType());
		this.primitive = Strings.isPrimitive(operationParameter.getType().getName());
	}

	public String getName() {
		return operationParameter.getName();
	}

	public String getDescription() {
		return operationParameter.getDescription();
	}

	public boolean isRestEnabled() {
		return operationParameter.isRestEnabled();
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
