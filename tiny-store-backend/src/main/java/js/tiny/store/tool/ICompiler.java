package js.tiny.store.tool;

import java.io.File;
import java.io.IOException;

public interface ICompiler {

	void setVersion(Version version);

	String compile(File sourceDir, File classDir) throws IOException;

	String compile(File sourceDir, File classDir, File[] libraries) throws IOException;

	public static enum Version {
		JAVA_8("1.8");

		private String value;

		private Version(String value) {
			this.value = value;
		}

		public String value() {
			return value;
		}
	}
}
