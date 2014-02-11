package es.bull.framework.specs;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.plugin.ImageCalculator;
import ij.process.ImageProcessor;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.bull.framework.ThreadProperty;

public class CommonSpec {

	private WebDriver driver;
	private WebElement currentElement;
	private String browser;
	private String shortBrowser;
	private final Logger logger = LoggerFactory.getLogger(ThreadProperty
			.get("class"));

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

	public WebElement getCurrentElement() {
		return currentElement;
	}

	public void setCurrentElement(WebElement currentElement) {
		this.currentElement = currentElement;
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
		switch (shortBrowser.substring(0, 1)) {
		case "P":
			startWidth = element.getLocation().getX();
			startHeight = element.getLocation().getY();
			break;
		default:
			startWidth = ((Locatable) element).getCoordinates().inViewPort()
					.getX();
			startHeight = ((Locatable) element).getCoordinates().inViewPort()
					.getY();
			break;
		}

		BufferedImage eleScreenshot = fullImg.getSubimage(startWidth,
				startHeight, eleWidth, eleHeight);

		return eleScreenshot;
	}
}
