package es.bull.testingframework.cucumber.testng;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.TestListenerAdapter;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

public class ExecutiveResultsReporter extends TestListenerAdapter {

	private String toImgTag(String browser) {

		ResultsBackend results = ResultsBackend.getInstance();
		String webcontext = results.getContext();
		return "<img src='" + webcontext + "/userContent/cucumber/" + browser
				+ ".png'>";
	}

	private ArrayList<String> getGroupsForClass(String clazz,
			ITestContext context) {

		ArrayList<String> response = new ArrayList<String>();
		Map<String, Collection<ITestNGMethod>> MethodsGroups = context
				.getSuite().getMethodsByGroups();

		for (Map.Entry<String, Collection<ITestNGMethod>> entry : MethodsGroups
				.entrySet()) {
			for (ITestNGMethod method : entry.getValue()) {
				if (method.getTestClass().toString().contains(clazz)) {
					response.add(entry.getKey());
				}
			}
		}
		return response;
	}

	public class Browser {
		private String name;
		private String version;

		public Browser(String name, String version) {
			this.name = name;
			this.version = version;
		}

		public String getName() {
			return name;
		}

		public String getVersion() {
			return version;
		}
	}

	public class Group {
		private String name;
		private String hsl;

		public Group(String name, String hsl) {
			this.name = name;
			this.hsl = hsl;
		}

		public String getName() {
			return name;
		}

		public String getHsl() {
			return hsl;
		}
	}

	public class BrowserResult {
		private String status;
		private String summary;

		public BrowserResult(String status, String summary) {
			this.status = status;
			this.summary = summary;
		}

		public String getStatus() {
			return status;
		}

		public String getSummary() {
			return summary;
		}
	}

	public class DataResult {
		private String value;
		private ArrayList<String> results = new ArrayList<String>();

		public DataResult(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public ArrayList<String> getResults() {
			return results;
		}

		public void addResult(String res) {
			results.add(res);
		}
	}

	public class ScenarioResult {
		private String name;
		private ArrayList<DataResult> dataResults = new ArrayList<DataResult>();

		public ScenarioResult(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public ArrayList<DataResult> getDataResults() {
			return dataResults;
		}

		public void addDataResult(DataResult data) {
			dataResults.add(data);
		}
	}

	public class Feature {
		private String name;
		private ArrayList<Group> groups = new ArrayList<Group>();
		private String classPackage;
		private String buildUrl = "";
		private String clazz;
		private ArrayList<BrowserResult> browserResults = new ArrayList<BrowserResult>();
		private ArrayList<ScenarioResult> scenarioResults = new ArrayList<ScenarioResult>();

		public Feature(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public ArrayList<Group> getGroups() {
			return groups;
		}

		public void addGroup(Group gr) {
			groups.add(gr);
		}

		public String getClassPackage() {
			return classPackage;
		}

		public void setClassPackage(String classPackage) {
			this.classPackage = classPackage;
		}

		public String getBuildUrl() {
			return buildUrl;
		}

		public void setBuildUrl(String buildUrl) {
			if (buildUrl != null) {
				this.buildUrl = buildUrl;
			}
		}

		public String getClazz() {
			return clazz;
		}

		public void setClazz(String clazz) {
			this.clazz = clazz;
		}

		public ArrayList<BrowserResult> getBrowserResults() {
			return browserResults;
		}

		public void addBrowserResult(BrowserResult br) {
			browserResults.add(br);
		}

		public ArrayList<ScenarioResult> getScenarioResults() {
			return scenarioResults;
		}

		public void addScenarioResult(ScenarioResult sr) {
			scenarioResults.add(sr);
		}
	}

	@Override
	public void onFinish(ITestContext context) {
		Configuration cfg = new Configuration();
		Writer fileWriter;
		Template template = null;
		cfg.setClassForTemplateLoading(this.getClass(), "/");
		try {
			template = cfg.getTemplate("dashboard.ftl");
		} catch (IOException e) {
			e.printStackTrace();
		}

		cfg.setIncompatibleImprovements(new Version(2, 3, 20));
		cfg.setDefaultEncoding("UTF-8");

		Map<String, Object> input = new HashMap<String, Object>();

		List<Group> groupsF = new ArrayList<Group>();
		for (String group : context.getIncludedGroups()) {
			Random rand = new Random();
			String hsl = ((int) (rand.nextInt(24) + 4) * 12) + ", 90%, 50%";
			Group groupDef = new Group(group, hsl);
			while (groupsF.contains(groupDef)) {
				hsl = ((int) (rand.nextInt(24) + 4) * 12) + ", 90%, 50%";
				groupDef = new Group(group, hsl);
			}
			groupsF.add(groupDef);
		}
		input.put("groups", groupsF);

		ResultsBackend results = ResultsBackend.getInstance();
		ArrayList<String> browsers = results.getSortedUniqueBrowsers();

		List<Browser> browserF = new ArrayList<Browser>();
		for (String browser : browsers) {
			browserF.add(new Browser(toImgTag(browser.split("_")[0]), browser
					.split("_")[1]));
		}
		input.put("browsers", browserF);

		String webcontext = results.getContext();
		input.put("webcontext", webcontext);
		ArrayList<String> classes = results.getSortedUniqueClasses();

		List<Feature> featureF = new ArrayList<Feature>();
		for (String clazz : classes) {
			String feature = results.featureForClass(clazz);
			Feature feat = new Feature(feature);

			feat.setClassPackage(clazz.substring(0, clazz.lastIndexOf(".")));
			feat.setBuildUrl(System.getenv().get("BUILD_URL"));
			feat.setClazz(clazz);

			for (String group : getGroupsForClass(clazz, context)) {
				for (Group gr : groupsF) {
					if (gr.getName().equals(group)
							&& !(feat.getGroups().contains(gr))) {
						feat.addGroup(gr);
					}
				}
			}
			Integer browserCount = 1;
			for (String browser : browsers) {
				browserCount++;
				String exampleResult = results.getExecutionResults(clazz,
						browser);
				if (exampleResult.equals("-")) {
					feat.addBrowserResult(new BrowserResult("-", "-"));
				} else if (exampleResult.split(":")[1].startsWith("0")) { // ok
					feat.addBrowserResult(new BrowserResult("OK",
							exampleResult = exampleResult.split("/")[1]));
				} else if (exampleResult.split(":")[0].startsWith("0")) { // err
					feat.addBrowserResult(new BrowserResult("KO",
							exampleResult = exampleResult.split("/")[1]));
				} else {
					feat.addBrowserResult(new BrowserResult("NOK",
							exampleResult = exampleResult.split("/")[1]));
				}
			}

			input.put("browserCount", browserCount);

			for (String scenario : results.getScenarios(clazz)) {
				ScenarioResult scenarioF = new ScenarioResult(scenario);
				for (String data : results.getData(clazz, scenario)) {
					DataResult dataF = new DataResult(data);
					for (String browser : browsers) {

						dataF.addResult(results.getExecutionResults(clazz,
								scenario, data, browser, feature));
					}
					scenarioF.addDataResult(dataF);
				}
				feat.addScenarioResult(scenarioF);
			}

			featureF.add(feat);
		}
		input.put("features", featureF);

		try {
			fileWriter = new FileWriter(new File(
					"target/executions/dashboard.html"));
			template.process(input, fileWriter);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}
}
