package js.tiny.store.tool;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface 
public interface WorkUnit {
	void execute(Connection connection) throws SQLException;
}