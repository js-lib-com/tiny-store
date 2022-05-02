package js.tiny.store.tool;

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
		if(type.getCollection() == null) {
			return getSimpleName(type.getName());
		}
		return concat(getSimpleName(type.getCollection()), '<', getSimpleName(type.getName()), '>');
	}
}
