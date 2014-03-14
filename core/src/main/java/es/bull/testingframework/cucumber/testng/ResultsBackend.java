package es.bull.testingframework.cucumber.testng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class ResultsBackend {

	private static ResultsBackend instance = new ResultsBackend();

	private ArrayList<ScenarioResult> sResult = new ArrayList<ScenarioResult>();
	private String context = "";

	private ResultsBackend() {
	}

	public static ResultsBackend getInstance() {
		return instance;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public ArrayList<ScenarioResult> getScenarioResult() {
		return sResult;
	}

	public ArrayList<ScenarioResult> getScenarioResult(String clazz) {
		ArrayList<ScenarioResult> response = new ArrayList<ScenarioResult>();
		for (ScenarioResult result : sResult) {
			if (result.getClazz().equals(clazz)) {
				response.add(result);
			}
		}
		return response;
	}

	public void addScenarioResult(String clazz, String feature,
			Integer position, String scenarioName, String browser, String data,
			String executionResult, String warn) {
		ScenarioResult result = new ScenarioResult(clazz, feature, position,
				scenarioName, browser, data, executionResult, warn);
		sResult.add(result);
	}

	public ArrayList<String> getSortedUniqueBrowsers() {
		ArrayList<String> browsers = new ArrayList<String>();
		for (ScenarioResult result : sResult) {
			browsers.add(result.getBrowser());
		}
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(browsers);
		browsers.clear();
		browsers.addAll(hs);
		Collections.sort(browsers);
		return browsers;
	}

	public ArrayList<String> getSortedUniqueFeatures() {
		ArrayList<String> features = new ArrayList<String>();
		for (ScenarioResult result : sResult) {
			features.add(result.getFeature());
		}
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(features);
		features.clear();
		features.addAll(hs);
		Collections.sort(features);
		return features;
	}

	public ArrayList<String> getSortedUniqueClasses() {
		ArrayList<String> classes = new ArrayList<String>();
		for (ScenarioResult result : sResult) {
			classes.add(result.getClazz());
		}
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(classes);
		classes.clear();
		classes.addAll(hs);
		Collections.sort(classes);
		return classes;
	}

	public String getExecutionResults(String clazz, String browser) {
		Integer total = 0;
		Integer passed = 0;
		Integer failed = 0;

		String response = "-";

		for (ScenarioResult result : sResult) {
			if (result.getBrowser().equals(browser)
					&& result.getClazz().equals(clazz)) {
				total++;
				if (result.getExecutionResult().equals("PASS")) {
					passed++;
				} else if (result.getExecutionResult().equals("FAIL")) {
					failed++;
				}
			}
		}

		if (total > 0) {
			response = String.valueOf(passed) + ":" + String.valueOf(failed)
					+ "/" + String.valueOf(total);
		}
		return response;
	}

	public String getExecutionResults(String clazz, String scenario,
			String data, String browser, String feature) {

		String response = "-";
		String BUILD_URL = System.getenv().get("BUILD_URL");

		for (ScenarioResult result : sResult) {
			if (result.getBrowser().equals(browser)
					&& result.getData().equals(data)
					&& result.getScenario().equals(scenario)
					&& result.getClazz().equals(clazz)) {

				data = data.replaceAll("[\\\\|\\/|\\|:]", "_");

				response = "<a href='" + BUILD_URL + "/testngreports/"
						+ clazz.substring(0, clazz.lastIndexOf(".")) + "/"
						+ clazz + "/" + feature + " " + data + " " + browser
						+ "' class='fancybox fancybox.iframe' >";

				if (result.getWarn().equals("TRUE")) {
					if (result.getExecutionResult().equals("PASS")) {
						response = response + "<img alt='Passed test' src='"
								+ context
								+ "/userContent/cucumber/OKW.png'/></a>";
					}
				} else {
					if (result.getExecutionResult().equals("PASS")) {
						response = response + "<img alt='Passed test' src='"
								+ context
								+ "/userContent/cucumber/OK.png'/></a>";
					} else if (result.getExecutionResult().equals("FAIL")) {
						response = response + "<img alt='Failed test' src='"
								+ context
								+ "/userContent/cucumber/KO.png'/></a>";
					}
				}
			}
		}
		return response;
	}

	public String featureForClass(String clazz) {
		for (ScenarioResult result : sResult) {
			if (result.getClazz().equals(clazz)) {
				return result.getFeature();
			}
		}
		return "-";
	}

	public ArrayList<String> getData(String clazz, String scenario) {
		ArrayList<String> datas = new ArrayList<String>();

		for (ScenarioResult resultdata : sResult) {
			if ((resultdata.getClazz().equals(clazz))
					&& (resultdata.getScenario().equals(scenario))
					&& !(datas.contains(resultdata.getData()))) {
				datas.add(resultdata.getData());
			}
		}
		return datas;
	}

	public ArrayList<String> getScenarios(String clazz) {
		ArrayList<String> scenarios = new ArrayList<String>();

		String previousScenario = "";
		for (ScenarioResult resultdata : sResult) {
			String currentScenario = resultdata.getScenario();
			if ((resultdata.getClazz().equals(clazz))
					&& (!currentScenario.equals(previousScenario))) {
				previousScenario = currentScenario;
				scenarios.add(currentScenario);
			}
		}
		return scenarios;
	}

	public class ScenarioResult {

		private String clazz;
		private String feature;
		private Integer position;
		private String scenario;
		private String browser;
		private String data;
		private String executionResult;
		private String warn;

		public ScenarioResult(String clazz, String feature, Integer position,
				String scenario, String browser, String data,
				String executionResult, String warn) {
			this.setClazz(clazz);
			this.setFeature(feature);
			this.setPosition(position);
			this.setScenario(scenario);
			this.setBrowser(browser);
			this.setData(data);
			this.setExecutionResult(executionResult);
			this.setWarn(warn);
		}

		public String getClazz() {
			return clazz;
		}

		public void setClazz(String clazz) {
			this.clazz = clazz;
		}

		public Integer getPosition() {
			return position;
		}

		public void setPosition(Integer position) {
			this.position = position;
		}

		public String getScenario() {
			return scenario;
		}

		public void setScenario(String scenario) {
			this.scenario = scenario;
		}

		public String getFeature() {
			return feature;
		}

		public void setFeature(String feature) {
			this.feature = feature;
		}

		public String getBrowser() {
			return browser;
		}

		public void setBrowser(String browser) {
			this.browser = browser;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}

		public String getExecutionResult() {
			return executionResult;
		}

		public void setExecutionResult(String executionResult) {
			this.executionResult = executionResult;
		}

		public String getWarn() {
			return warn;
		}

		public void setWarn(String warn) {
			this.warn = warn;
		}
	}
}
