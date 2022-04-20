package js.tiny.store.meta;

public class OperationParameter {
	private TypeDef type;
	private String name;
	private String description;
	private String restParamType;
	private String restParamName;
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

	public String getRestParamType() {
		return restParamType;
	}

	public void setRestParamType(String restParamType) {
		this.restParamType = restParamType;
	}

	public String getRestParamName() {
		return restParamName;
	}

	public void setRestParamName(String restParamName) {
		this.restParamName = restParamName;
	}

	public ParameterFlag getFlag() {
		return flag;
	}

	public void setFlag(ParameterFlag flag) {
		this.flag = flag;
	}
}
