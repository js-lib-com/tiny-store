package js.tiny.store.tool;

import java.util.ArrayList;
import java.util.List;

import js.tiny.store.meta.EntityField;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.meta.TypeDef;

public class Strings extends js.util.Strings {
	public static String getPackageName(String qualifiedName) {
		int lastDotSeparatorPosition = qualifiedName.lastIndexOf('.');
		if (lastDotSeparatorPosition == -1) {
			throw new IllegalArgumentException("Not qualified class name: " + qualifiedName);
		}
		return qualifiedName.substring(0, lastDotSeparatorPosition);
	}

	public static String getSimpleName(String qualifiedName) {
		if (qualifiedName.endsWith(".")) {
			throw new IllegalArgumentException("Invalid class name: " + qualifiedName);
		}
		int lastDotSeparatorPosition = qualifiedName.lastIndexOf('.');
		if (lastDotSeparatorPosition == -1) {
			return qualifiedName;
		}
		return qualifiedName.substring(lastDotSeparatorPosition + 1);
	}

	public static String getParameterizedName(TypeDef type) {
		if (type.getCollection() == null) {
			return getSimpleName(type.getName());
		}
		return concat(getSimpleName(type.getCollection()), '<', getSimpleName(type.getName()), '>');
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

	public static String memberToColumnName(String memberName) {
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

	public static String columnToMemberName(String columnName) {
		if (columnName == null) {
			return null;
		}
		if (columnName.isEmpty()) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		boolean first = true;
		for (String word : split(columnName, '_', '-')) {
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
		return Strings.getSimpleName(entity.getClassName()).toLowerCase();
	}

	public static String columnName(EntityField field) {
		String alias = field.getAlias();
		if (alias != null) {
			return alias;
		}
		return Strings.memberToColumnName(field.getName());
	}
}
