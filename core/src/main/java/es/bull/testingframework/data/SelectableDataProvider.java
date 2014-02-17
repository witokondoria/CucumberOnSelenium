package es.bull.testingframework.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import com.google.common.collect.Lists;

public class SelectableDataProvider extends CommonDataProvider {

	@DataProvider
	public static Iterator<String[]> selectableBrowsers(ITestContext context,
			Constructor<?> testConstructor) throws Exception {

		new PropertiesDataExtractor();
		
		ArrayList<String> browsers = selectedBrowsers(testConstructor.getName());

		List<String[]> lData = Lists.newArrayList();

		for (String s : browsers) {
			lData.add(new String[] { s });
		}

		if (lData.size() == 0) {
			lData.add(new String[] { "" });
		}
		
		return lData.iterator();
	}

}
