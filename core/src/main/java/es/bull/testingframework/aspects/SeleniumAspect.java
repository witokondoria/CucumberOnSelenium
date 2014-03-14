package es.bull.testingframework.aspects;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import es.bull.testingframework.ThreadProperty;
import es.bull.testingframework.specs.BaseSpec;

@Aspect
public class SeleniumAspect {

	@Pointcut("call(static public void org.hamcrest.MatcherAssert.assertThat(..))"
			+ " || call(* org.openqa.selenium.*.click(..))"
			+ " || call(* org.openqa.selenium.*.findElement(..))")
	protected void exceptionCallPointcut() {
	}

	@Around(value = "exceptionCallPointcut()")
	public Object aroundExceptionCalls(ProceedingJoinPoint pjp)
			throws Throwable {
		Throwable newEx = null;
		try {
			Object retVal = pjp.proceed();
			return retVal;
		} catch (AssertionError ae) {
			newEx = treatException(pjp, ae);
			throw newEx;
		} catch (WebDriverException wex) {
			newEx = treatException(pjp, wex);
			throw newEx;
		}
	}

	public Throwable treatException(ProceedingJoinPoint pjp, Throwable ex)
			throws Exception {

		String cap = captureEvidence(pjp, "screenCapture");
		String html = captureEvidence(pjp, "htmlSource");

		String BUILD_URL = System.getenv().get("BUILD_URL");

		String capURL = "\n" + BUILD_URL + "artifact/" + cap + " \n\n";
		String htmlURL = "\n" + BUILD_URL + "artifact/" + html + " \n\n";

		Throwable newEx = new Throwable(capURL + "\n" + htmlURL + "\n"
				+ ex.getMessage(), ex);
		return newEx;
	}

	private Integer getDocumentHeight(WebDriver driver) {
		WebElement body = driver.findElement(By.tagName("html"));
		return body.getSize().getHeight();
	}

	private File adjustLastCapture(Integer newTrailingImageHeight,
			ArrayList<File> capture) throws IOException {
		// cuts last image just in case it dupes information
		Integer finalHeight = 0;
		Integer finalWidth = 0;

		File trailingImage = capture.get(capture.size() - 1);
		capture.remove(capture.size() - 1);

		BufferedImage oldTrailingImage = ImageIO.read(trailingImage);
		BufferedImage newTrailingImage = new BufferedImage(
				oldTrailingImage.getWidth(), oldTrailingImage.getHeight()
						- newTrailingImageHeight, BufferedImage.TYPE_INT_RGB);

		newTrailingImage.createGraphics().drawImage(oldTrailingImage, 0,
				0 - newTrailingImageHeight, null);

		File newTrailingImageF = File.createTempFile("tmpnewTrailingImage",
				".png");
		newTrailingImageF.deleteOnExit();

		ImageIO.write(newTrailingImage, "png", newTrailingImageF);

		capture.add(newTrailingImageF);

		finalWidth = ImageIO.read(capture.get(0)).getWidth();
		for (File cap : capture) {
			finalHeight += ImageIO.read(cap).getHeight();
		}

		BufferedImage img = new BufferedImage(finalWidth, finalHeight,
				BufferedImage.TYPE_INT_RGB);

		Integer y = 0;
		BufferedImage tmpImg = null;
		for (File cap : capture) {
			tmpImg = ImageIO.read(cap);
			img.createGraphics().drawImage(tmpImg, 0, y, null);
			y += tmpImg.getHeight();
		}

		long ts = System.currentTimeMillis() / 1000L;

		File temp;

		temp = File.createTempFile("chromecap" + String.valueOf(ts), ".png");
		temp.deleteOnExit();
		ImageIO.write(img, "png", temp);

		return temp;
	}

	private File chromeFullScreenCapture(WebDriver driver) throws IOException, InterruptedException {
		// scroll loop n times to get the whole page if browser is chrome
		ArrayList<File> capture = new ArrayList<File>();

		Boolean atBottom = false;
		((RemoteWebDriver) driver).manage().window().maximize();
		Integer windowSize = ((Long) ((JavascriptExecutor) driver)
				.executeScript("return document.documentElement.clientHeight"))
				.intValue();
		System.out.println(windowSize);

		Integer accuScroll = 0;
		Integer newTrailingImageHeight = 0;

		while (!atBottom) {
			Thread.sleep(1500);
			capture.add(((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE));

			((JavascriptExecutor) driver).executeScript("if(window.screen)"
					+ " {window.scrollBy(0," + windowSize + ");};");

			accuScroll += windowSize;
			if (getDocumentHeight(driver) <= accuScroll) {
				atBottom = true;
			}
		}
		newTrailingImageHeight = accuScroll - getDocumentHeight(driver);
		return adjustLastCapture(newTrailingImageHeight, capture);
	}

	private String captureEvidence(ProceedingJoinPoint pjp, String type)
			throws Exception {

		BaseSpec spec = (BaseSpec) pjp.getThis();
		WebDriver driver = spec.getCommonSpec().getDriver();
		String dir = "./target/executions/";

		String clazz = ThreadProperty.get("class");
		String currentBrowser = ((RemoteWebDriver) driver).getCapabilities()
				.getBrowserName()
				+ "_"
				+ ((RemoteWebDriver) driver).getCapabilities().getVersion();
		String currentData = ThreadProperty.get("dataSet");

		if (!currentData.equals("")) {
			currentData = currentData.replaceAll("[\\\\|\\/|\\|\\s|:]", "_");
		}

		String outputFile = dir + clazz + "/" + currentBrowser + "-"
				+ currentData;
		outputFile = outputFile.replaceAll(" ", "_");

		if (type.equals("screenCapture")) {
			outputFile = outputFile + ".png";
			File file = null;
			
			if (currentBrowser.startsWith("chrome")) {
				file = chromeFullScreenCapture(driver);
			} else {
				file = ((TakesScreenshot) driver)
						.getScreenshotAs(OutputType.FILE);
			}
			try {
				FileUtils.copyFile(file, new File(outputFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (type.equals("htmlSource")) {
			outputFile = outputFile + ".html";
			String source = ((RemoteWebDriver) driver).getPageSource();

			File fout = new File(outputFile);
			fout.getParentFile().mkdirs();
			FileOutputStream fos = new FileOutputStream(fout, true);

			Writer out = new OutputStreamWriter(fos, "UTF8");
			PrintWriter writer = new PrintWriter(out, false);
			writer.append(source);
			out.close();
		}
		return outputFile;
	}
}
