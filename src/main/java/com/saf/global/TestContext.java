package com.saf.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.WebDriver;

public class TestContext {
//	private WebDriver driver = null;
	private WebDriver driver = null;
	private List<String> assertList = new ArrayList<String>();
	private HashMap<String, Object> testData = new HashMap<String, Object>();

	// Thread local variable containing each thread's Webdriver object
	private static final ThreadLocal<TestContext> thread = new ThreadLocal<TestContext>() {
		protected TestContext initialValue() {
			return new TestContext();
		}
	};

	protected WebDriver getDriver() {
		return getWebDriver();
	}

	protected TestContext get() {
		return thread.get();
	}

	protected String getString(String key) {
		try {
			key = (String) thread.get().testData.get(key);
			if (key == null) {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
		return key;
	}

	protected Object getObject(String key) {
		return thread.get().testData.get(key);
	}

	protected void put(String key, Object value) {
		thread.get().testData.put(key, value);
	}

	protected List<String> getAssertList() {
		return thread.get().assertList;
	}

	protected void remove() {
		thread.remove();
	}

	public synchronized int getI() {
		Integer id = (Integer) getObject("Step");
		if (id == null) {
			put("Step", 1);
			return 1;
		}
		put("Step", id + 1);
		return id;
	}

	public int getRetryCount(String methodName) {
		Integer retryCount = (Integer) getObject(methodName);
		if (retryCount == null) {
			put(methodName, 0);
			return 0;
		}
		return retryCount;
	}

	public void increamentRetryCount(String methodName) {
		put(methodName, getRetryCount(methodName) + 1);
	}

	public void setAssertList(List<String> assertList) {
		thread.get().assertList = assertList;
	}

	public WebDriver getWebDriver() {
		return thread.get().driver;
	}

	public void setWebDriver(WebDriver driver) {
		thread.get().driver = driver;
	}

	public static String getProperty(String key, String defaultValue) {
		return LoadProperties.getProperty(key, defaultValue);
	}

	//public static String getProperty(String key) {
	//	return LoadProperties.getProperty(key);
//	}

}
