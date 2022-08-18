package com.jslib.tiny.store.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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

import com.jslib.tiny.store.util.Strings;

import com.jslib.api.log.Log;
import com.jslib.api.log.LogFactory;

class MavenClientImpl implements IMavenClient {
	private static final Log log = LogFactory.getLog(MavenClientImpl.class);

	private final HttpClientBuilder httpClientBuilder;
	private final RequestConfig httpRequestConfig;

	public MavenClientImpl(Properties properties) {
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		RequestConfig.Builder httpRequestConfigBuilder = RequestConfig.custom();

		String proxyHost = properties.getProperty("proxy.host");
		if (proxyHost != null) {
			int proxyPort = Integer.parseInt(properties.getProperty("proxy.port"));
			HttpHost proxy = new HttpHost(proxyHost, proxyPort, properties.getProperty("proxy.protocol"));
			httpRequestConfigBuilder.setProxy(proxy);
		}
		this.httpRequestConfig = httpRequestConfigBuilder.build();

		String proxyUser = properties.getProperty("proxy.user");
		if (proxyUser != null) {
			int proxyPort = Integer.parseInt(properties.getProperty("proxy.port"));
			CredentialsProvider credentials = new BasicCredentialsProvider();
			credentials.setCredentials(new AuthScope(proxyHost, proxyPort), new UsernamePasswordCredentials(proxyUser, properties.getProperty("proxy.password")));
			httpClientBuilder = httpClientBuilder.setDefaultCredentialsProvider(credentials);
		}
		this.httpClientBuilder = httpClientBuilder;
	}

	@Override
	public void deploy(String serverURL, Coordinates coordinates, File archive) throws IOException {
		String url = Strings.concat(serverURL, coordinates.getPath(), '/', archive.getName());
		log.debug("Deploy archive to |{uri}|.", url);

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
