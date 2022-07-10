package js.tiny.store.tool;

import java.io.File;
import java.io.IOException;

import js.tiny.store.meta.Version;
import js.util.Strings;

public interface IMavenClient {

	void deploy(String serverURL, Coordinates coordinates, File archive) throws IOException;

	static class Coordinates {
		private final String groupId;
		private final String artifactId;
		private final Version version;

		public Coordinates(String groupId, String artifactId, Version version) {
			this.groupId = groupId;
			this.artifactId = artifactId;
			this.version = version;
		}

		public String getGroupId() {
			return groupId;
		}

		public String getArtifactId() {
			return artifactId;
		}

		public Version getVersion() {
			return version;
		}

		public String getPath() {
			return Strings.concat(groupId.replace('.', '/'), '/', artifactId, '/', version);
		}
	}
}
