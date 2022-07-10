package js.tiny.store;

import java.io.File;
import java.util.Properties;

import jakarta.annotation.PostConstruct;
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
	private String mongoDatabaseName;

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
	private Properties properties;

	@PostConstruct
	private void postConstruct() {
		this.workspaceDir = new File(workspaceDirPath);
		this.runtimeDir = new File(runtimeDirPath);

		this.properties = new Properties();
		setProperty("workspace.dir", workspaceDir);
		setProperty("runtime.dir", runtimeDir);
		setProperty("mongo.url", mongoURL);
		setProperty("mongo.db", mongoDatabaseName);
		setProperty("proxy.protocol", proxyProtocol);
		setProperty("proxy.host", proxyHost);
		setProperty("proxy.port", proxyPort);
		setProperty("proxy.User", proxyUser);
		setProperty("proxy.Password", proxyPassword);
	}

	private void setProperty(String key, Object value) {
		if (value != null) {
			properties.put(key, value);
		}
	}

	public File getWorkspaceDir() {
		return workspaceDir;
	}

	public File getRuntimeDir() {
		return runtimeDir;
	}

	public String getMongoURL() {
		return mongoURL;
	}

	public String getMongoDatabaseName() {
		return mongoDatabaseName;
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

	public Properties getProperties() {
		return properties;
	}
}
