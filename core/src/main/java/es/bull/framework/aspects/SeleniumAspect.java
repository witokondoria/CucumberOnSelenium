package es.bull.framework.aspects;

import java.io.File;
import java.io.IOException;

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

import es.bull.framework.ThreadProperty;
import es.bull.framework.specs.BaseSpec;

@Aspect
public class SeleniumAspect {

	@Pointcut("call(static public void org.hamcrest.MatcherAssert.assert*(..))"
			+ " || call(* org.openqa.selenium.*.findElement(..))"
			+ " || call(* org.openqa.selenium.*.findElements(..))")
	protected void exceptionCallPointcut() {
	}

	@Around(value = "exceptionCallPointcut()")
	public Object aroundExceptionCalls(ProceedingJoinPoint pjp) throws Throwable {
		try {
			Object retVal = pjp.proceed();
			 return retVal;
		} catch (AssertionError | WebDriverException ex) {
			String cap = captureScreen(pjp);
			String BUILD_URL = System.getenv().get("BUILD_URL");

			String capURL = "\n" + BUILD_URL + "artifact/" + cap + " \n\n";
			Throwable newEx = new Throwable(capURL + ex.getMessage(), ex);
			throw newEx;
		}
	}

	private String captureScreen(ProceedingJoinPoint pjp) throws Exception {

		BaseSpec spec = (BaseSpec) pjp.getThis();
		WebDriver driver = spec.getCommonSpec().getDriver();
		String dir = "./target/executions/";

		String clazz = ThreadProperty.get("class");
		String currentBrowser = ((RemoteWebDriver) driver).getCapabilities()
				.getBrowserName()
				+ "_"
				+ ((RemoteWebDriver) driver).getCapabilities().getVersion();
		String currentData = ThreadProperty.get("dataSet");

		String outputFile = dir + clazz + "/" + currentBrowser + "-"
				+ currentData + ".png";
		outputFile = outputFile.replaceAll(" ", "_");
		File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(file, new File(outputFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputFile;
	}
}
