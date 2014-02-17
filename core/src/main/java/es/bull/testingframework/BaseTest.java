package es.bull.testingframework;

import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

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
		//TODO: parallel execution and priorization based on grid status
	}
}
