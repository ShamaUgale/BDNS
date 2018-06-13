package com.saf.global;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class LoadProperties {

	private static Properties parameters;
	protected static Properties NPPApiConfig=null;
	private static Properties NPPApiData=null;
	private static Properties NPPApiEndpoints=null;
	private static Properties NPPApiUsers=null;
	
	public static boolean stopSuiteOnFailure=Boolean.parseBoolean(getProperty("stopSuiteOnFailure", "false"));
	public static final String REPORT_URL = getProperty("report_url", "C:/Reports");
	public static final String BROWSER = getProperty("browser", "firefox");
	public static final String VERSION = getProperty("version", "1.0");
	public static final String PLATFORM = getProperty("platform", "WINDOWS");
	public static final String HOST = getProperty("host", "localhost");
	public static final int PORT = Integer.parseInt(getProperty("port", "4444"));
	public static final boolean DEBUG = Boolean.parseBoolean(getProperty("debug", "false"));
	public static final String CAPTURE_PATH = getProperty("capture_path", "C:/Reports");
	public static final int TIMEOUT = Integer.parseInt(getProperty("timeout", "30"));
	public static final int RETRY = Integer.parseInt(getProperty("retry", "2"));
	public static final boolean DISPLAYLOCATORNAMES = Boolean.parseBoolean(getProperty("displayLocatorNames", "true"));
	public static final boolean DISPLAYLOCATORS = Boolean.parseBoolean(getProperty("displayLocators", "false"));
	public static final boolean HIGHLIGHT = Boolean.parseBoolean(getProperty("highlight", "true"));
	public static final String APP_URL = getProperty("app_url", "http://www.google.com/");//
	public static final boolean VISIBILITY_CHECK = Boolean.parseBoolean(getProperty("visibilityCheck", "true"));;
	public static final int TYPE_WAIT =Integer.parseInt(getProperty("typeWait", "1000"));;
	public static final int STEP_WAIT =Integer.parseInt(getProperty("stepWait", "0"));;
	public static final String LOCATORS_DIR_PATH = getProperty("locators_dir_path", "src/main/java/com/saf/pages/");
	public static final String DATA_DIR_PATH = getProperty("data_dir_path", "src/main/java/com/saf/pages/");

	public static final String DB_DRIVER = getProperty("db_driver", "com.mysql.jdbc.Driver");
	public static final String DB_URL = getProperty("db_url", "jdbc:mysql://localhost/NSBIZ");
	public static final String DB_DATABASE = getProperty("db_database", "testdata");
	public static final String DB_USERNAME = getProperty("db_username", "root");
	public static final String DB_PASSWORD = getProperty("db_password", "mysql");


	
	public static String getNPPApiConfigProperty(String key) {
		if (NPPApiConfig == null) {
			try {
				NPPApiConfig = new Properties();
				NPPApiConfig.load(new FileReader("API_CONFIG.properties"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return NPPApiConfig.getProperty(key);
	}
	
	public static String getNPPApiDataProperty(String key) {
		if (NPPApiData == null) {
			try {
				NPPApiData = new Properties();
				NPPApiData.load(new FileReader("API_DATA.properties"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return NPPApiData.getProperty(key);
	}
	
	public static String getNPPApiUser(String key) {
		if (NPPApiUsers == null) {
			try {
				NPPApiUsers = new Properties();
				NPPApiUsers.load(new FileReader("API_Users.properties"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return NPPApiUsers.getProperty(key);
	}
	
	public static String getNPPEnpoint(String key) {
		if (NPPApiEndpoints == null) {
			try {
				NPPApiEndpoints = new Properties();
				NPPApiEndpoints.load(new FileReader("APIEndpoints.properties"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return NPPApiEndpoints.getProperty(key);
	}
	
	public static String getProperty(String key, String defaultValue) {
		if (parameters == null) {
			try {
				parameters = new Properties();
				parameters.load(new FileReader("test.properties"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return parameters.getProperty(key, defaultValue);
	}

}
