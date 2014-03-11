package es.rtve.specs;

import es.bull.testingframework.specs.BaseSpec;
import es.bull.testingframework.specs.CommonSpec;

public class WhenSpec extends BaseSpec {

	public WhenSpec(CommonSpec spec) {
		this.commonspec = spec;
	}

//	@Cuando("^hago click en la pestaña \"(.+?)\"$")
//	public void clickOnTab(String tabTitle) {
//		commonspec.getLogger().info("{}: Clicking on tab {}",
//				commonspec.getShortBrowser(), tabTitle);
//		String expression = "//li/a[@title = '" + tabTitle + "']";
//		WebElement elem = commonspec.getDriver().findElement(
//				By.xpath(expression));
//		elem.click();
//	}		
}