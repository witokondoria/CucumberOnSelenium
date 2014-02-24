package es.bull.testingframework.specs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import cucumber.api.java.en.Then;
import cucumber.api.java.es.Entonces;

public class ThenGSpec extends BaseSpec {

	public ThenGSpec(CommonSpec spec) {
		this.commonspec = spec;
	}

	@Then("^load time has to be under \"(\\d+)\"$")
	@Entonces("^el tiempo en segundos transcurrido ha de ser menor a \"(\\d+)\"$")
	public void assertAccumLoadTime(int seconds) {
		commonspec.getLogger().info("{}: Verifying load time",
				commonspec.getShortBrowser());

		long start = commonspec.getStartTS();
		long end = System.currentTimeMillis();
		int load = (int) ((end - start) / 1000);

		assertThat("Page load time over defined threshold", load, lessThan(seconds));
	}

}