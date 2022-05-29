package js.tiny.store.meta;

import org.bson.types.ObjectId;

import js.tiny.store.dao.PersistedObject;

public class DataService implements PersistedObject {
	private ObjectId id;
	private String storeId;

	/** Qualified class name for data service interface, used as primary key. */
	private String interfaceName;
	/** Qualified class name for data service implementation. */
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

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
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
