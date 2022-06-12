package js.tiny.store.meta;

import org.bson.types.ObjectId;

import js.tiny.store.dao.IPersistedObject;

public class DataService implements IPersistedObject {
	private ObjectId id;
	private String storeId;
	private String packageName;

	/**
	 * Simple class name for data service implementation. Implementation qualified name is created from store package name and
	 * this simple name. Simple interface name is always the implementation name prefixed with 'I'. Interface qualified name is
	 * obtained the same as implementation.
	 */
	private String className;
	private String description;
	private boolean restEnabled;
	private String restPath;

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

	public String getRestPath() {
		return restPath;
	}

	public void setRestPath(String restPath) {
		this.restPath = restPath;
	}
}
