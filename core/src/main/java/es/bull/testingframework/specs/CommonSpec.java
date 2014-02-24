package es.bull.testingframework.specs;

import static org.testng.Assert.fail;
import ij.ImagePlus;
import ij.io.FileSaver;
import ij.plugin.ImageCalculator;
import ij.process.ImageProcessor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.bull.testingframework.ThreadProperty;

public class CommonSpec {

	private WebDriver driver;
	private List<WebElement> currentElements;
	private String browser;
	private String shortBrowser;
	private final Logger logger = LoggerFactory.getLogger(ThreadProperty
			.get("class"));

	private long startTS;

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public WebDriver getDriver() {
		return this.driver;
	}

	public String getBrowser() {
		return this.browser;
	}

	public String getShortBrowser() {
		return this.shortBrowser;
	}

	public Logger getLogger() {
		return this.logger;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public void setShortBrowser(String shortBrowser) {
		this.shortBrowser = shortBrowser;
	}

	public List<WebElement> getCurrentElements() {
		return currentElements;
	}

	public void setCurrentElements(List<WebElement> currentElements) {
		this.currentElements = currentElements;
	}

	public long getStartTS() {
		return startTS;
	}

	public void setStartTS(long startTS) {
		this.startTS = startTS;
	}

	public List<WebElement> locateElements(String attrib, String value) {
		List<WebElement> elems = null;

		this.getLogger().info("{}: Locating elements", this.getShortBrowser());

		if (attrib.equals("id")) {
			elems = this.getDriver().findElements(By.id(value));
		} else if (attrib.equals("name")) {
			elems = this.getDriver().findElements(By.name(value));
		} else if (attrib.equals("class")) {
			elems = this.getDriver().findElements(By.className(value));
		} else if (attrib.equals("xpath")) {
			elems = this.getDriver().findElements(By.xpath(value));
		} else {
			fail("Unimplemented locator method");
		}
		return elems;
	}

	public Double capsCompare(String pestanya, BufferedImage capture,
			BufferedImage expected) {
		ImagePlus captureI = new ImagePlus("captured", capture);
		ImagePlus expectedI = new ImagePlus("expected", expected);

		ImageProcessor ip = captureI.getProcessor();
		captureI.trimProcessor();
		captureI.setProcessor(null, ip.convertToByte(false));

		ImageProcessor ipe = expectedI.getProcessor();
		expectedI.trimProcessor();
		expectedI.setProcessor(null, ipe.convertToByte(false));

		FileSaver fse = new FileSaver(expectedI);
		fse.saveAsPng("target/executions/" + pestanya + browser
				+ "_expected_bw.png");

		ImageCalculator calc = new ImageCalculator();
		calc.run("substract", expectedI, captureI);

		FileSaver fsc = new FileSaver(captureI);
		fsc.saveAsPng("target/executions/" + pestanya + browser
				+ "_captured_bw.png");
		fse.saveAsPng("target/executions/" + pestanya + browser
				+ "_substracted_bw.png");

		Integer imagePixels = captureI.getHeight() * captureI.getWidth();
		Double different = 0.01;

		for (int i = 0; i < expectedI.getWidth(); i++) {
			for (int j = 0; j < expectedI.getHeight(); j++) {
				if (expectedI.getPixel(i, j)[0] != 0) {
					different++;
				}
			}
		}
		Double variation = (different * 100) / imagePixels;
		return (100 - variation);
	}

	public BufferedImage screenCapture(WebElement element) throws Exception {
		((Locatable) element).getCoordinates().inViewPort();
		Thread.sleep(2000);
		((Locatable) element).getCoordinates().inViewPort();

		File screenshot = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);

		int eleWidth = element.getSize().getWidth();
		int eleHeight = element.getSize().getHeight();
		int startWidth = 0;
		int startHeight = 0;
		if (shortBrowser.substring(0, 1).equals("P")) {
			startWidth = element.getLocation().getX();
			startHeight = element.getLocation().getY();
		} else {
			startWidth = ((Locatable) element).getCoordinates().inViewPort()
					.getX();
			startHeight = ((Locatable) element).getCoordinates().inViewPort()
					.getY();
		}

		BufferedImage eleScreenshot = fullImg.getSubimage(startWidth,
				startHeight, eleWidth, eleHeight);

		return eleScreenshot;
	}
}
