package js.tiny.store.template;

import js.tiny.store.meta.EntityField;
import js.tiny.store.tool.Strings;

public class EntityFieldTemplate {
	private final EntityField entityField;
	private final String type;
	private final String title;

	public EntityFieldTemplate(EntityField entityField) {
		this.entityField = entityField;
		this.type = Strings.getParameterizedName(entityField.getType());
		this.title = Strings.toTitleCase(entityField.getName());
	}

	public String getName() {
		return entityField.getName();
	}

	public String getDescription() {
		return entityField.getDescription();
	}

	public String getAlias() {
		return entityField.getAlias();
	}
	
	public String getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}
}
