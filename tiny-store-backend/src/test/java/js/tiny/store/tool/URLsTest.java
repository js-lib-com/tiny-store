package js.tiny.store.tool;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import com.jslib.tiny.store.util.URLs;

public class URLsTest {
	@Test
	public void GivenGitUrlWithUserName_WhenGetHost_ThenHost() {
		// given
		String url = "https://irotaru:mami1964@git.gnotis.ro:8443/xp/gas-detection-db.git";
		
		// when
		String host = URLs.host(url);
		
		System.out.println(URLs.scheme(url));
		System.out.println(URLs.host(url));
		System.out.println(URLs.hostURL(url));
		System.out.println(URLs.port(url));
		System.out.println(URLs.userinfo(url));
		System.out.println(URLs.user(url));
		System.out.println(URLs.password(url));
		System.out.println(URLs.authority(url));
		System.out.println(URLs.path(url));
		
		// then
		assertThat(host, notNullValue());
		assertThat(host, equalTo("git.gnotis.ro"));
	}
	
	@Test
	public void GivenUrlWithoutPath_WhenGetHost_ThenNotNull() {
		// given
		String url = "https://git.gnotis.ro";
		
		// when
		String host = URLs.host(url);
		
		// then
		assertThat(host, notNullValue());
		assertThat(host, equalTo("git.gnotis.ro"));
	}
	
	@Test
	public void GivenUrlWithPortAndWithoutPath_WhenGetHost_ThenNotNull() {
		// given
		String url = "https://git.gnotis.ro:8443";
		
		// when
		String host = URLs.host(url);
		
		// then
		assertThat(host, notNullValue());
		assertThat(host, equalTo("git.gnotis.ro"));
	}
	
	@Test
	public void GivenUrlWithPortAndWithoutPath_WhenGetPort_ThenNotNull() {
		// given
		String url = "https://git.gnotis.ro:8443";
		
		// when
		String port = URLs.port(url);
		
		// then
		assertThat(port, notNullValue());
		assertThat(port, equalTo("8443"));
	}
}
