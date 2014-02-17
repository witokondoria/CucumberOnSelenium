package es.rtve.specs;

import static es.bull.testingframework.matchers.MatcherAssert.assertWarnThat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cucumber.api.Transform;
import cucumber.api.java.es.Entonces;
import es.bull.testingframework.cucumber.converter.ArrayListConverter;
import es.bull.testingframework.specs.BaseSpec;
import es.bull.testingframework.specs.CommonSpec;

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

	@Entonces("^existe al menos un elemento con \"(.*?)\" \"(.*?)\"$")
	public void assertContainerExists(String attrib, String value) {
		List<WebElement> elems = null;

		commonspec.getLogger().info("{}: Verifying container existance",
				commonspec.getShortBrowser());

		switch (attrib) {
		case "id":
			elems = commonspec.getDriver().findElements(By.id(value));
			break;
		case "name":
			elems = commonspec.getDriver().findElements(By.name(value));
			break;
		default:
			fail("Unimplemented locator method");
			break;
		}

		assertThat("No elements found with expected " + attrib + ": " + value,
				elems.size(), greaterThan(0));
		commonspec.setCurrentElements(elems);
	}

	@Entonces("^el primero de ellos no contiene elementos con clase \"(.*?)\"$")
	public void assertValidContent(
			@Transform(ArrayListConverter.class) ArrayList<String> invalidContent) {

		commonspec.getLogger().info("{}: Verifying valid content",
				commonspec.getShortBrowser());

		WebElement firstContent = commonspec.getCurrentElements().get(1);
		ArrayList<String> children = new ArrayList<String>();
		for (String element : invalidContent) {
			if (firstContent.findElements(By.className(element)).size() > 0) {
				children.add(element);
			}
		}

		assertThat("Invalid content at element", children,
				not(hasItems(invalidContent.toArray(new String[invalidContent
						.size()]))));
	}

	@Entonces("^si existe mas de uno, \"(.+?)\" habrá que generar un aviso$")
	public void warnOnMoreThanOneElement(String doWarn) {
		if (doWarn.toLowerCase().equals("si")) {
			commonspec.getLogger().info(
					"{}: Warn if theres more than one element",
					commonspec.getShortBrowser());
			assertWarnThat("More than one element found", commonspec
					.getCurrentElements().size(), equalTo(1));
		}
	}
}