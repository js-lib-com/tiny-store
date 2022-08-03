package com.jslib.tiny.store.util;

public class Types extends js.util.Types {
	public static boolean isPrimitiveLike(String type) {
		return js.util.Types.isPrimitiveLike(Classes.forOptionalName(type));
	}

	public static boolean isNumber(String type) {
		return js.util.Types.isNumber(Classes.forOptionalName(type));
	}
}
