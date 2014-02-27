package es.bull.testingframework.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import es.bull.testingframework.cucumber.testng.ResultsBackend;

public class CommonDataProvider {

	public static ArrayList<String> gridUniqueBrowsers(
			ArrayList<String> gridBrowsers) throws IOException {

		HashSet<String> hs = new HashSet<String>();
		hs.addAll(gridBrowsers);
		gridBrowsers.clear();
		gridBrowsers.addAll(hs);

		return gridBrowsers;
	}

	public static ArrayList<String> gridBrowsers() throws IOException {
		ArrayList<String> response = new ArrayList<String>();

		String URL = System.getProperty("selenium.gridHub");
		URL = URL + "/grid/console";
		Document doc = Jsoup.connect(URL).timeout(20000).get();
		Elements slaves = doc.select("div.proxy");

		for (Element slave : slaves) {
			String slaveStatus = slave.select("p.proxyname").first().text();
			if (!slaveStatus.contains("Connection")) {
				Integer iBusy = 0;
				Elements browserList = slave.select("div.content_detail")
						.select("*[title]");
				Elements busyBrowserList = slave.select("div.content_detail")
						.select("p > .busy");
				for (Element browserDetails : browserList) {
					if (browserDetails.attr("title").startsWith("{")) {
						Pattern pat = Pattern
								.compile("browserName=(.*?),.*?(version=(.*?))?}");
						Matcher m = pat.matcher(browserDetails.attr("title"));
						while (m.find()) {
							response.add(m.group(1) + "_" + m.group(3));
						}
					} else {
						String version = busyBrowserList.get(iBusy).parent()
								.text();
						String browser = busyBrowserList.get(iBusy).text();
						version = version.substring(2);
						version = version.replace(browser, "");
						String browserSrc = busyBrowserList.get(iBusy)
								.select("img").attr("src");
						if (!browserSrc.equals("")) {
							browser = browserSrc.substring(
									browserSrc.lastIndexOf("/") + 1,
									browserSrc.length() - 4);
						}
						response.add(browser + "_" + version);
						iBusy++;
					}
				}
			}
		}

		return response;
	}

	public static ArrayList<String> gridJenkinsBrowsers(String URL)
			throws IOException {

		ArrayList<String> response = new ArrayList<String>();
		Document doc = null;

		if (!URL.contains("localhost")) {
			String username = "Bull";
			String password = "bull";
			String login = username + ":" + password;
			String base64login = new String(Base64.encodeBase64(login
					.getBytes()));

			doc = Jsoup.connect(URL)
					.header("Authorization", "Basic " + base64login)
					.timeout(20000).get();
		} else {
			doc = Jsoup.connect(URL).timeout(20000).get();
		}

		Elements slaves = doc
				.select("#main-panel > table > tbody > tr > td:nth-child(2)");

		for (Element slave : slaves) {
			Pattern pat = Pattern
					.compile("browserName=(.*?),.*?(version=(.*?))?}");
			Matcher m = pat.matcher(slave.ownText());
			while (m.find()) {
				response.add(m.group(1) + "_" + m.group(3));
			}
		}

		return response;
	}

	protected static ArrayList<String> selectedBrowsers(String method)
			throws IOException {
		ArrayList<String> response = new ArrayList<String>();
		ArrayList<String> availableBrowsers = null;

		Properties props = new Properties();

		InputStream stream = new FileInputStream("./environment.properties");
		props.load(stream);

		String seleniumOnJenkins = props.getProperty("seleniumOnJenkins", "");
		ResultsBackend results = ResultsBackend.getInstance();

		if (seleniumOnJenkins.equals("")) {
			availableBrowsers = new ArrayList<String>(gridBrowsers());
			results.setContext("");
		} else {
			// FIXME: parse context from seleniumOnJenkins variable
			if (seleniumOnJenkins.contains("localhost")) { 
				results.setContext("");
			} else {
				results.setContext("/jenkins");
			}
			availableBrowsers = new ArrayList<String>(
					gridJenkinsBrowsers(seleniumOnJenkins));
		}
		availableBrowsers = gridUniqueBrowsers(availableBrowsers);

		String settings = props.getProperty(method, "@grid {all_distinct}");
		if (settings.equals("@grid {all_distinct}")) {
			response = availableBrowsers;
		} else if (settings.startsWith("@browsers")) {
			settings = settings.replace("@browsers {", "");
			settings = settings.replace("}", "");
			settings = settings.replace(", ", ",");
			settings = settings.replace(" ,", ",");
			String[] aBr = settings.split(",");

			for (String br : aBr) {
				if (!br.contains("_") && !br.equals("")) { // no version
															// specified
					for (String browser : availableBrowsers) {
						if (browser.startsWith(br)) {
							response.add(browser);
						}
					}
				} else if (availableBrowsers.contains(br) && !br.equals("")) {
					response.add(br);
				}
			}
		}
		return response;
	}
}
