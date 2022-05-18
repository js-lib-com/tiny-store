package js.tiny.store.template;

import java.util.List;

import js.tiny.store.meta.DataService;

public class ProjectTemplate {
	private final List<DataService> services;

	public ProjectTemplate(List<DataService> services) {
		this.services = services;
	}

	public List<DataService> getServices() {
		return services;
	}
}
