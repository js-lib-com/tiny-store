package js.tiny.store.template;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import js.tiny.store.meta.EntityField;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.meta.TypeDef;
import js.tiny.store.tool.Strings;

public class StoreEntityTemplate {
	private final StoreEntity storeEntity;

	private final String packageName;
	private final String className;
	private final SortedSet<String> imports;
	private final List<EntityFieldTemplate> fields;

	public StoreEntityTemplate(Store store, StoreEntity storeEntity) {
		this.storeEntity = storeEntity;

		this.packageName = store.getPackageName();
		this.className = Strings.getSimpleName(storeEntity.getClassName());

		SortedSet<String> imports = new TreeSet<>();
		for (EntityField field : storeEntity.getFields()) {
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
			if(type.getName().startsWith(packageName)) {
				continue;
			}
			imports.add(type.getName());
		}
		this.imports = imports.isEmpty() ? null : imports;

		this.fields = new ArrayList<>();
		storeEntity.getFields().forEach(field -> this.fields.add(new EntityFieldTemplate(field)));
	}

	public String getDescription() {
		return storeEntity.getDescription();
	}

	public String getAlias() {
		return storeEntity.getAlias();
	}

	public String getPackageName() {
		return packageName;
	}

	public String getClassName() {
		return className;
	}

	public SortedSet<String> getImports() {
		return imports;
	}

	public List<EntityFieldTemplate> getFields() {
		return fields;
	}
}
