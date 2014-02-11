package es.rtve.specs;

import cucumber.api.java.es.Dado;
import es.bull.framework.specs.CommonSpec;

public class GivenSpec{

	private CommonSpec commonspec;

	public GivenSpec (CommonSpec spec) {
		this.commonspec = spec;
	}
	
	@Dado("^que navego a \"(.+?)\"$")
	public void browseTo(String url) {
		commonspec.getLogger().info("{}: Browsing to {}", commonspec.getShortBrowser(), url);
		commonspec.getDriver().get(url);
	}
}