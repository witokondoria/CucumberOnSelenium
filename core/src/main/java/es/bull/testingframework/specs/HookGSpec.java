package es.bull.testingframework.specs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.thoughtworks.selenium.SeleniumException;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import es.bull.testingframework.ThreadProperty;
import es.bull.testingframework.specs.CommonSpec;

public class HookGSpec {

	private CommonSpec commonspec;

	public HookGSpec(CommonSpec spec) {
		this.commonspec = spec;
	}

	@Before
	public void cucumberSetup() throws MalformedURLException {
		String browserType = "";

		commonspec.setBrowser(ThreadProperty.get("browser"));
		String hub = System.getProperty("selenium.gridHub");
		hub = hub + "/wd/hub";
		browserType = commonspec.getBrowser().split("_")[0];
		String version = commonspec.getBrowser().split("_")[1];
		commonspec.setShortBrowser(commonspec.getBrowser().substring(0, 1)
				.toUpperCase()
				+ version);

		DesiredCapabilities capabilities = null;

		switch (browserType.toLowerCase()) {
		case "chrome":
			capabilities = DesiredCapabilities.chrome();
			break;
		case "firefox":
			capabilities = DesiredCapabilities.firefox();
			break;
		case "phantomjs":
			capabilities = DesiredCapabilities.phantomjs();
			break;
		case "internet explorer":
			capabilities = DesiredCapabilities.internetExplorer();
			break;
		default:
			commonspec.getLogger().error("Unknown browser: " + browserType);
			throw new SeleniumException("Unknown browser: " + browserType);
		}

		capabilities.setVersion(version);

		String proxy = System.getProperty("selenium.proxy");
		if (proxy != "") {
			Proxy oProxy = new Proxy().setHttpProxy(proxy);
			capabilities.setCapability(CapabilityType.PROXY, oProxy);
		}

		commonspec.setDriver(new RemoteWebDriver(new URL(hub), capabilities));
		commonspec.getDriver().manage().timeouts()
				.pageLoadTimeout(60, TimeUnit.SECONDS);
		commonspec.getDriver().manage().timeouts()
				.implicitlyWait(10, TimeUnit.SECONDS);
		commonspec.getDriver().manage().timeouts()
				.setScriptTimeout(30, TimeUnit.SECONDS);

		//FIXME: shamefully hardcoded
		commonspec.getDriver().manage().window()
				.setSize(new Dimension(1366, 768));

		commonspec.setDriver(new Augmenter().augment(commonspec.getDriver()));
	}

	@After
	public void cucumberTeardown() {
		commonspec.getLogger().info("{}: Ended running scenario\n",
				commonspec.getShortBrowser());
		if (commonspec.getDriver() != null) {
			commonspec.getDriver().quit();
		}
	}
}
