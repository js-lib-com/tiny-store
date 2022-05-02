package js.tiny.store.meta;

import java.util.ArrayList;
import java.util.List;

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

	public boolean isDefaultPackage() {
		return name.startsWith("java.lang.");
	}

	public boolean isVoid() {
		return "void".equalsIgnoreCase(name);
	}

	public boolean isCollection() {
		return collection != null;
	}

	private static final List<String> PRIMITIVES = new ArrayList<>();
	static {
		PRIMITIVES.add("byte");
		PRIMITIVES.add("short");
		PRIMITIVES.add("int");
		PRIMITIVES.add("long");
		PRIMITIVES.add("double");
		PRIMITIVES.add("float");
		PRIMITIVES.add("java.lang.Byte");
		PRIMITIVES.add("java.lang.Short");
		PRIMITIVES.add("java.lang.Integer");
		PRIMITIVES.add("java.lang.Long");
		PRIMITIVES.add("java.lang.Double");
		PRIMITIVES.add("java.lang.Float");
		PRIMITIVES.add("java.lang.String");
	}

	public boolean isPrimitive() {
		return PRIMITIVES.contains(name);
	}

	@Override
	public String toString() {
		return "TypeDef [collection=" + collection + ", name=" + name + "]";
	}
}
