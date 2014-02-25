package es.rtve.specs;

import static es.bull.testingframework.matchers.MatcherAssert.assertWarnThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cucumber.api.java.es.Entonces;
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

		if (tabTitle.equals("Televisión")) {
			assertThat("Current tab " + tabTitle + " with bad color",
					tab.getCssValue("background-color"),
					not(equalTo("rgba(119, 119, 119, 1)")));
		} else {
			assertThat("Current tab " + tabTitle + " with bad color",
					tab.getCssValue("background"), containsString("color-stop"));
		}
	}

	@Entonces("^la etiqueta \"(.+?)\" debe tener un id \"(.*?)\", en minusculas, sin acentos$")
	public void assertIdOnTagLower(String tag, String id) {
		String unnacentedLowerId = Normalizer.normalize(id.toLowerCase(),
				Form.NFD).replaceAll("\\p{Block=CombiningDiacriticalMarks}+",
				"");
		assertIdOnTag(tag, unnacentedLowerId);
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

	@Entonces("^existe al menos un elemento con atributo \"(.*?)\" y valor \"(.*?)\"$")
	public void assertContainerExists(String attrib, String value) {

		commonspec.getLogger().info("{}: Verifying container existance",
				commonspec.getShortBrowser());

		List<WebElement> elems = commonspec.locateElements(attrib, value);

		assertThat("No elements found with expected " + attrib + ": " + value,
				elems.size(), greaterThan(0));

		commonspec.setCurrentElements(elems);
	}

	@Entonces("^ninguno está vacio$")
	public void warnOnEmptyElement() {
		commonspec.getLogger().info("{}: Verifying non-empty elements",
				commonspec.getShortBrowser());

		List<WebElement> elements = new ArrayList<WebElement>();

		for (WebElement element : commonspec.getCurrentElements()) {
			List<WebElement> markElements = element.getAttribute("class")
					.contains("mark") ? new ArrayList<WebElement>(
					Arrays.asList(element)) : element.findElements(By
					.className("mark"));
			assertWarnThat("Unexpected child element count with class mark",
					markElements.size(), greaterThan(0));
			elements.addAll(markElements);
		}
		commonspec.setCurrentElements(elements);
	}

	@Entonces("^contienen al menos un elemento con clase \"(.*?)\"$")
	public void assertExistentContent(String imperativeElement) {
		commonspec.getLogger().info(
				"{}: Verifying must-have content at element",
				commonspec.getShortBrowser());

		Integer foundElements = 0;
		for (WebElement element : commonspec.getCurrentElements()) {
			foundElements += element.findElements(
					By.className(imperativeElement)).size();

		}
		assertWarnThat("Bad number of elements found", foundElements,
				greaterThan(0));

	}

	// public void assertInexistentInvalidContent(
	// @Transform(ArrayListConverter.class) ArrayList<String> bannedClass) {

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