package com.jslib.tiny.store.dao;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.jslib.tiny.store.Context;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.jslib.util.Params;

@ApplicationScoped
public class MongoDB {
	private MongoClient client;
	private MongoDatabase database;

	@Inject
	public MongoDB(Context context) {
		Params.notNull(context.getMongoURL(), "Context Mongo URL");
		Params.notNull(context.getMongoDatabaseName(), "Context Mongo database name");
		
		ConnectionString connectionString = new ConnectionString(context.getMongoURL());
		CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
		CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

		MongoClientSettings clientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).codecRegistry(codecRegistry).build();
		this.client = MongoClients.create(clientSettings);
		this.database = this.client.getDatabase(context.getMongoDatabaseName());
	}

	@PreDestroy
	private void preDestroy() {
		client.close();
	}

	public <T> MongoCollection<T> getCollection(String collectionName, Class<T> type) {
		return database.getCollection(collectionName, type);
	}
}
