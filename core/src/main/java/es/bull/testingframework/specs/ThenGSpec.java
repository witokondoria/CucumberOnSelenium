package es.bull.testingframework.specs;

import static es.bull.testingframework.matchers.MatcherAssert.assertWarnThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;

import cucumber.api.Transform;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.es.Entonces;
import cucumber.api.java.es.Y;
import es.bull.testingframework.cucumber.converter.WarnBooleanConverter;
import es.bull.testingframework.cucumber.annotation.Parameter;
import es.bull.testingframework.cucumber.annotation.Parameters;

public class ThenGSpec extends BaseSpec {

	public ThenGSpec(CommonSpec spec) {
		this.commonspec = spec;
	}

	@Parameters({
			@Parameter(name = Parameter.Name.SECONDS, type = Parameter.Type.INT),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@Then("^load time has to be under '(\\d+)' seconds( \\(WARN otherwise\\))?$")
	@Entonces("^el tiempo transcurrido ha de ser menor a '(\\d+)' segundos( \\(WARN en otro caso\\))?$")
	public void assertElapsedLoadTime(Integer seconds,
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info("{}: Verifying load time",
				commonspec.getShortBrowser());
		long start = commonspec.getStartTS();
		long end = System.currentTimeMillis();
		int load = (int) ((end - start) / 1000);

		if (warn) {
			assertWarnThat("Page load time over defined threshold", load,
					lessThanOrEqualTo(seconds));
		} else {
			assertThat("Page load time over defined threshold", load,
					lessThanOrEqualTo(seconds));
		}
	}

	@Parameters({
			@Parameter(name = Parameter.Name.NUMBER, type = Parameter.Type.INT),
			@Parameter(name = Parameter.Name.ATTRIBUTE, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.TEXT, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@Then("^there (?:is|are) '(\\d)' elements?, with attribute '(.*?)' and value '(.*?)'( \\(WARN otherwise\\))?$")
	@Entonces("^existen? '(\\d+)' elementos?, con atributo '(.*?)' y valor '(.*?)'( \\(WARN en otro caso\\))?$")
	public void assertElementsExists(Integer expectedCount, String attrib,
			String value, @Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info("{}: Verifying element(s) existance",
				commonspec.getShortBrowser());
		List<WebElement> elems = commonspec.locateElements(attrib, value);

		if (warn) {
			assertWarnThat("Unexpected number of elements found", elems.size(),
					equalTo(expectedCount));
		} else {
			assertThat("Unexpected number of elements found", elems.size(),
					equalTo(expectedCount));
		}
		commonspec.setCurrentElements(elems);
	}

	@Parameters({
			@Parameter(name = Parameter.Name.NUMBER, type = Parameter.Type.INT),
			@Parameter(name = Parameter.Name.ATTRIBUTE, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.TEXT, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@Then("^there (?:is|are)n't '(\\d)' elements?, with attribute '(.*?)' and value '(.*?)'( \\(WARN otherwise\\))?$")
	@Entonces("^no existen? '(\\d+)' elementos?, con atributo '(.*?)' y valor '(.*?)'( \\(WARN en otro caso\\))?$")
	public void assertElementsDoesntExists(Integer expectedCount,
			String attrib, String value,
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info("{}: Verifying element(s) existance",
				commonspec.getShortBrowser());
		List<WebElement> elems = commonspec.locateElements(attrib, value);

		if (warn) {
			assertWarnThat("Unexpected number of elements found", elems.size(),
					not(equalTo(expectedCount)));
		} else {
			assertThat("Unexpected number of elements found", elems.size(),
					not(equalTo(expectedCount)));
		}
	}

	@Parameters({
			@Parameter(name = Parameter.Name.NUMBER, type = Parameter.Type.INT),
			@Parameter(name = Parameter.Name.ATTRIBUTE, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.TEXT, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@Then("^there (?:is|are) more than '(\\d)' elements?, with attribute '(.*?)' and value '(.*?)'( \\(WARN otherwise\\))?$")
	@Entonces("^existen? mas de '(\\d+)' elementos?, con atributo '(.*?)' y valor '(.*?)'( \\(WARN en otro caso\\))?$")
	public void assertMoreThanNElementsExists(Integer expectedCount,
			String attrib, String value,
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info("{}: Verifying element(s) existance",
				commonspec.getShortBrowser());
		List<WebElement> elems = commonspec.locateElements(attrib, value);
		if (warn) {
			assertWarnThat("Unexpected number of elements found", elems.size(),
					greaterThan(expectedCount));
		} else {
			assertThat("Unexpected number of elements found", elems.size(),
					greaterThan(expectedCount));
		}
		commonspec.setCurrentElements(elems);
	}

	@Parameters({
			@Parameter(name = Parameter.Name.NUMBER, type = Parameter.Type.INT),
			@Parameter(name = Parameter.Name.ATTRIBUTE, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.TEXT, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@Then("^there (?:is|are) less than '(\\d)' elements?, with attribute '(.*?)' and value '(.*?)'( \\(WARN otherwise\\))?$")
	@Entonces("^existen? menos de '(\\d+)' elementos?, con atributo '(.*?)' y valor '(.*?)'( \\(WARN en otro caso\\))?$")
	public void assertLessThanNElementsExists(Integer expectedCount,
			String attrib, String value,
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info("{}: Verifying element(s) existance",
				commonspec.getShortBrowser());
		List<WebElement> elems = commonspec.locateElements(attrib, value);

		if (warn) {
			assertWarnThat("Unexpected number of elements found", elems.size(),
					lessThan(expectedCount));
		} else {
			assertThat("Unexpected number of elements found", elems.size(),
					lessThan(expectedCount));
		}
		commonspec.setCurrentElements(elems);
	}

	@Parameters({
			@Parameter(name = Parameter.Name.NUMBER, type = Parameter.Type.INT),
			@Parameter(name = Parameter.Name.ATTRIBUTE, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.TEXT, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@Then("^there (?:is|are) '(\\d)' or more elements, with attribute '(.*?)' and value '(.*?)'( \\(WARN otherwise\\))?$")
	@Entonces("^existen? '(\\d+)' elementos? o mas, con atributo '(.*?)' y valor '(.*?)'( \\(WARN en otro caso\\))?$")
	public void assertMoreThanEqualNElementsExists(Integer expectedCount,
			String attrib, String value,
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info("{}: Verifying element(s) existance",
				commonspec.getShortBrowser());
		List<WebElement> elems = commonspec.locateElements(attrib, value);

		if (warn) {
			assertWarnThat("Unexpected number of elements found", elems.size(),
					greaterThanOrEqualTo(expectedCount));
		} else {
			assertThat("Unexpected number of elements found", elems.size(),
					greaterThanOrEqualTo(expectedCount));
		}
		commonspec.setCurrentElements(elems);
	}

	@Parameters({
			@Parameter(name = Parameter.Name.NUMBER, type = Parameter.Type.INT),
			@Parameter(name = Parameter.Name.ATTRIBUTE, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.TEXT, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@Then("^there (?:is|are) '(\\d+)' or less elements?, with attribute '(.*?)' and value '(.*?)'( \\(WARN otherwise\\))?$")
	@Entonces("^existen? '(\\d+)' elementos? o menos, con atributo '(.*?)' y valor '(.*?)'( \\(WARN en otro caso\\))?$")
	public void assertLessThanEqualNElementsExists(Integer expectedCount,
			String attrib, String value,
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info("{}: Verifying element(s) existance",
				commonspec.getShortBrowser());
		List<WebElement> elems = commonspec.locateElements(attrib, value);

		if (warn) {
			assertWarnThat("Unexpected number of elements found", elems.size(),
					lessThanOrEqualTo(expectedCount));
		} else {
			assertThat("Unexpected number of elements found", elems.size(),
					lessThanOrEqualTo(expectedCount));
		}
		commonspec.setCurrentElements(elems);
	}

	// TODO: pluralize
	@Parameters({
			@Parameter(name = Parameter.Name.NUMBER, type = Parameter.Type.INT),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@And("^has '(\\d+)' child(?:ren)? elements?( \\(WARN otherwise\\))?$")
	@Y("^tiene '(\\d+)' elementos? hijos?( \\(WARN en otro caso\\))?$")
	public void assertHasNChildren(Integer expectedChildren,
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info("{}: Verifying children count",
				commonspec.getShortBrowser());
		List<WebElement> elems = commonspec.locateElements(commonspec
				.getCurrentElements().get(0), "*");

		if (warn) {
			assertWarnThat("Unexpected number of children found", elems.size(),
					equalTo(expectedChildren));
		} else {
			assertThat("Unexpected number of children found", elems.size(),
					equalTo(expectedChildren));
		}
	}

	@Parameters({
			@Parameter(name = Parameter.Name.NUMBER, type = Parameter.Type.INT),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@And("^hasn't '(\\d+)' child(?:ren)? elements?( \\(WARN otherwise\\))?$")
	@Y("^no tiene '(\\d+)' elementos? hijos?( \\(WARN en otro caso\\))?$")
	public void assertNotHasNChildren(Integer expectedChildren,
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info("{}: Verifying children count",
				commonspec.getShortBrowser());
		List<WebElement> elems = commonspec.locateElements(commonspec
				.getCurrentElements().get(0), "*");

		if (warn) {
			assertWarnThat("Unexpected number of children found", elems.size(),
					not(equalTo(expectedChildren)));
		} else {
			assertThat("Unexpected number of children found", elems.size(),
					not(equalTo(expectedChildren)));
		}
	}

	@Parameters({
			@Parameter(name = Parameter.Name.NUMBER, type = Parameter.Type.INT),
			@Parameter(name = Parameter.Name.HTMLTAG, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.ATTRIBUTE, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.TEXT, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@And("^has '(\\d+)' child(?:ren)? elements?, with tag '(.*?)', attribute '(.*?)' and value '(.*?)'( \\(WARN otherwise\\))?$")
	@Y("^tienen? '(\\d+)' elementos? hijos?, con tag '(.*?)', atributo '(.*?)' y valor '(.*?)'( \\(WARN en otro caso\\))?$")
	public void assertHasNChildrenWithDetails(Integer expectedChildren,
			String tag, String attribute, String value,
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info("{}: Verifying children count",
				commonspec.getShortBrowser());
		for (WebElement elem : commonspec.getCurrentElements()) {
			List<WebElement> elems = commonspec.locateElements(elem, tag,
					attribute, value);
			if (warn) {
				assertWarnThat("Unexpected number of children found",
						elems.size(), equalTo(expectedChildren));
			} else {
				assertThat("Unexpected number of children found", elems.size(),
						equalTo(expectedChildren));
			}
		}
	}

	@Parameters({
			@Parameter(name = Parameter.Name.NUMBER, type = Parameter.Type.INT),
			@Parameter(name = Parameter.Name.HTMLTAG, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.ATTRIBUTE, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.TEXT, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@And("^hasn't '(\\d+)' child(?:ren)? elements?, with tag '(.*?)', attribute '(.*?)' and value '(.*?)'( \\(WARN otherwise\\))?$")
	@Y("^no tienen? '(\\d+)' elementos? hijos?, con tag '(.*?)', atributo '(.*?)' y valor '(.*?)'( \\(WARN en otro caso\\))?$")
	public void assertNotHasNChildrenWithDetails(Integer expectedChildren,
			String tag, String attribute, String value,
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info("{}: Verifying children count",
				commonspec.getShortBrowser());
		for (WebElement elem : commonspec.getCurrentElements()) {
			List<WebElement> elems = commonspec.locateElements(elem, tag,
					attribute, value);
			if (warn) {
				assertWarnThat("Unexpected number of children found",
						elems.size(), not(equalTo(expectedChildren)));
			} else {
				assertThat("Unexpected number of children found", elems.size(),
						not(equalTo(expectedChildren)));
			}
		}
	}

	@Parameters({
			@Parameter(name = Parameter.Name.NUMBER, type = Parameter.Type.INT),
			@Parameter(name = Parameter.Name.HTMLTAG, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.ATTRIBUTE, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.TEXT, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@And("^'(\\d+)' child(?:ren)? elements?, with tag '(.*?)', attribute '(.*?)' and value '(.*?)'( \\(WARN otherwise\\))?$")
	@Y("^'(\\d+)' elementos? hijos?, con tag '(.*?)', atributo '(.*?)' y valor '(.*?)'( \\(WARN en otro caso\\))?$")
	public void assertAdditionalFiltering(Integer expectedChildren, String tag,
			String attribute, String value,
			@Transform(WarnBooleanConverter.class) Boolean warn) {

		commonspec.getLogger().info(
				"{}: Verifying children count, with additional filtering",
				commonspec.getShortBrowser());

		List<WebElement> filteredElems = new ArrayList<WebElement>();

		for (WebElement elem : commonspec.getCurrentElements()) {
			if ((elem.getTagName().equals(tag))
					&& (elem.getAttribute(attribute).equals(value))) {
				filteredElems.add(elem);
			}

		}
		commonspec.setCurrentElements(filteredElems);

		if (warn) {
			assertWarnThat("Unexpected number of children found",
					filteredElems.size(), equalTo(expectedChildren));
		} else {
			assertThat("Unexpected number of children found",
					filteredElems.size(), equalTo(expectedChildren));
		}
	}

	@Parameters({
			@Parameter(name = Parameter.Name.NUMBER, type = Parameter.Type.INT),
			@Parameter(name = Parameter.Name.ATTRIBUTE, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.TEXT, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@And("^'(\\d+)' child(?:ren)? elements?, with attribute '(.*?)' and value '(.*?)'( \\(WARN otherwise\\))?$")
	@Y("^'(\\d+)' elementos? hijos?, con atributo '(.*?)' y valor '(.*?)'( \\(WARN en otro caso\\))?$")
	public void assertAdditionalFilteringTagless(Integer expectedChildren,
			String attribute, String value,
			@Transform(WarnBooleanConverter.class) Boolean warn) {

		commonspec.getLogger().info(
				"{}: Verifying children count, with additional filtering",
				commonspec.getShortBrowser());

		List<WebElement> filteredElems = new ArrayList<WebElement>();

		for (WebElement elem : commonspec.getCurrentElements()) {
			if (elem.getAttribute(attribute).equals(value)) {
				filteredElems.add(elem);
			}

		}
		commonspec.setCurrentElements(filteredElems);

		if (warn) {
			assertWarnThat("Unexpected number of children found",
					filteredElems.size(), equalTo(expectedChildren));
		} else {
			assertThat("Unexpected number of children found",
					filteredElems.size(), equalTo(expectedChildren));
		}
	}

	@Parameters({
			@Parameter(name = Parameter.Name.ATTRIBUTE, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.TEXT, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@And("^with attribute '(.*?)' and value '(.*?)'( \\(WARN otherwise\\))?$")
	@Y("^con atributo '(.*?)' y valor '(.*?)'( \\(WARN en otro caso\\))?$")
	public void assertAdditionalFilteringNumberlessTagless(String attribute,
			String value, @Transform(WarnBooleanConverter.class) Boolean warn) {

		commonspec.getLogger().info(
				"{}: Verifying children count, with additional filtering",
				commonspec.getShortBrowser());

		List<WebElement> filteredElems = new ArrayList<WebElement>();

		for (WebElement elem : commonspec.getCurrentElements()) {
			if (elem.getAttribute(attribute).equals(value)) {
				filteredElems.add(elem);
			}

		}
		commonspec.setCurrentElements(filteredElems);
	}

	@Parameters({ @Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@And("^(?:is|are) visibles?( \\(WARN otherwise\\))?$")
	@Y("^(?:es|son) visibles?( \\(WARN en otro caso\\))?$")
	public void assertVisibles(
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info("{}: Verifying elements visibility ({})",
				commonspec.getShortBrowser(), warn.toString());
		List<WebElement> elems = commonspec.getCurrentElements();
		for (WebElement elem : elems) {
			if (warn) {
				assertWarnThat("Unexpected element visibility",
						elem.isDisplayed(), equalTo(true));
			} else {
				assertThat("Unexpected element visibility", elem.isDisplayed(),
						equalTo(true));
			}
		}
	}

	@Parameters({ @Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@And("^(?:is|are) invisibles?( \\(WARN otherwise\\))?$")
	@Y("^(?:es|son) invisibles?( \\(WARN en otro caso\\))?$")
	public void assertInvisibles(
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info(
				"{}: Verifying elements invisibility ({}})",
				commonspec.getShortBrowser(), warn.toString());
		List<WebElement> elems = commonspec.getCurrentElements();
		for (WebElement elem : elems) {
			if (warn) {
				assertWarnThat("Unexpected element visibility",
						elem.isDisplayed(), equalTo(false));
			} else {
				assertThat("Unexpected element visibility", elem.isDisplayed(),
						equalTo(false));
			}
		}
	}

	@Parameters({ @Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@And("^(?:is|are) enabled( \\(WARN otherwise\\))?$")
	@Y("^están? habilitados?( \\(WARN en otro caso\\))?$")
	public void assertAllEnabled(
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info(
				"{}: Verifying elements enabled status ({})",
				commonspec.getShortBrowser(), warn.toString());
		List<WebElement> elems = commonspec.getCurrentElements();
		for (WebElement elem : elems) {
			if (warn) {
				assertWarnThat("Unexpected element enabled status",
						elem.isEnabled(), equalTo(true));
			} else {
				assertThat("Unexpected element enabled status",
						elem.isEnabled(), equalTo(true));
			}
		}
	}

	@Parameters({ @Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@And("^(?:is|are) disabled( \\(WARN otherwise\\))?$")
	@Y("^están? deshabilitados?( \\(WARN en otro caso\\))?$")
	public void assertAllDisabled(
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info(
				"{}: Verifying elements enabled status ({})",
				commonspec.getShortBrowser(), warn.toString());
		List<WebElement> elems = commonspec.getCurrentElements();
		for (WebElement elem : elems) {
			assertThat("Unexpected element enabled status", elem.isEnabled(),
					equalTo(false));
		}
	}

	@Parameters({ @Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@And("^(?:is|are) selected( \\(WARN otherwise\\))?$")
	@Y("^están? seleccionados?( \\(WARN en otro caso\\))?$")
	public void assertAllSelected(
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info(
				"{}: Verifying elements selected status ({})",
				commonspec.getShortBrowser(), warn.toString());
		List<WebElement> elems = commonspec.getCurrentElements();
		for (WebElement elem : elems) {
			if (warn) {
				assertWarnThat("Element with unexpected selected status",
						elem.isSelected(), equalTo(true));
			} else {
				assertThat("Element with unexpected selected status",
						elem.isSelected(), equalTo(true));
			}
		}
	}

	@Parameters({ @Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@And("^(?:is|are) unselected( \\(WARN otherwise\\))?$")
	@Y("^no están? seleccionados?( \\(WARN en otro caso\\))?$")
	public void assertAllNotSelected(
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info(
				"{}: Verifying elements unselected status ({})",
				commonspec.getShortBrowser(), warn.toString());
		List<WebElement> elems = commonspec.getCurrentElements();
		for (WebElement elem : elems) {
			if (warn) {
				assertWarnThat("Element with unexpected selected status",
						elem.isSelected(), equalTo(false));
			} else {
				assertThat("Element with unexpected selected status",
						elem.isSelected(), equalTo(false));
			}
		}
	}

	@Parameters({
			@Parameter(name = Parameter.Name.TEXT, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@And("^its text is '(.*?)'( \\(WARN otherwise\\))?$")
	@Y("^su texto es '(.*?)'( \\(WARN en otro caso\\))?$")
	public void assertTextIs(String expectedText,
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info(
				"{}: Verifying text for previous first element",
				commonspec.getShortBrowser());
		WebElement elem = commonspec.getCurrentElements().get(0);
		String actual = commonspec.valueOrText(elem);

		if (warn) {
			assertWarnThat("Unexpected text", actual, equalTo(expectedText));
		} else {
			assertThat("Unexpected text", actual, equalTo(expectedText));
		}
	}

	@Parameters({
			@Parameter(name = Parameter.Name.TEXT, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@And("^its text isn't '(.*?)'( \\(WARN otherwise\\))?$")
	@Y("^su texto no es '(.*?)'( \\(WARN en otro caso\\))?$")
	public void assertTextIsNot(String expectedText,
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info(
				"{}: Verifying text for previous first element",
				commonspec.getShortBrowser());
		WebElement elem = commonspec.getCurrentElements().get(0);
		String actual = commonspec.valueOrText(elem);

		if (warn) {
			assertWarnThat("Unexpected text", actual,
					not(equalTo(expectedText)));
		} else {
			assertThat("Unexpected text", actual, not(equalTo(expectedText)));
		}
	}

	@Parameters({
			@Parameter(name = Parameter.Name.TEXT, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@And("^its text contains '(.*?)'( \\(WARN otherwise\\))?$")
	@Y("^su texto contiene '(.*?)'( \\(WARN en otro caso\\))?$")
	public void assertTextContains(String expectedText,
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info("{}: Verifying text (expected)",
				commonspec.getShortBrowser());
		WebElement elem = commonspec.getCurrentElements().get(0);
		String actual = commonspec.valueOrText(elem);

		if (warn) {
			assertWarnThat("Unexpected text", actual,
					containsString(expectedText));
		} else {
			assertThat("Unexpected text", actual, containsString(expectedText));
		}
	}

	@Parameters({
			@Parameter(name = Parameter.Name.TEXT, type = Parameter.Type.STRING),
			@Parameter(name = Parameter.Name.WARN, type = Parameter.Type.BOOL) })
	@And("^its text doesn't contains '(.*?)'( \\(WARN otherwise\\))?$")
	@Y("^su texto no contiene '(.*?)'( \\(WARN en otro caso\\))?$")
	public void assertTextContainsNot(String expectedText,
			@Transform(WarnBooleanConverter.class) Boolean warn) {
		commonspec.getLogger().info("{}: Verifying text (not expected)",
				commonspec.getShortBrowser());
		WebElement elem = commonspec.getCurrentElements().get(0);
		String actual = commonspec.valueOrText(elem);

		if (warn) {
			assertWarnThat("Unexpected text", actual,
					not(containsString(expectedText)));
		} else {
			assertThat("Unexpected text", actual,
					not(containsString(expectedText)));
		}
	}
}