package js.tiny.store.meta;

import java.util.Objects;

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
	public int hashCode() {
		return Objects.hash(collection, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypeDef other = (TypeDef) obj;
		return Objects.equals(collection, other.collection) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "TypeDef [collection=" + collection + ", name=" + name + "]";
	}
}
