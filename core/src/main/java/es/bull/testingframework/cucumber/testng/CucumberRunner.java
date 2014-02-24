package es.bull.testingframework.cucumber.testng;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cucumber.api.CucumberOptions;
import cucumber.runtime.ClassFinder;
import cucumber.runtime.CucumberException;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;
import cucumber.runtime.Utils;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import es.bull.testingframework.matchers.AssertionWarn;

public class CucumberRunner {

	private final cucumber.runtime.Runtime runtime;

	public CucumberRunner(Class<?> clazz, String browser) throws IOException {
		ClassLoader classLoader = clazz.getClassLoader();
		ResourceLoader resourceLoader = new MultiLoader(classLoader);

		RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(
				clazz, new Class[] { CucumberOptions.class });
		RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();

		List<String> uniqueGlue = new ArrayList<String>();
		uniqueGlue.add("classpath:es/rtve/specs");
		uniqueGlue.add("classpath:es/bull/testingframework/specs");
		runtimeOptions.getGlue().clear();
		runtimeOptions.getGlue().addAll(uniqueGlue);

		new File("target/executions/").mkdirs();
		CucumberReporter reporter = new CucumberReporter(
				Utils.toURL("target/executions/" + clazz.getCanonicalName()
						+ "-" + browser + ".xml"), clazz.getCanonicalName());
		runtimeOptions.getFormatters().add(reporter);
		ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader,
				classLoader);
		runtime = new cucumber.runtime.Runtime(resourceLoader, classFinder,
				classLoader, runtimeOptions);
	}

	public void runCukes() throws IOException {
		runtime.run();
		
        if (!runtime.getErrors().isEmpty() && !(runtime.getErrors().get(0) instanceof AssertionWarn)) {
            throw new CucumberException(runtime.getErrors().get(0));
        }
	}
}