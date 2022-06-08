package js.tiny.store.tool;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
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

	@SuppressWarnings("unchecked")
	public static <T> Class<T> sqlType(int sqlType) {
		return (Class<T>) SQL_TYPES.get(sqlType);
	}

	private static final Map<Integer, Class<?>> SQL_TYPES = new HashMap<>();
	static {
		SQL_TYPES.put(Types.BIGINT, long.class);
		SQL_TYPES.put(Types.BIT, byte.class);
		SQL_TYPES.put(Types.BLOB, byte[].class);
		SQL_TYPES.put(Types.BOOLEAN, boolean.class);
		SQL_TYPES.put(Types.CHAR, String.class);
		SQL_TYPES.put(Types.CLOB, byte[].class);
		SQL_TYPES.put(Types.DATE, Date.class);
		SQL_TYPES.put(Types.DECIMAL, BigDecimal.class);
		SQL_TYPES.put(Types.DOUBLE, double.class);
		SQL_TYPES.put(Types.FLOAT, float.class);
		SQL_TYPES.put(Types.INTEGER, int.class);
		SQL_TYPES.put(Types.LONGNVARCHAR, String.class);
		SQL_TYPES.put(Types.LONGVARBINARY, byte[].class);
		SQL_TYPES.put(Types.LONGVARCHAR, String.class);
		SQL_TYPES.put(Types.NCHAR, String.class);
		SQL_TYPES.put(Types.NUMERIC, BigDecimal.class);
		SQL_TYPES.put(Types.NVARCHAR, String.class);
		SQL_TYPES.put(Types.REAL, double.class);
		SQL_TYPES.put(Types.SMALLINT, short.class);
		SQL_TYPES.put(Types.TIME, Timestamp.class);
		SQL_TYPES.put(Types.TIMESTAMP, Timestamp.class);
		SQL_TYPES.put(Types.TINYINT, byte.class);
		SQL_TYPES.put(Types.VARBINARY, byte[].class);
		SQL_TYPES.put(Types.VARCHAR, String.class);
	}
}
