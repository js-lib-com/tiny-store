package js.tiny.store.dao;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;

import js.tiny.store.meta.DataService;
import js.tiny.store.meta.Repository;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;
import js.util.Params;

class DAO<T> {
	private final MongoCollection<T> collection;

	public DAO(MongoDB mongo, Class<T> type) {
		this.collection = mongo.getCollection(COLLECTIONS.get(type), type);
	}

	public T get(String id) {
		return collection.find(eq("_id", new ObjectId(id))).first();
	}

	public T get(String name, Object value) {
		return collection.find(eq(name, value)).first();
	}

	public T filterAnd(Map<String, String> filters) {
		List<Bson> equalExpressions = new ArrayList<>();
		for (Map.Entry<String, String> entry : filters.entrySet()) {
			equalExpressions.add(eq(entry.getKey(), entry.getValue()));
		}
		return collection.find(and(equalExpressions)).first();
	}

	public List<T> find(String name, Object value) {
		List<T> list = new ArrayList<>();
		collection.find(eq(name, value)).forEach(item -> list.add(item));
		return list;
	}

	public void create(T t) {
		collection.insertOne(t);
	}

	public void update(String name, String value, T t) {
		collection.replaceOne(eq(name, value), t);
	}

	public void update(Map<String, String> filters, T t) {
		Params.GT(filters.size(), 1, "Filters size");
		List<Bson> equalExpressions = new ArrayList<>();
		for (Map.Entry<String, String> entry : filters.entrySet()) {
			equalExpressions.add(eq(entry.getKey(), entry.getValue()));
		}
		collection.replaceOne(and(equalExpressions), t);
	}

	public void delete(String id) {
		collection.deleteOne(eq("_id", new ObjectId(id)));
	}

	public void delete(String name, Object value) {
		collection.deleteOne(eq(name, value));
	}

	private static final Map<Class<?>, String> COLLECTIONS = new HashMap<>();
	static {
		COLLECTIONS.put(Store.class, "store");
		COLLECTIONS.put(Repository.class, "repository");
		COLLECTIONS.put(StoreEntity.class, "entity");
		COLLECTIONS.put(DataService.class, "service");
		COLLECTIONS.put(ServiceOperation.class, "operation");
	}
}
