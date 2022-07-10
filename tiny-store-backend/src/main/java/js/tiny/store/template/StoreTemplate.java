package js.tiny.store.template;

import js.tiny.store.meta.Store;
import js.tiny.store.util.Strings;

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

	public String getPackageName() {
		return store.getPackageName();
	}

	public String getRestPath() {
		return store.getRestPath();
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
