package es.bull.framework;

import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterClass;

abstract public class BaseTest{

	protected String browser = "";

	public String getBrowser() {
		return this.browser;
	}
	
	@BeforeSuite
	public void beforeSuite() throws IOException {
	}

	@BeforeClass
	public void beforeClass() {
		ThreadProperty.set("class", this.getClass().getCanonicalName());
	}

	@AfterClass
	public void afterClass() {
		
	}
	
	@BeforeMethod
	public void beforeMethod(Method method) {
		ThreadProperty.set("browser", this.browser);
	}
}
