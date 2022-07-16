package js.tiny.store.meta;

public class OperationValue {
	private TypeDef type;
	private String description;

	public OperationValue() {

	}

	public OperationValue(Class<?> collection, Class<?> type) {
		this.type = new TypeDef(collection, type);
	}

	public OperationValue(Class<?> type) {
		this.type = new TypeDef(type);
	}

	public OperationValue(String type) {
		this.type = new TypeDef(type);
	}

	public TypeDef getType() {
		return type;
	}

	public void setType(TypeDef type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
