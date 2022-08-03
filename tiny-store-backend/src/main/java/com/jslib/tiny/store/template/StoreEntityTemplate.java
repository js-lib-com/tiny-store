package com.jslib.tiny.store.template;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.jslib.tiny.store.meta.EntityField;
import com.jslib.tiny.store.meta.StoreEntity;
import com.jslib.tiny.store.meta.TypeDef;
import com.jslib.tiny.store.util.Strings;

public class StoreEntityTemplate {
	private final StoreEntity entity;

	private final String packageName;
	private final String className;
	private final SortedSet<String> imports;
	private final List<EntityFieldTemplate> fields;

	public StoreEntityTemplate(StoreEntity entity) {
		this.entity = entity;

		this.packageName = Strings.packageName(entity.getClassName());
		this.className = Strings.simpleName(entity.getClassName());

		SortedSet<String> imports = new TreeSet<>();
		for (EntityField field : entity.getFields()) {
			final TypeDef type = field.getType();
			if (type.getCollection() != null) {
				imports.add(type.getCollection());
			}
			if (Strings.isDefaultPackage(type.getName())) {
				continue;
			}
			if (Strings.isPrimitive(type.getName())) {
				continue;
			}
			if (type.getName().startsWith(packageName)) {
				continue;
			}
			imports.add(type.getName());
		}
		this.imports = imports.isEmpty() ? null : imports;

		this.fields = new ArrayList<>();
		entity.getFields().forEach(field -> this.fields.add(new EntityFieldTemplate(field)));
	}

	public String getDescription() {
		return entity.getDescription();
	}

	public String getAlias() {
		return entity.getAlias();
	}

	public String getPackageName() {
		return packageName;
	}

	public String getClassName() {
		return className;
	}

	public String getQualifiedClassName() {
		return Strings.concat(packageName, '.', className);
	}

	public SortedSet<String> getImports() {
		return imports;
	}

	public List<EntityFieldTemplate> getFields() {
		return fields;
	}
}
