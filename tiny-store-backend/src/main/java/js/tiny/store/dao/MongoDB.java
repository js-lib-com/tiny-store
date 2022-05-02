package js.tiny.store.dao;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import jakarta.annotation.PreDestroy;
import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Startup
public class MongoDB {
	private static final String MONGO_URL = "mongodb://10.138.44.35:27017";
	private static final String MONG_DB = "tiny_store";

	private final MongoClient client;
	private final MongoDatabase database;

	public MongoDB() {
		ConnectionString connectionString = new ConnectionString(MONGO_URL);
		CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
		CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

		MongoClientSettings clientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).codecRegistry(codecRegistry).build();
		this.client = MongoClients.create(clientSettings);
		this.database = this.client.getDatabase(MONG_DB);
	}

	@PreDestroy
	private void preDestroy() {
		client.close();
	}

	public <T> MongoCollection<T> getCollection(String collectionName, Class<T> type) {
		return database.getCollection(collectionName, type);
	}
}
