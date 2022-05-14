package js.tiny.store.meta;

import java.util.List;

import org.bson.types.ObjectId;

import js.tiny.store.dao.PersistedObject;

public class StoreEntity implements PersistedObject {
	private ObjectId id;
	private String storeId;
	
	/** Entity qualified class name. */
	private String className;
	/** Name for table or collection storing this entity. */
	private String alias;
	private String description;
	/** Primary key or ID on database record. */
	private Identity identity;
	private List<EntityField> fields;

	@Override
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
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

	public Identity getIdentity() {
		return identity;
	}

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	public List<EntityField> getFields() {
		return fields;
	}

	public void setFields(List<EntityField> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		return "RepositoryEntity [className=" + className + ", description=" + description + ", alias=" + alias + ", identity=" + identity + ", fields=" + fields + "]";
	}
}
