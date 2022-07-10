package js.tiny.store.util;

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
		SQL_TYPES.put(Types.BIGINT, Long.class);
		SQL_TYPES.put(Types.BIT, Byte.class);
		SQL_TYPES.put(Types.BLOB, Byte[].class);
		SQL_TYPES.put(Types.BOOLEAN, Boolean.class);
		SQL_TYPES.put(Types.CHAR, String.class);
		SQL_TYPES.put(Types.CLOB, Byte[].class);
		SQL_TYPES.put(Types.DATE, Date.class);
		SQL_TYPES.put(Types.DECIMAL, BigDecimal.class);
		SQL_TYPES.put(Types.DOUBLE, Double.class);
		SQL_TYPES.put(Types.FLOAT, Float.class);
		SQL_TYPES.put(Types.INTEGER, Integer.class);
		SQL_TYPES.put(Types.LONGNVARCHAR, String.class);
		SQL_TYPES.put(Types.LONGVARBINARY, Byte[].class);
		SQL_TYPES.put(Types.LONGVARCHAR, String.class);
		SQL_TYPES.put(Types.NCHAR, String.class);
		SQL_TYPES.put(Types.NUMERIC, BigDecimal.class);
		SQL_TYPES.put(Types.NVARCHAR, String.class);
		SQL_TYPES.put(Types.REAL, Double.class);
		SQL_TYPES.put(Types.SMALLINT, Short.class);
		SQL_TYPES.put(Types.TIME, Timestamp.class);
		SQL_TYPES.put(Types.TIMESTAMP, Timestamp.class);
		SQL_TYPES.put(Types.TINYINT, Byte.class);
		SQL_TYPES.put(Types.VARBINARY, Byte[].class);
		SQL_TYPES.put(Types.VARCHAR, String.class);
	}
}
