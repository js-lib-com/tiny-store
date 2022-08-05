package com.jslib.tiny.store.util;

public class Types extends com.jslib.util.Types {
	public static boolean isPrimitiveLike(String type) {
		return com.jslib.util.Types.isPrimitiveLike(Classes.forOptionalName(type));
	}

	public static boolean isNumber(String type) {
		return com.jslib.util.Types.isNumber(Classes.forOptionalName(type));
	}
}
