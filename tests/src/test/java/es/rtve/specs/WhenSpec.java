package es.rtve.specs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cucumber.api.java.es.Cuando;
import es.bull.testingframework.specs.BaseSpec;
import es.bull.testingframework.specs.CommonSpec;

public class WhenSpec extends BaseSpec {

	public WhenSpec(CommonSpec spec) {
		this.commonspec = spec;
	}

	@Cuando("^maximizo el navegador$")
	public void maximize() {
		commonspec.getLogger().info("{}: Maximizing the browser window",
				commonspec.getShortBrowser());
		commonspec.getDriver().manage().window().maximize();
	}

	@Cuando("^hago click en la pestaña \"(.+?)\"$")
	public void clickOnTab(String tabTitle) {
		commonspec.getLogger().info("{}: Clicking on tab {}",
				commonspec.getShortBrowser(), tabTitle);
		String expression = "//li/a[@title = '" + tabTitle + "']";
		WebElement elem = commonspec.getDriver().findElement(
				By.xpath(expression));
		elem.click();
	}
}