package es.bull.testingframework.cucumber.testng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.testng.ITestContext;
import org.testng.TestListenerAdapter;

public class ExecutiveResultsReporter extends TestListenerAdapter {

	public String toImgTag(String browser) {
		
		ResultsBackend results = ResultsBackend.getInstance();
		String webcontext = results.getContext();
		return "<img src='" + webcontext + "/userContent/cucumber/" + browser + ".png'>";
	}

	@Override
	public void onFinish(ITestContext context) {

		try {
			ClassLoader classLoader = Thread.currentThread()
					.getContextClassLoader();
			InputStream template1 = classLoader
					.getResourceAsStream("dashboard_header1.html");
			InputStream template2 = classLoader
					.getResourceAsStream("dashboard_header2.html");

			File fout = new File("target/executions/dashboard.html");
			fout.getParentFile().mkdirs();
			FileOutputStream fos = new FileOutputStream(fout, true);

			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = template1.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}

			Writer out = new OutputStreamWriter(fos, "UTF8");
			PrintWriter writer = new PrintWriter(out, true);

			ResultsBackend results = ResultsBackend.getInstance();
			String webcontext = results.getContext();
			
			writer.append("<script src='" + webcontext + "/userContent/cucumber/jquery-1.11.0.min.js' type='text/javascript'></script>\n");			
			writer.append("<link rel='stylesheet' href='" + webcontext + "/userContent/cucumber/jquery.fancybox.css' type='text/css' media='screen' />\n");
		    writer.append("<script type='text/javascript' src='" + webcontext + "/userContent/cucumber/jquery.fancybox.pack.js'></script>\n");
		    writer.flush();
			
			len = 0;			
			while ((len = template2.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			
			writer.append("  <table id='results'>\n");

			ArrayList<String> browsers = results.getSortedUniqueBrowsers();
			ArrayList<String> classes = results.getSortedUniqueClasses();

			writer.append("\t\t<tr>\n");
			writer.append("\t\t\t<th/>\n");
			for (String browser : browsers) {
				writer.append("\t\t\t<th>" + toImgTag(browser.split("_")[0])
						+ " ");
				writer.append("<span class='browser'>" + browser.split("_")[1]
						+ "</span></th>\n");
			}
			writer.append("\t\t</tr>\n");

			for (String clazz : classes) {
				String feature = results.featureForClass(clazz);
				String classPackage = clazz
						.substring(0, clazz.lastIndexOf("."));
				String BUILD_URL = System.getenv().get("BUILD_URL");

				writer.append("    <tr class='results' >\n");
				writer.append("      <td class='feature'> <a class='fancybox fancybox.iframe' href='"
						+ BUILD_URL
						+ "/testngreports/"
						+ classPackage
						+ "/"
						+ clazz + "'>" + feature + "</a></td>\n");
				Integer browserCount = 1;
				for (String browser : browsers) {
					browserCount++;
					String thisResult = results.getExecutionResults(clazz,
							browser);
					String thisColor = "";
					if (thisResult.equals("-")) {
						thisColor = "rgba(175, 162, 162, 0.39)";
					} else if (thisResult.split(":")[1].startsWith("0")) { // ok
						thisColor = "rgba(153, 198, 142, 0.39)";
					} else if (thisResult.split(":")[0].startsWith("0")) { // err
						thisColor = "rgba(247, 13, 26, 0.39)";
					} else {
						thisColor = "rgba(248, 240, 49, 0.39)";
					}
					if (!thisResult.equals("-")) {
						thisResult = thisResult.split("/")[1];
					}
					writer.append("      <td style='background-color:"
							+ thisColor + ";'>" + thisResult + "</td>\n");
				}
				writer.append("    </tr>\n");
				writer.append("    <tr class='detailedresults'><td class='nomargin' colspan='"
						+ browserCount + "'>\n");

				writer.append("<table class='details' >\n");
				for (String data : results.getUniqueData(clazz)) {
					writer.append("<tr>\n");
					writer.append("<td class='feature details' >\n");
					writer.append(data + "\n");
					writer.append("</td>\n");
					for (String browser : browsers) {
						writer.append("<td>\n");
						writer.append(results.getExecutionResults(clazz, data,
								browser, feature) + "\n");
						writer.append("</td>\n");
					}
					writer.append("</tr>\n");
				}
				writer.append("</table>\n");
				writer.append("    </td></tr>\n");
			}

			writer.append("  </table>\n");
			writer.append("</body>");

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
