package es.rtve.specs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cucumber.api.java.es.Entonces;
import es.bull.framework.specs.BaseSpec;
import es.bull.framework.specs.CommonSpec;

public class ThenSpec extends BaseSpec {

	public ThenSpec(CommonSpec spec) {
		this.commonspec = spec;
	}

	@Entonces("^la pestaña \"(.*?)\" debe tener un color distinto de gris$")
	public void assertTabNotGrey(String tabTitle) {

		WebElement tab = commonspec.getDriver().findElement(
				By.xpath("//li/a[@title='" + tabTitle + "']"));
		commonspec.getLogger().info("{}: Verifying tab {} isn't greyed",
				commonspec.getShortBrowser(), tabTitle);

		switch (tabTitle) {
		case "Televisión":
			assertThat("Current tab " + tabTitle + " with bad color",
					tab.getCssValue("background-color"),
					not(equalTo("rgba(119, 119, 119, 1)")));
			break;
		default:
			assertThat("Current tab " + tabTitle + " with bad color",
					tab.getCssValue("background"), containsString("color-stop"));
			break;
		}
	}

	@Entonces("^la etiqueta \"(.+?)\" debe tener un id \"(.*?)\", en minusculas$")
	public void assertIdOnTagLower(String tag, String id) {
		assertIdOnTag(tag, id.toLowerCase());
	}

	@Entonces("^la etiqueta \"(.+?)\" debe tener un id \"(.*?)\"$")
	public void assertIdOnTag(String tag, String id) {
		commonspec.getLogger().info("{}: Verifying expected id for {} tag",
				commonspec.getShortBrowser(), tag);

		if (id.contains(" ")) {
			id = id.substring(id.indexOf(" "), id.length());
		}

		assertThat(tag + " tag with bad id", commonspec.getDriver()
				.findElement(By.tagName(tag)).getAttribute("id"), equalTo(id));
	}
}