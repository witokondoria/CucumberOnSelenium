package es.rtve;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import cucumber.api.CucumberOptions;
import es.bull.testingframework.BaseTest;
import es.bull.testingframework.cucumber.testng.CucumberRunner;
import es.bull.testingframework.data.SelectableDataProvider;

@CucumberOptions(features = { "src/test/resources/es/rtve/pestañasInicio.feature" })
public class PestañasInicioTes extends BaseTest {

	@Factory(dataProviderClass = SelectableDataProvider.class, dataProvider = "selectableBrowsers")
	public PestañasInicioTes(String browser) {
		this.browser = browser;
	}

	@Test
	public void cucumberTest() throws Exception {				
			new CucumberRunner(this.getClass(), this.browser).runCukes();
	}
}
