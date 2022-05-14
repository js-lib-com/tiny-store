package js.tiny.store.meta;

import org.bson.types.ObjectId;

import js.tiny.store.dao.PersistedObject;

public class DataService implements PersistedObject {
	private ObjectId id;

	/** Qualified package name for parent store. */
	private String storePackage;
	/** Parent repository name. */
	private String repositoryName;
	/** Qualified class name for data service interface, used as primary key. */
	private String interfaceName;
	/** Qualified class name for data service implementation. */
	private String className;
	private String description;
	private String restPath;

	@Override
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getStorePackage() {
		return storePackage;
	}

	public void setStorePackage(String storePackage) {
		this.storePackage = storePackage;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String store) {
		this.repositoryName = store;
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

	public String getRestPath() {
		return restPath;
	}

	public void setRestPath(String restPath) {
		this.restPath = restPath;
	}
}
