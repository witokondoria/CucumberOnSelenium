package es.bull.testingframework.specs;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;

import cucumber.api.java.en.When;
import cucumber.api.java.es.Cuando;

public class WhenGSpec extends BaseSpec {

	public WhenGSpec(CommonSpec spec) {
		this.commonspec = spec;
	}

	@When("^I maximize the browser$")
	@Cuando("^maximizo el navegador$")
	public void maximize() {
		commonspec.getLogger().info("{}: Maximizing the browser window",
				commonspec.getShortBrowser());
		commonspec.getDriver().manage().window().maximize();
	}

	@When("^I move the mouse over th(is|ese) elements?$")
	@Cuando("^muevo el ratón a es(e|os) elementos?$")
	public void mouseMoveToElements(String target) throws InterruptedException {
		commonspec.getLogger().info("{}: Moving the mouse",
				commonspec.getShortBrowser());

		List<WebElement> elems = commonspec.getCurrentElements();
		Actions builder = new Actions(commonspec.getDriver());

		if (target.equals("e") || target.equals("is")) {
			WebElement elem = commonspec.getCurrentElements().get(0);
			Action mouseMove = builder.moveToElement(elem).build();
			mouseMove.perform();
			Thread.sleep(2000);
		} else if (target.equals("os") || target.equals("ese")) {
			for (WebElement elem : elems) {
				Action mouseMove = builder.moveToElement(elem).build();
				mouseMove.perform();
				Thread.sleep(2000);
			}
		}
	}

	@When("^I type '(.*?)' at this element$")
	@Cuando("^escribo el texto '(.*?)' en ese elemento$")
	public void typeToElement(String keys) {
		commonspec.getLogger().info(
				"{}: Typing to the first previous found element",
				commonspec.getShortBrowser());

		WebElement elem = commonspec.getCurrentElements().get(0);
		elem.sendKeys(keys);
	}

	@When("^I click on this element$")
	@Cuando("^hago click en ese elemento$")
	public void clickElement() {
		commonspec.getLogger().info(
				"{}: Clicking on the first previous found element",
				commonspec.getShortBrowser());

		WebElement elem = commonspec.getCurrentElements().get(0);
		elem.click();
	}

	@When("^I wipe the text at this element$")
	@Cuando("^elimino el texto de ese elemento$")
	public void clearElement() {
		commonspec.getLogger().info(
				"{}: Clearing the first previous found element",
				commonspec.getShortBrowser());

		WebElement elem = commonspec.getCurrentElements().get(0);
		elem.clear();
	}

	@When("^I scroll to this element$")
	@Cuando("^hago scroll a ese elemento$")
	public void scrollElement() throws InterruptedException {
		commonspec.getLogger().info("{}: Scrolling",
				commonspec.getShortBrowser());

		WebElement elem = commonspec.getCurrentElements().get(0);
		((Locatable) elem).getCoordinates().inViewPort();
		Thread.sleep(2000);
		((Locatable) elem).getCoordinates().inViewPort();

	}

	@When("^I wait '(\\d+)' seconds?$")
	@Cuando("^espero '(\\d+)' segundos?$")
	public void explicitWait(Integer time) throws InterruptedException {
		commonspec.getLogger().info("{}: Idle waiting",
				commonspec.getShortBrowser());

		Thread.sleep(time * 1000);
	}
}