package js.tiny.store.meta;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import js.json.Json;
import js.util.Classes;

public class Repository {
	private final String name;
	private final Database database;
	private final RepositoryService[] services;
	private final RepositoryEntity[] entities;

	public Repository(File metaDir, RepositoryMeta meta) throws IOException {
		this.name = meta.getName();
		this.database = meta.getDatabase();
		this.services = new RepositoryService[meta.getServices().length];
		this.entities = new RepositoryEntity[meta.getEntities().length];

		Json json = Classes.loadService(Json.class);
		for (int i = 0; i < this.services.length; ++i) {
			try (Reader reader = new FileReader(new File(metaDir, meta.getServices()[i] + ".json"))) {
				this.services[i] = json.parse(reader, RepositoryService.class);
			}
		}
		for (int i = 0; i < this.entities.length; ++i) {
			try (Reader reader = new FileReader(new File(metaDir, meta.getEntities()[i] + ".json"))) {
				this.entities[i] = json.parse(reader, RepositoryEntity.class);
			}
		}
	}

	public String getName() {
		return name;
	}

	public Database getDatabase() {
		return database;
	}

	public RepositoryService[] getServices() {
		return services;
	}

	public RepositoryEntity[] getEntities() {
		return entities;
	}
}
