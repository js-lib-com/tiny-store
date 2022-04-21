package js.tiny.store.meta;

public class RepositoryMeta {
	private String name;
	private Database database;
	private String[] services;
	private String[] entities;

	public String getName() {
		return name;
	}

	public Database getDatabase() {
		return database;
	}

	public String[] getServices() {
		return services;
	}

	public String[] getEntities() {
		return entities;
	}
}
