package com.mrt.utils;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * 
 * @author Murat.Degirmenci
 *
 */
public class ConfigReader {
	
	private static Properties properties;
	
	static {
		try {
			String path="configuration.properties";
			FileInputStream file= new FileInputStream(path);
			
			properties=new Properties();
			properties.load(file);
			
			file.close();
		}catch (Exception e) {
			System.out.println("Properies not found "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static String getProperty (String keyWord) {
		return properties.getProperty(keyWord);
	}
	
	
	

}
