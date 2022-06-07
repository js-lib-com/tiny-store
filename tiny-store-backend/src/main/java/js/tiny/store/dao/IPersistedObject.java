package js.tiny.store.dao;

import org.bson.types.ObjectId;

public interface IPersistedObject {

	void setId(ObjectId id);

	ObjectId getId();

	public default String id() {
		return getId() != null ? getId().toHexString() : null;
	}

}
