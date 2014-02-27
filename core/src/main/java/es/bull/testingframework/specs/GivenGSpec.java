package es.bull.testingframework.specs;

import org.openqa.selenium.By;

import cucumber.api.java.en.Given;
import cucumber.api.java.es.Dado;

public class GivenGSpec extends BaseSpec {

	public GivenGSpec(CommonSpec spec) {
		this.commonspec = spec;
	}

	@Given("^I browse to \"(.+?)\"$")
	@Dado("^que navego a \"(.+?)\"$")
	public void browseTo(String url) {
		commonspec.getLogger().info("{}: Browsing to {}",
				commonspec.getShortBrowser(), url);
		if (!url.startsWith("https://") && !url.startsWith("http://")) {
			commonspec.getLogger().info("{}: Appending transport protocol to url",
					commonspec.getShortBrowser(), url);
			url = "http://" + url;
		}
		commonspec.setStartTS(System.currentTimeMillis());
		commonspec.getDriver().get(url);
	}
	
	@Given("^I dummy find$")	
	public void dummy(String url) {		
		commonspec.getDriver().findElement(By.id("dummy"));
	}
}