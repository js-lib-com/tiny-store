package js.tiny.store.tool;

import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLs {
	private static final String SCHEME = "(?:(?<scheme>https?|ftp)\\:)";

	private static final String USERINFO = "(?:(?<userinfo>(?<user>[^/:@]+)(?:\\:(?<password>[^/@]+))?)@)?";
	private static final String HOST = "(?<host>[^/:]+)";
	private static final String PORT = "(?:\\:(?<port>\\d{1,5}))?";
	private static final String AUTHORITY = "(?<authority>//" + USERINFO + HOST + PORT + ")?";

	private static final String PATH = "(?<path>/[^?]*)?";
	private static final String QUERY = "(?:\\?(?<query>[^#]+))?";
	private static final String FRAGMENT = "(?:#(?<fragment>.+))?";

	private static final Pattern URL = Pattern.compile(SCHEME + AUTHORITY + PATH + QUERY + FRAGMENT);

	public static boolean isValid(String url) {
		return URL.matcher(url).find();
	}

	public static String scheme(String url) {
		return match(url, "scheme");
	}

	public static String userinfo(String url) {
		return match(url, "userinfo");
	}

	public static String user(String url) {
		return match(url, "user");
	}

	public static String password(String url) {
		return match(url, "password");
	}

	public static String host(String url) {
		return match(url, "host");
	}

	public static String hostURL(String url) {
		Matcher matcher = URL.matcher(url);
		if (!matcher.find()) {
			return null;
		}
		return Strings.concat(matcher.group("scheme"), "://", matcher.group("host"), '/');
	}

	public static String port(String url) {
		return match(url, "port");
	}

	public static String authority(String url) {
		return match(url, "authority");
	}

	public static String path(String url) {
		return match(url, "path");
	}

	public static String query(String url) {
		return match(url, "query");
	}

	public static String fragment(String url) {
		return match(url, "fragment");
	}

	public static String match(URI uri, String componentName) {
		return match(uri.toASCIIString(), componentName);
	}

	public static String match(URL url, String componentName) {
		return match(url.toExternalForm(), componentName);
	}

	public static String match(String url, String componentName) {
		Matcher matcher = URL.matcher(url);
		return matcher.find() ? matcher.group(componentName) : null;
	}
}
