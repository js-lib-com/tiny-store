package js.tiny.store.meta;

public class TypeDef {
	/** Optional qualified name for collection class, null if type is not a collection. */
	private String collection;
	/** Qualified class name. */
	private String name;

	public TypeDef() {
	}

	public TypeDef(String name) {
		this(null, name);
	}

	public TypeDef(String collection, String name) {
		this.collection = collection;
		this.name = name;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getCollection() {
		return collection;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "TypeDef [collection=" + collection + ", name=" + name + "]";
	}
}
