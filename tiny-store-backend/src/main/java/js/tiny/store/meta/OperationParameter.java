package js.tiny.store.meta;

public class OperationParameter {
	private TypeDef type;
	private String name;
	private String description;
	private boolean restEnabled;
	private RestParameter restParameter;
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

	public boolean isRestEnabled() {
		return restEnabled;
	}

	public void setRestEnabled(boolean restEnabled) {
		this.restEnabled = restEnabled;
	}

	public RestParameter getRestParameter() {
		return restParameter;
	}

	public void setRestParameter(RestParameter restParameter) {
		this.restParameter = restParameter;
	}

	public ParameterFlag getFlag() {
		return flag;
	}

	public void setFlag(ParameterFlag flag) {
		this.flag = flag;
	}
}
