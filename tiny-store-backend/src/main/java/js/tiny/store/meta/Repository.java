package js.tiny.store.meta;

public class Repository {
	private String name;
	private Database database;
	private RepositoryService[] services;
	private RepositoryEntity[] entities;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public RepositoryService[] getServices() {
		return services;
	}

	public void setServices(RepositoryService[] services) {
		this.services = services;
	}

	public RepositoryEntity[] getEntities() {
		return entities;
	}

	public void setEntities(RepositoryEntity[] entities) {
		this.entities = entities;
	}
}
