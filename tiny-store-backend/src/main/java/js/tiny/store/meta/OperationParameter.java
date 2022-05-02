package js.tiny.store.meta;

public class OperationParameter {
	private TypeDef type;
	private String name;
	private String description;
	private boolean pathParam;
	private ParameterFlag flag;

	public TypeDef getType() {
		return type;
	}

	public void setType(TypeDef type) {
		this.type = type;
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

	public boolean isPathParam() {
		return pathParam;
	}

	public void setPathParam(boolean pathParam) {
		this.pathParam = pathParam;
	}

	public ParameterFlag getFlag() {
		return flag;
	}

	public void setFlag(ParameterFlag flag) {
		this.flag = flag;
	}
}
