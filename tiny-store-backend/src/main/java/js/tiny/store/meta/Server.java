package js.tiny.store.meta;

import org.bson.types.ObjectId;

import js.tiny.store.dao.IPersistedObject;

/**
 * Server providing external services like Maven or Git repositories.
 * 
 * @author Iulian Rotaru
 */
public class Server implements IPersistedObject {
	private ObjectId id;

	private ServerType type;
	private String hostURL;
	private String username;
	private String password;

	@Override
	public String id() {
		return id.toHexString();
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public ServerType getType() {
		return type;
	}

	public void setType(ServerType type) {
		this.type = type;
	}

	public String getHostURL() {
		return hostURL;
	}

	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Server [type=" + type + ", hostURL=" + hostURL + ", username=" + username + ", password=" + password + "]";
	}
}
