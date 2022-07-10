package js.tiny.store.tool;

import java.io.File;
import java.io.IOException;

public interface IGitClient {

	void commit(File projectDir, String message) throws IOException;

	void push(File projectDir, String username, String password) throws IOException;

}
