package js.tiny.store.tool;

import java.util.HashMap;
import java.util.Map;

public class Classes extends js.util.Classes {
	@SuppressWarnings("unchecked")
	public static <T> Class<T> forOptionalName(String className) {
		Class<?> type = PRIMITIVES.get(className);
		return type != null ? (Class<T>) type : js.util.Classes.forOptionalName(className);
	}

	private static final Map<String, Class<?>> PRIMITIVES = new HashMap<String, Class<?>>();
	static {
		PRIMITIVES.put("int", Integer.TYPE);
		PRIMITIVES.put("long", Long.TYPE);
		PRIMITIVES.put("double", Double.TYPE);
		PRIMITIVES.put("float", Float.TYPE);
		PRIMITIVES.put("bool", Boolean.TYPE);
		PRIMITIVES.put("char", Character.TYPE);
		PRIMITIVES.put("byte", Byte.TYPE);
		PRIMITIVES.put("void", Void.TYPE);
		PRIMITIVES.put("short", Short.TYPE);
	}
}
