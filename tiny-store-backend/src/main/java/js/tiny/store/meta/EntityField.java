package js.tiny.store.meta;

import js.util.Strings;

public class EntityField {
	private String name;
	private String alias;
	private String description;
	private TypeDef type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getTitle() {
		return Strings.toTitleCase(name);
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

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public String toString() {
		return "EntityField [name=" + name + ", description=" + description + ", type=" + type + ", alias=" + alias + "]";
	}
}
