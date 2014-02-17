package es.bull.testingframework.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesDataExtractor {

	public PropertiesDataExtractor() throws IOException {

		Properties props = new Properties();

		InputStream stream = new FileInputStream("./environment.properties");
		props.load(stream);
			
		String grid = props.getProperty("grid", "localhost:4444");		

		String proxy = props.getProperty("proxy", "");
		
		System.setProperty("selenium.proxy", proxy);
		System.setProperty("selenium.gridHub", grid);
	}
}
