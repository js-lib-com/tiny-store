package com.jslib.tiny.store.template;

import com.jslib.tiny.store.meta.Store;
import com.jslib.tiny.store.meta.Version;
import com.jslib.tiny.store.util.Strings;
import com.jslib.tiny.store.util.URLs;

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
