package com.mrt.utils;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * 
 * @author Murat.Degirmenci
 *
 */
import java.io.IOException;


/**
 * ConfigReader reads properties from the configuration file.
 */
public class ConfigReader {

	private static final Properties properties;

	static {
		properties = new Properties();
		String path = "configuration.properties";
		try (FileInputStream file = new FileInputStream(path)) {
			properties.load(file);
		} catch (IOException e) {
			System.out.println("Properties not found: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the value of a property by its key.
	 *
	 * @param keyWord the key of the property
	 * @return the value of the property
	 */
	public static String getProperty(String keyWord) {
		return properties.getProperty(keyWord);
	}
}

