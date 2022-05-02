package js.tiny.store.meta;

public class Repository {
	private String storePackage;
	private String name;
	private String display;
	private String description;

	private String connectionString;
	private String user;
	private String password;

	public String getStorePackage() {
		return storePackage;
	}

	public void setStorePackage(String storePackage) {
		this.storePackage = storePackage;
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

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
