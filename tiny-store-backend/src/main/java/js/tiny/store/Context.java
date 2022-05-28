package js.tiny.store;

import java.io.File;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Context {
	@Resource(name = "workspace.dir")
	private String workspaceDirPath;
	@Resource(name = "runtime.dir")
	private String runtimeDirPath;

	@Resource(name = "mongo.url")
	private String mongoURL;
	@Resource(name = "mongo.db")
	private String mongoDatabase;

	@Resource(name = "proxy.protocol")
	private String proxyProtocol;
	@Resource(name = "proxy.host")
	private String proxyHost;
	@Resource(name = "proxy.port")
	private int proxyPort;
	@Resource(name = "proxy.user")
	private String proxyUser;
	@Resource(name = "proxy.password")
	private String proxyPassword;

	private File workspaceDir;
	private File runtimeDir;

	public File getWorkspaceDir() {
		if (workspaceDir == null) {
			workspaceDir = new File(workspaceDirPath);
		}
		return workspaceDir;
	}

	public File getRuntimeDir() {
		if (runtimeDir == null) {
			runtimeDir = new File(runtimeDirPath);
		}
		return runtimeDir;
	}

	public String getMongoURL() {
		return mongoURL;
	}

	public String getMongoDatabase() {
		return mongoDatabase;
	}

	public boolean hasProxy() {
		return proxyHost != null;
	}

	public boolean isProxySecure() {
		return proxyUser != null;
	}

	public String getProxyProtocol() {
		return proxyProtocol;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public String getProxyUser() {
		return proxyUser;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}
}
