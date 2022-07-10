package js.tiny.store.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import js.log.Log;
import js.log.LogFactory;
import js.tiny.store.Context;
import js.tiny.store.util.Strings;

class MavenClientImpl implements IMavenClient {
	private static final Log log = LogFactory.getLog(MavenClientImpl.class);

	private final HttpClientBuilder httpClientBuilder;
	private final RequestConfig httpRequestConfig;

	public MavenClientImpl(Context context) {
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		RequestConfig.Builder httpRequestConfigBuilder = RequestConfig.custom();
		if (context.hasProxy()) {
			HttpHost proxy = new HttpHost(context.getProxyHost(), context.getProxyPort(), context.getProxyProtocol());
			httpRequestConfigBuilder.setProxy(proxy);
		}
		this.httpRequestConfig = httpRequestConfigBuilder.build();

		if (context.isProxySecure()) {
			CredentialsProvider credentials = new BasicCredentialsProvider();
			credentials.setCredentials(new AuthScope(context.getProxyHost(), context.getProxyPort()), new UsernamePasswordCredentials(context.getProxyUser(), context.getProxyPassword()));
			httpClientBuilder = httpClientBuilder.setDefaultCredentialsProvider(credentials);
		}
		this.httpClientBuilder = httpClientBuilder;
	}

	@Override
	public void deploy(String serverURL, Coordinates coordinates, File archive) throws IOException {
		String url = Strings.concat(serverURL, coordinates.getPath(), '/', archive.getName());
		log.debug("Deploy archive to |%s|.", url);

		try (CloseableHttpClient client = httpClientBuilder.build()) {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(httpRequestConfig);
			httpPost.setHeader("Content-Type", "application/octet-stream");
			httpPost.setEntity(new InputStreamEntity(new FileInputStream(archive)));

			try (CloseableHttpResponse response = client.execute(httpPost)) {
				if (response.getStatusLine().getStatusCode() != 200) {
					throw new IOException(String.format("Fail to upload file %s", archive));
				}
			}
		}
	}
}
