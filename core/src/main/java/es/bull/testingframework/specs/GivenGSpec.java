package es.bull.testingframework.specs;

import org.openqa.selenium.Cookie;

import cucumber.api.java.en.Given;
import cucumber.api.java.es.Dado;
import es.bull.testingframework.cucumber.annotation.Parameter;
import es.bull.testingframework.cucumber.annotation.Parameters;

public class GivenGSpec extends BaseSpec {

	public GivenGSpec(CommonSpec spec) {
		this.commonspec = spec;
	}

	@Parameters({
		@Parameter(name = Parameter.Name.COOKIE, type = Parameter.Type.STRING)})
	@Given("^I set a cookie '(.+?)', with value '(.+?)', for domain '(.+?)'$")
	@Dado("^que tengo la cookie '(.+?)', con valor '(.+?)', para el dominio '(.+?)'$")
	public void setCookie(String cookie, String value, String domain) {
		commonspec.getLogger().info("{}: Cookie set: {}",
				commonspec.getShortBrowser(), cookie);		
		commonspec.getDriver().get("http://www"+ domain + "/api/blank");
		commonspec.getDriver().manage().addCookie(new Cookie(cookie, value, domain, "/", null));
	}
	
	@Parameters({
		@Parameter(name = Parameter.Name.URL, type = Parameter.Type.STRING)})
	@Given("^I browse to '(.+?)'$")
	@Dado("^que navego a '(.+?)'$")
	public void browseTo(String url) {
		commonspec.getLogger().info("{}: Browsing to {}",
				commonspec.getShortBrowser(), url);
		if (!url.startsWith("http")) {
			commonspec.getLogger().info("{}: Appending transport protocol to url",
					commonspec.getShortBrowser(), url);
			url = "http://" + url;
		}
		commonspec.setStartTS(System.currentTimeMillis());
		commonspec.getDriver().get(url);
	}
	
	@Given("^I close the browser$")
	@Dado("^que cierro el navegador$")
	public void browserClose(String url) {
		commonspec.getLogger().info("{}: Closing browser", commonspec.getShortBrowser());
		commonspec.getDriver().quit();
	}
}