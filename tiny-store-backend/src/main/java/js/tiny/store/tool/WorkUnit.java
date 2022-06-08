package js.tiny.store.tool;

import java.sql.Connection;

@FunctionalInterface 
public interface WorkUnit {
	void execute(Connection connection) throws Exception;
}