package js.tiny.store;

import java.util.Date;

import org.bson.types.ObjectId;

import js.tiny.store.dao.IPersistedObject;

public class ChangeLog implements IPersistedObject {
	private ObjectId id;

	private String storeId;
	private Date timestamp;
	private String change;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}
}
