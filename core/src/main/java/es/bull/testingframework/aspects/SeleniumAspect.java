package es.bull.testingframework.aspects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.commons.io.FileUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
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
			File file = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
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
