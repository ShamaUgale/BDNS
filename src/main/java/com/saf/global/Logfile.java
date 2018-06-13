package com.saf.global;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

public class Logfile extends TestContext{
	public static Category log = writeLog();
	

	/**
	 * 
	 * @return log object to Super class
	 */
	@SuppressWarnings("deprecation")
	public static Category writeLog() {
		initializeLogger();
		log = Category.getInstance(Logfile.class);
		log.info("Log4JExample - leaving the constructor ...<BR>");
		return log;
	}

	/**
 *  
 */
	private static void initializeLogger() {
		Properties logProperties = new Properties();
		String path = "", finalPath = "";
		Global global = new Global();
		try {
			// load our log4j properties / configuration file
			logProperties.load(new FileInputStream("filePaths.properties"));
			path = logProperties.getProperty("logfile");
			finalPath = global.appendProjName(path);
			logProperties.load(new FileInputStream(finalPath));
			PropertyConfigurator.configure(logProperties);
			// log.info("Logging initialized.");
		} catch (IOException e) {
			throw new RuntimeException("Unable to load logging property " + finalPath);
		}
	}

	public static void main(String ar[]) {
		log.info("Test Program");
	}


	
}