package js.tiny.store.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import js.json.JsonLifeCycle;
import js.util.Strings;

public class TypeDef implements JsonLifeCycle, Comparable<TypeDef> {
	private TypeDef collection;
	private String qualifiedName;

	/** Parameterized name, e.g. List<String> or String */
	private transient String name;
	private transient String packageName;
	private transient String simpleName;

	public TypeDef() {
	}

	public TypeDef(String qualifiedName) {
		this(null, qualifiedName);
	}

	public TypeDef(TypeDef collection, String qualifiedName) {
		this.collection = collection;

		this.qualifiedName = qualifiedName;
		int lastDotSeparatorPosition = qualifiedName.lastIndexOf('.');
		if (lastDotSeparatorPosition != -1) {
			// package can be missing for primitive type, e.g. int.class
			this.packageName = qualifiedName.substring(0, lastDotSeparatorPosition);
			this.simpleName = qualifiedName.substring(lastDotSeparatorPosition + 1);
		} else {
			this.packageName = null;
			this.simpleName = qualifiedName;
		}

		if (collection != null) {
			this.name = Strings.concat(collection.simpleName, '<', this.simpleName, '>');
		} else {
			this.name = this.simpleName;
		}
	}

	public TypeDef(Class<?> type) {
		this(null, type);
	}

	public TypeDef(TypeDef collection, Class<?> type) {
		this(collection, type.getCanonicalName());
	}

	@Override
	public void preStringify() {
	}

	@Override
	public void postParse() {
		int lastDotSeparatorPosition = qualifiedName.lastIndexOf('.');
		if (lastDotSeparatorPosition != -1) {
			// package can be missing for primitive type, e.g. int.class
			packageName = qualifiedName.substring(0, lastDotSeparatorPosition);
			simpleName = qualifiedName.substring(lastDotSeparatorPosition + 1);
		} else {
			packageName = null;
			simpleName = qualifiedName;
		}

		if (collection != null) {
			name = Strings.concat(collection.simpleName, '<', simpleName, '>');
		} else {
			name = simpleName;
		}
	}

	public TypeDef getCollection() {
		return collection;
	}

	public String getName() {
		return name;
	}

	public String getQualifiedName() {
		return qualifiedName;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public String getInterfaceName() {
		return Strings.concat(packageName, '.', "I", simpleName);
	}

	public boolean isDefaultPackage() {
		return "java.lang".equals(packageName);
	}

	public boolean isVoid() {
		return "void".equalsIgnoreCase(simpleName);
	}

	private static final List<String> PRIMITIVES = new ArrayList<>();
	static {
		PRIMITIVES.add("byte");
		PRIMITIVES.add("short");
		PRIMITIVES.add("int");
		PRIMITIVES.add("long");
		PRIMITIVES.add("double");
		PRIMITIVES.add("float");
		PRIMITIVES.add("Byte");
		PRIMITIVES.add("Short");
		PRIMITIVES.add("Integer");
		PRIMITIVES.add("Long");
		PRIMITIVES.add("Double");
		PRIMITIVES.add("Float");
		PRIMITIVES.add("String");
	}

	public boolean isPrimitive() {
		return PRIMITIVES.contains(simpleName);
	}

	public boolean isCollection() {
		return collection != null;
	}

	@Override
	public int compareTo(TypeDef other) {
		return this.qualifiedName.compareTo(other.qualifiedName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(qualifiedName);
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
		return Objects.equals(qualifiedName, other.qualifiedName);
	}
}
