package es.rtve.specs;

import cucumber.api.java.es.Dado;
import es.bull.testingframework.specs.BaseSpec;
import es.bull.testingframework.specs.CommonSpec;

public class GivenSpec extends BaseSpec{

	public GivenSpec (CommonSpec spec) {
		this.commonspec = spec;
	}
	
	@Dado("^que cierro el navegador$")
	public void runApp(String url) {
		commonspec.getLogger().info("{}: Closing browser", commonspec.getShortBrowser());
		commonspec.getDriver().quit();
	}
}