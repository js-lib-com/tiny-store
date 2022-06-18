package js.tiny.store.tool;

public class Types extends js.util.Types {
	public static boolean isPrimitiveLike(String type) {
		return js.util.Types.isPrimitiveLike(Classes.forOptionalName(type));
	}

	public static boolean isNumber(String type) {
		return js.util.Types.isNumber(Classes.forOptionalName(type));
	}
}
