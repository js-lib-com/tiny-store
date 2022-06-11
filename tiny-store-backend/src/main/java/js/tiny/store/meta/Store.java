package js.tiny.store.meta;

import org.bson.types.ObjectId;

import js.tiny.store.dao.IPersistedObject;

public class Store implements IPersistedObject {
	private ObjectId id;

	private String owner;
	private String name;
	private String display;
	private String description;
	/**
	 * Optional REST path for this store, default to null. If used, usually is the the store name. If null this data store does
	 * not have REST support and can be consumed only by Java applications via remote EJB. User interface should reflect missing
	 * REST support and to not display controls related to REST protocol.
	 */
	private String restPath;

	private DeploymentType deploymentType;
	private String gitURL;
	private String mavenServer;
	private String packageName;
	private Version version;

	private String databaseURL;
	private String databaseUser;
	private String databasePassword;

	@Override
	public ObjectId getId() {
		return id;
	}

	@Override
	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
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

	public DeploymentType getDeploymentType() {
		return deploymentType;
	}

	public void setDeploymentType(DeploymentType deploymentType) {
		this.deploymentType = deploymentType;
	}

	public String getGitURL() {
		return gitURL;
	}

	public void setGitURL(String gitURL) {
		this.gitURL = gitURL;
	}

	public String getMavenServer() {
		return mavenServer;
	}

	public void setMavenServer(String mavenServer) {
		this.mavenServer = mavenServer;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public String getDatabaseURL() {
		return databaseURL;
	}

	public void setDatabaseURL(String databaseURL) {
		this.databaseURL = databaseURL;
	}

	public String getDatabaseUser() {
		return databaseUser;
	}

	public void setDatabaseUser(String databaseUser) {
		this.databaseUser = databaseUser;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}
}
