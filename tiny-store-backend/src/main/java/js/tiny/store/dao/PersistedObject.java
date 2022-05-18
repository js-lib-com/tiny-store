package js.tiny.store.dao;

import org.bson.types.ObjectId;

public interface PersistedObject {
	
	void setId(ObjectId id);
	
	ObjectId getId();
	
}
