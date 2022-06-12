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
import com.mongodb.client.model.Updates;

import js.tiny.store.ChangeLog;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;

class DAO<T extends IPersistedObject> {
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

	public T get(Map<String, Object> filters) {
		List<Bson> equalExpressions = new ArrayList<>();
		for (Map.Entry<String, Object> entry : filters.entrySet()) {
			equalExpressions.add(eq(entry.getKey(), entry.getValue()));
		}
		return collection.find(and(equalExpressions)).first();
	}

	public List<T> find(String name, Object value) {
		List<T> list = new ArrayList<>();
		collection.find(eq(name, value)).forEach(item -> list.add(item));
		return list;
	}

	public List<T> find(Map<String, Object> filters) {
		List<Bson> equalExpressions = new ArrayList<>();
		for (Map.Entry<String, Object> entry : filters.entrySet()) {
			equalExpressions.add(eq(entry.getKey(), entry.getValue()));
		}
		List<T> list = new ArrayList<>();
		collection.find(and(equalExpressions)).forEach(item -> list.add(item));
		return list;
	}

	public T create(T t) {
		t.setId(collection.insertOne(t).getInsertedId().asObjectId().getValue());
		return t;
	}

	public void update(T t) {
		collection.replaceOne(eq("_id", t.getId()), t);
	}

	public void update(String fieldName, Object fieldValue, String whereName, Object whereValue) {
		Bson query = eq(whereName, whereValue);
		Bson updates = Updates.set(fieldName, fieldValue);
		collection.updateMany(query, updates);
	}

	public void delete(String id) {
		collection.deleteOne(eq("_id", new ObjectId(id)));
	}

	public void delete(String name, Object value) {
		collection.deleteMany(eq(name, value));
	}

	private static final Map<Class<?>, String> COLLECTIONS = new HashMap<>();
	static {
		COLLECTIONS.put(Store.class, "store");
		COLLECTIONS.put(StoreEntity.class, "entity");
		COLLECTIONS.put(DataService.class, "service");
		COLLECTIONS.put(ServiceOperation.class, "operation");
		COLLECTIONS.put(ChangeLog.class, "changelog");
	}
}
