package js.tiny.store.meta;

public class ProjectMeta {
	private String name;
	private String display;
	private String description;
	private String groupId;
	private String version;
	private String author;
	private RepositoryMeta[] repositories;

	public String getName() {
		return name;
	}

	public String getDisplay() {
		return display;
	}

	public String getDescription() {
		return description;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getVersion() {
		return version;
	}

	public String getAuthor() {
		return author;
	}

	public RepositoryMeta[] getRepositories() {
		return repositories;
	}
}
