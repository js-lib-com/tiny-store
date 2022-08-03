package com.jslib.tiny.store.meta;

public class EntityField {
	private String name;
	private String alias;
	private String description;
	private TypeDef type;
	private FieldFlag flag;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TypeDef getType() {
		return type;
	}

	public void setType(TypeDef type) {
		this.type = type;
	}

	public FieldFlag getFlag() {
		return flag;
	}

	public void setFlag(FieldFlag flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "EntityField [name=" + name + ", alias=" + alias + ", description=" + description + ", type=" + type + ", flag=" + flag + "]";
	}
}
