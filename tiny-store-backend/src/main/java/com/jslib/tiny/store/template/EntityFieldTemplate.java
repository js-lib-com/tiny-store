package com.jslib.tiny.store.template;

import com.jslib.tiny.store.meta.EntityField;
import com.jslib.tiny.store.meta.FieldFlag;
import com.jslib.tiny.store.util.Strings;

public class EntityFieldTemplate {
	private final EntityField entityField;
	private final String type;
	private final String title;

	public EntityFieldTemplate(EntityField entityField) {
		this.entityField = entityField;
		this.type = Strings.simpleParameterizedName(entityField.getType());
		this.title = Character.toUpperCase(entityField.getName().charAt(0)) + entityField.getName().substring(1);
	}

	public String getName() {
		return entityField.getName();
	}

	public String getAlias() {
		return entityField.getAlias();
	}

	public String getDescription() {
		return entityField.getDescription();
	}

	public FieldFlag getFlag() {
		return entityField.getFlag();
	}

	public String getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}
}
