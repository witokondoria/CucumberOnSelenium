package es.bull.testingframework.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
		ArrayList<String> response = new ArrayList<>();

		String URL = System.getProperty("selenium.gridHub");
		URL = URL + "/grid/console";
		Document doc = Jsoup.connect(URL).timeout(10000).get();

		Elements slaves = doc.select("div.proxy");

		for (Element slave : slaves) {
			String slaveStatus = slave.select("p.proxyname").first().text();
			if (!slaveStatus.contains("Connection")) {
				Elements browserList = slave.select("div.content_detail")
						.select("*[title]");
				for (Element browserDetails : browserList) {
					Pattern pat = Pattern
							.compile("browserName=(.*?),.*?(version=(.*?))?}");
					Matcher m = pat.matcher(browserDetails.attr("title"));

					while (m.find()) {
						response.add(m.group(1) + "_" + m.group(3));
					}
				}
			}
		}

		return response;
	}

	protected static ArrayList<String> selectedBrowsers(String method)
			throws IOException {
		ArrayList<String> response = new ArrayList<>();

		ArrayList<String> availableBrowsers = new ArrayList<>(gridBrowsers());
		availableBrowsers = gridUniqueBrowsers(availableBrowsers);
		
		Properties props = new Properties();

		InputStream stream = new FileInputStream("./environment.properties");
		props.load(stream);

		String settings = props.getProperty(method, "@grid{all_distinct}");
		if (settings.equals("@grid {all_distinct}")) {
			response = availableBrowsers;
		} else if (settings.startsWith("@browsers")) {
			settings = settings.replace("@browsers {", "");
			settings = settings.replace("}", "");
			settings = settings.replace(", ", ",");
			settings = settings.replace(" ,", ",");
			String[] aBr = settings.split(",");

			for (String br : aBr) {
				if (!br.contains("_") && !br.equals("")) { // no version specified
					for (String browser : availableBrowsers) {
						if (browser.startsWith(br)) {
							response.add(browser);
						}
					}
				}
				else if (availableBrowsers.contains(br) && !br.equals("")) {
					response.add(br);
				}
			}
		}
		return response;
	}
}
