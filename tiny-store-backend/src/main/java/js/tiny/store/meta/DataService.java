package js.tiny.store.meta;

import java.util.List;

public class DataService {
	/** Qualified package name for parent store. */
	private String storePackage;
	/** Parent repository name. */
	private String repositoryName;
	/** Qualified class name for data service implementation. */
	private String className;
	/** Qualified class name for data service interface. */
	private String interfaceName;
	private String description;
	private String restPath;
	private List<ServiceOperation> operations;

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

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getRestPath() {
		return restPath;
	}

	public void setRestPath(String restPath) {
		this.restPath = restPath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ServiceOperation> getOperations() {
		return operations;
	}

	public void setOperations(List<ServiceOperation> operations) {
		this.operations = operations;
	}
}
