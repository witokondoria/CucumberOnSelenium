package es.rtve;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteTouchScreen;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DroidTest {

	private WebDriver driver;

	@BeforeMethod(enabled = true, groups = { "droid" })
	public void setUp() throws MalformedURLException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "droid");
		capabilities.setCapability("device", "Android");
		// capabilities.setCapability("device", "selendroid");
		capabilities.setCapability(CapabilityType.VERSION, "4.4");
		capabilities.setCapability("app", "chrome");
		// capabilities.setCapability("app", new File
		// ("C:/Users/delgado-j/Desktop/bin/Chrome_33.0.1750.135.apk"));
		capabilities.setCapability("app-package", "com.android.chrome");
		capabilities.setCapability("app-activity", "com.android.chrome.Main");

		driver = new SwipeableWebDriver(new URL(
				"http://192.168.45.188:4444/wd/hub"), capabilities);
		// new URL("http://PR-B00926:4723/wd/hub"), capabilities);
	}

	@AfterMethod(enabled = true, groups = { "droid" })
	public void tearDown() {
		driver.quit();
	}

	@Test(enabled = true, groups = { "droid" })
	public void webView() throws InterruptedException {
		driver.get("http://192.168.45.188:4444/grid/console");
		Thread.sleep(6000);
		driver.get("view-source:http://192.168.45.188:4444/grid/console");
		Thread.sleep(6000);
		driver.get("javascript: alert(document.getElementsByTagName('html')[0].innerHTML);");

		Thread.sleep(6000);
		List<WebElement> elems = driver.findElements(By.tagName("button"));
		for (WebElement ele : elems) {
			System.out.println(ele.getText());

		}
	}

	public class SwipeableWebDriver extends RemoteWebDriver implements
			HasTouchScreen {
		private RemoteTouchScreen touch;

		public SwipeableWebDriver(URL remoteAddress,
				Capabilities desiredCapabilities) {
			super(remoteAddress, desiredCapabilities);
			touch = new RemoteTouchScreen(getExecuteMethod());
		}

		public TouchScreen getTouch() {
			return touch;
		}
	}

}