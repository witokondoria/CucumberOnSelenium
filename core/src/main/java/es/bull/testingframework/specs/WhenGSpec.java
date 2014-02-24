package es.bull.testingframework.specs;

import cucumber.api.java.en.When;
import cucumber.api.java.es.Cuando;

public class WhenGSpec extends BaseSpec {

	public WhenGSpec(CommonSpec spec) {
		this.commonspec = spec;
	}

	@When("^maximizing the browser$")
	@Cuando("^maximizo el navegador$")
	public void maximize() {
		commonspec.getLogger().info("{}: Maximizing the browser window",
				commonspec.getShortBrowser());
		commonspec.getDriver().manage().window().maximize();
	}
}