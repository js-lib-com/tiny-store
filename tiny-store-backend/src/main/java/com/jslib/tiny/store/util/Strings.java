package com.jslib.tiny.store.util;

import java.util.ArrayList;
import java.util.List;

import com.jslib.tiny.store.meta.EntityField;
import com.jslib.tiny.store.meta.ServiceOperation;
import com.jslib.tiny.store.meta.StoreEntity;
import com.jslib.tiny.store.meta.TypeDef;

public class Strings extends js.util.Strings {
	public static String packageName(String typeName) {
		if (typeName == null) {
			return null;
		}
		int lastDotSeparatorPosition = typeName.lastIndexOf('.');
		if (lastDotSeparatorPosition == -1) {
			return null;
		}
		return typeName.substring(0, lastDotSeparatorPosition);
	}

	public static String qualifiedName(String packageName, String typeName) {
		return concat(packageName, '.', simpleName(typeName));
	}

	public static String simpleName(String typeName) {
		if (typeName.endsWith(".")) {
			throw new IllegalArgumentException("Invalid type name: " + typeName);
		}
		int lastDotSeparatorPosition = typeName.lastIndexOf('.');
		if (lastDotSeparatorPosition == -1) {
			return typeName;
		}
		return typeName.substring(lastDotSeparatorPosition + 1);
	}

	public static String simpleParameterizedName(TypeDef type) {
		if (type.getCollection() == null) {
			return simpleName(type.getName());
		}
		return concat(simpleName(type.getCollection()), '<', simpleName(type.getName()), '>');
	}

	public static String memberName(StoreEntity entity, EntityField field) {
		return concat(entity.getClassName(), '#', field.getName());
	}

	public static boolean replacePackage(TypeDef type, String oldPackage, String newPackage) {
		if (oldPackage.equals(packageName(type.getName()))) {
			type.setName(qualifiedName(newPackage, type.getName()));
			return true;
		}
		return false;
	}

	public static boolean replaceClass(TypeDef type, String oldQualifiedClass, String newClass) {
		if (oldQualifiedClass.equals(type.getName())) {
			type.setName(qualifiedName(packageName(oldQualifiedClass), newClass));
			return true;
		}
		return false;
	}

	public static boolean isDefaultPackage(String qualifiedName) {
		return qualifiedName.startsWith("java.lang.");
	}

	public static boolean isVoid(String name) {
		return "void".equalsIgnoreCase(name);
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

	public static boolean isPrimitive(String name) {
		return PRIMITIVES.contains(name);
	}

	public static String memberToDatabaseCase(String memberName) {
		if (memberName == null) {
			return null;
		}
		final int length = memberName.length();
		StringBuilder builder = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			char c = memberName.charAt(i);
			if (Character.isLowerCase(c)) {
				builder.append(c);
				continue;
			}

			c = Character.toLowerCase(c);
			if (i > 0) {
				builder.append('_');
			}
			builder.append(c);
		}
		return builder.toString();
	}

	public static String databaseToMemberName(String columnName) {
		if (columnName == null) {
			return null;
		}
		if (columnName.isEmpty()) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		boolean first = true;
		for (String word : split(columnName, '_')) {
			if (first) {
				first = false;
				sb.append(word);
				continue;
			}

			sb.append(Character.toUpperCase(word.charAt(0)));
			sb.append(word.substring(1).toLowerCase());
		}
		return sb.toString();
	}

	public static String tableName(StoreEntity entity) {
		String alias = entity.getAlias();
		if (alias != null) {
			return alias;
		}
		return simpleName(entity.getClassName());
	}

	public static String columnName(EntityField field) {
		String alias = field.getAlias();
		if (alias != null) {
			return alias;
		}
		return field.getName();
	}

	public static String columnName(String tableName, String columnName) {
		return concat(tableName, ':', columnName);
	}

	public static String operationName(ServiceOperation operation) {
		return concat(operation.getServiceClass(), '#', operation.getName());
	}

	public static int charCount(String string, char c) {
		int count = 0;
		for (int i = 0; i < string.length(); ++i) {
			if (string.charAt(i) == c) {
				++count;
			}
		}
		return count;
	}
}
