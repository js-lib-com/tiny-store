package js.tiny.store.template;

import js.tiny.store.meta.OperationException;
import js.tiny.store.tool.Strings;

public class OperationExceptionTemplate {
	private final OperationException operationException;
	
	private final String type;

	public OperationExceptionTemplate(OperationException operationException) {
		this.operationException = operationException;
		this.type = Strings.getParameterizedName(operationException.getType());
	}

	public String getCause() {
		return operationException.getCause();
	}

	public String getType() {
		return type;
	}
}
