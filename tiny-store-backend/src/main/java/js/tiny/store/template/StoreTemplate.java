package js.tiny.store.template;

import js.tiny.store.meta.Store;
import js.tiny.store.meta.Version;
import js.tiny.store.util.Strings;
import js.tiny.store.util.URLs;

public class StoreTemplate {
	private final Store store;

	public StoreTemplate(Store store) {
		this.store = store;
	}

	public String getName() {
		return store.getName();
	}

	public String getDisplay() {
		return store.getDisplay();
	}

	public String getDescription() {
		return store.getDescription();
	}

	public Version getVersion() {
		return store.getVersion();
	}

	public String getPackageName() {
		return store.getPackageName();
	}

	public String getRestPath() {
		return store.getRestPath();
	}

	public String getMavenServer() {
		return store.getMavenServer();
	}

	public String getMavenRepositoryId() {
		return URLs.host(store.getMavenServer());
	}

	public String getDatabaseURL() {
		return Strings.escapeXML(store.getDatabaseURL());
	}

	public String getDatabaseUser() {
		return store.getDatabaseUser();
	}

	public String getDatabasePassword() {
		return Strings.escapeXML(store.getDatabasePassword());
	}
}
