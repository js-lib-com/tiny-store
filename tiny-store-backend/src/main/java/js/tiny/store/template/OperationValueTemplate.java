package js.tiny.store.template;

import js.tiny.store.meta.OperationValue;
import js.tiny.store.tool.Strings;

public class OperationValueTemplate {
	private final String type;
	private final String rawType;
	private final String description;
	private final boolean collection;

	public OperationValueTemplate(OperationValue operationValue) {
		if (operationValue.getType().getName() != null) {
			this.type = Strings.simpleParameterizedName(operationValue.getType());
			this.rawType = Strings.simpleName(operationValue.getType().getName());
			this.description = operationValue.getDescription();
			this.collection = operationValue.getType().getCollection() != null;
		} else {
			this.type = "void";
			this.rawType = null;
			this.description = null;
			this.collection = false;
		}
	}

	public String getType() {
		return type;
	}

	public boolean isVoid() {
		return "void".equals(type);
	}

	public boolean isCollection() {
		return collection;
	}

	public String getRawType() {
		return rawType;
	}

	public String getDescription() {
		return description;
	}
}
