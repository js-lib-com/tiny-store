package js.tiny.store.meta;

public enum FieldFlag {
	/** Field is an entity primary key provided by application. */
	PROVIDED_KEY,
	/** Field is an entity primary key generated by persistence engine with AUTO strategy. */
	AUTO_KEY,
	/** Field is an entity primary key generated by persistence engine with IDENTITY strategy. */
	IDENTITY_KEY,
	/** Field is an entity primary key generated by persistence engine with SEQUENCE strategy. */
	SEQUENCE_KEY,
	/** Field is an entity primary key generated by persistence engine with TABLE strategy. */
	TABLE_KEY
}
