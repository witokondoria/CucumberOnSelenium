package es.bull.testingframework.specs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.thoughtworks.selenium.SeleniumException;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import es.bull.testingframework.ThreadProperty;

public class HookGSpec extends BaseSpec {

	public HookGSpec(CommonSpec spec) {
		this.commonspec = spec;
	}

	@Before
	public void cucumberSetup() throws MalformedURLException {
		String browserType = "";

		commonspec.setBrowser(ThreadProperty.get("browser"));
		commonspec.getLogger().info("Setting up selenium for {}",
				ThreadProperty.get("class"));
		String hub = System.getProperty("selenium.gridHub");
		hub = hub + "/wd/hub";
		browserType = commonspec.getBrowser().split("_")[0];
		String version = commonspec.getBrowser().split("_")[1];
		commonspec.setShortBrowser(commonspec.getBrowser().substring(0, 1)
				.toUpperCase()
				+ version);

		DesiredCapabilities capabilities = null;

		if (browserType.toLowerCase().equals("droidemu")) {
			capabilities = DesiredCapabilities.chrome();
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("disable-plugins");
			chromeOptions.addArguments("use-mobile-user-agent");
			chromeOptions.addArguments("app=http://rtve.es/api/blank");
			chromeOptions.addArguments("app-window-size=1000,700");
			chromeOptions
					.addArguments("user-agent=\"Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 4 Build/JOP40D) "
							+ "AppleWebKit/535.19 (KHTML, like Gecko) "
							+ "Chrome/18.0.1025.166 Mobile Safari/535.19\")\"");
			capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		} else if (browserType.toLowerCase().equals("chrome")) {
			capabilities = DesiredCapabilities.chrome();
		} else if (browserType.toLowerCase().equals("firefox")) {
			capabilities = DesiredCapabilities.firefox();
		} else if (browserType.toLowerCase().equals("phantomjs")) {
			capabilities = DesiredCapabilities.phantomjs();
		} else if (browserType.toLowerCase().equals("internet explorer")) {
			capabilities = DesiredCapabilities.internetExplorer();
		} else {
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

		commonspec.getDriver().manage().deleteAllCookies();

		// FIXME: shamefully hardcoded
		if (!browserType.toLowerCase().equals("droidemu")) {
			commonspec.getDriver().manage().window()
					.setSize(new Dimension(1366, 768));
		}
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
