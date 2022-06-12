package js.tiny.store.meta;

import java.util.List;

import org.bson.types.ObjectId;

import js.tiny.store.dao.IPersistedObject;

public class StoreEntity implements IPersistedObject {
	private ObjectId id;
	private String storeId;
	private String packageName;

	/** Entity simple class name. Qualified name is created from store package name and this simple name. */
	private String className;
	/** Name for table or collection storing this entity. */
	private String alias;
	private String description;
	private List<EntityField> fields;

	@Override
	public ObjectId getId() {
		return id;
	}

	@Override
	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
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

	public List<EntityField> getFields() {
		return fields;
	}

	public void setFields(List<EntityField> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		return "StoreEntity [className=" + className + ", alias=" + alias + ", description=" + description + "]";
	}
}
