package es.bull.framework.cucumber.testng;

import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.SkipException;

import es.bull.framework.BaseTest;

public class TestSkipperListener implements IHookable {

	@Override
	public void run(IHookCallBack callback, ITestResult testResult) {
		BaseTest br = (BaseTest) testResult.getInstance();
		if (!br.getBrowser().equals("")) {
			callback.runTestMethod(testResult);
		} else {
			testResult.setStatus(ITestResult.SKIP);
			throw new SkipException("Skipped test due config");
		}
	}

}
