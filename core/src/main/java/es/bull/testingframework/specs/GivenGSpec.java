package es.bull.testingframework.specs;

import org.openqa.selenium.By;

import cucumber.api.java.en.Given;
import cucumber.api.java.es.Dado;
import es.bull.testingframework.specs.CommonSpec;

public class GivenGSpec {

	private CommonSpec commonspec;

	public GivenGSpec(CommonSpec spec) {
		this.commonspec = spec;
	}

	@Given("^I browse to \"(.+?)\"$")
	@Dado("^que navego a \"(.+?)\"$")
	public void browseTo(String url) {
		commonspec.getLogger().info("{}: Browsing to {}",
				commonspec.getShortBrowser(), url);
		commonspec.getDriver().get(url);
	}
	
	@Given("^I dummy find$")	
	public void dummy(String url) {		
		commonspec.getDriver().findElement(By.id("dummy"));
	}
}