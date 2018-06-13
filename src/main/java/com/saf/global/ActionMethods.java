package com.saf.global;

/*
 * @author - msonar
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;

import com.saf.util.DataHandler;
import com.saf.util.FileOperation;

public class ActionMethods extends Logfile {
	private static final String PREFIX = ">        ";
	private static final String error1 = "//*[contains(@class,'error')]";
	private static final String error2 = "//*[contains(@class,'red')]";
	private static boolean displayLocatorsInReport = false;
	//private static boolean stopForSoftAssertions = LoadProperties.STOP_FOR_SOFT_ASSERTIONS;
	private static boolean highlight = LoadProperties.HIGHLIGHT;

	public static void resetHighlight() {
		ActionMethods.highlight = false;
	}

	public static void setHighlight() {
		ActionMethods.highlight = true;
	}

	private static boolean showActionLogs = true;
	private boolean showVerifications = true;
	private boolean takeScreenshots = true;
	private String userDir = System.getProperty("user.dir");
	private String snapshotDir = "C:/Snapshots/"
			+ userDir
					.substring(userDir.lastIndexOf('\\') + 1, userDir.length());
	private static int snapshot_counter = 0;
	private boolean prevVisibilityCheck = true;
	protected int timeout = LoadProperties.TIMEOUT;
	private boolean visibilityCheck = LoadProperties.VISIBILITY_CHECK;
	public String ALPHA_NUM = "0123456789abcdefghijklmnopqrstuvwxyz";
	public String NUMERICS = "0123456789";
	protected ArrayList<String> excludedMethodNames = new ArrayList<String>();
	public static DateFormat dateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmssSSS");
	public static boolean status = true;
	public static Properties objects = new Properties();

	public ActionMethods() {
		new File(snapshotDir).mkdirs();
		Method[] methods;
		try {
			methods = Class.forName("com.saf.global.ActionMethods")
					.getMethods();
			for (Method method : methods) {
				excludedMethodNames.add(method.getName());
			}
			methods = Class.forName("com.saf.global.Global").getMethods();
			for (Method method : methods) {
				excludedMethodNames.add(method.getName());
			}

		} catch (Exception e) {
		}
	}

	public static String addStrings(String string1, String string2) {
		int sum = Integer.parseInt(string1.trim())
				+ Integer.parseInt(string2.trim());
		return Integer.toString(sum);
	}

	/**
	 * Performs calculations on Currency Values
	 * 
	 * @param v1
	 *            - First operand. Currency symbol should match with second
	 *            operand
	 * @param v2
	 *            - Second Operand
	 * @param opr
	 *            - operator can be +,-,*,/,%,+%,-%
	 */
	public String Calculate(String v1, String v2, String opr) {
		try {
			float a = 0, b = 0, c = 0;
			String output = "";
			v1 = v1.trim();
			v2 = v2.trim();
			String prefix;
			if (v1.contains(" ")) {
				prefix = v1.substring(0, v1.trim().lastIndexOf(" "));
			} else {
				prefix = v1.substring(0, 1);
			}
			a = extractNumber(v1, prefix);
			b = extractNumber(v2, prefix);
			if (opr.equals("+")) {
				c = a + b;
			}
			if (opr.equals("*")) {
				c = a * b;
			}
			if (opr.equals("/")) {
				c = a / b;
			}
			if (opr.equals("%")) {
				c = a * b / 100;
			}
			if (opr.equals("+%")) {
				c = a + a * b / 100;
			}
			if (opr.equals("-%")) {
				c = a - a * b / 100;
			}
			if (opr.equals("-")) {
				c = a - b;
			}
			if (c != 0) {
				output = prefix + new DecimalFormat("#,###.00").format(c);
			} else {
				output = prefix + "0.00";
			}
			return output;

		} catch (Exception e) {
			log("Action: Calculate   Status: Failed    "
					+ "Message: Exception while calculating values '" + v1
					+ " " + opr + " " + v2 + "'. Exception: " + e.getMessage(),
					TestStepType.ERRORMESSAGE);
			return "";

		}

	}

	private static float extractNumber(String v1, String prefix) {
		float n;
		n = Float.parseFloat(v1.trim().replace(prefix, "").replace(",", "")
				.trim());
		return n;
	}

	public static void logToFile(String fileName, String msg) {
		try {
			BufferedWriter f = new BufferedWriter(new FileWriter(
					LoadProperties.CAPTURE_PATH + File.separator + fileName
							+ ".txt", true));
			f.write(msg + '\n');
			f.close();
		} catch (IOException e) {
			System.out.println("Error while writing path.");
		}
	}

	public static String dateTime() {
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	}

	public static String Computername() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName().replace("-", "")
				.substring(0, 6);
	}

	protected void fail(String msg) {
		logWithScreenshot("<font color='red'>" + msg + "</font>",
				TestStepType.ERRORMESSAGE);
		status = false;
		throw new Error(msg);
	}

	public void changeAttribute(String locator) {
		WebElement element = waitForElement(locator);
		try {
			if (element != null) {
				setAttribute(element, "onclick",
						element.getAttribute("onchange"));
			}
		} catch (Exception e) {
			fail("Exception while setting attribute");
		}
	}

	/**
	 * Selects a check box if not already selected
	 */
	public void check(String locator) {
		WebElement element = waitForElement(locator);
		if (element != null) {
			try {
				if (!element.isSelected()) {
					// element.click();
					click(locator);

				}
				wait(500);
				if (!element.isSelected()) {
					// element.click();
					click(locator);

				}

			} catch (Exception e) {
				fail("Exception while check: " + e.getMessage());
			}
			log("Check \"" + getLocatorName(locator) + "\"",
					TestStepType.INNER_SUBSTEP);
		}

	}

	/**
	 * Click and Wait for 5 Seconds by default
	 */
	public boolean clickAndWait(String locator) {
		return clickAndWait(locator, 5);

	}

	/**
	 * Click and Wait for specified seconds
	 */
	public boolean clickAndWait(String locator, int wait) {
		if (click(locator)) {
			wait(wait * 1000);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Click on Element if Present even though isDisplayed() returns false and
	 * Wait for specified seconds Doesn't evaluate isDisplayed() while
	 * findElement. Use when the element is displayed visually but its not
	 * Visible to WebDriver with isDisplayed()
	 */
	public void clickAndWaitNoVisible(String locator) {
		resetVisibilityCheckRemember();
		clickAndWait(locator);
		setVisibilityCheckRemember();
	}

	/**
	 * Click if specified element is present with in specified timeout, else no
	 * action.
	 */
	public boolean clickIfPresent(String locator, int timeoutForElementPresent) {
		if (waitForElementWithNameNoReport(locator, timeoutForElementPresent)) {
			return click(locator);
		} else {
			return false;
		}
	}

	/**
	 * Click if specified element is present with in specified time in seconds
	 * before click, else no action and wait for specified seconds after click
	 */
	public boolean clickIfPresentAndWait(String locator,
			int waitTimeBeforeClick, int waitTimeAfterClick) {
		if (clickIfPresent(locator, waitTimeBeforeClick)) {
			wait(waitTimeAfterClick * 1000);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Click on element using java script Use when normal click doesn't work
	 */
	public void clickJ(String locator) {
		executeScript("arguments[0].click()", locator);
		log("ClickJ \"" + getLocatorName(locator) + "\"",
				TestStepType.INNER_SUBSTEP);

	}

	/**
	 * Click on element using java script and waits for specified seconds. Use
	 * when normal click doesn't work
	 * 
	 */
	public void clickJAndWait(String locator, int timeout) {
		clickJ(locator);
		wait(timeout * 1000);
	}

	/**
	 * Click on element using java script and waits for specified seconds. Use
	 * when normal click doesn't work
	 * 
	 */
	public void clickJAndWaitInvisible(String locator, int timeout) {
		resetVisibilityCheckRemember();
		clickJ(locator);
		setVisibilityCheckRemember();
		wait(timeout * 1000);
	}

	/**
	 * Click on element using java script and waits for 5 seconds. Use when
	 * normal click doesn't work
	 * 
	 */
	public void clickJAndWait(String locator) {
		clickJ(locator);
		wait(5000);
	}

	/**
	 * Execute the javascript code using javascript driver on particular web
	 * element
	 * 
	 * @param javascriptToExecute
	 * @param locator
	 */
	public void executeScript(String javascriptToExecute, String locator) {
		try {
			WebElement webElement = waitForElement(locator);
			((JavascriptExecutor) getDriver()).executeScript(
					javascriptToExecute, webElement);
		} catch (WebDriverException webDrivExc) {
			if (LoadProperties.DEBUG) {
				webDrivExc.printStackTrace();
			}
			log("Exception on Javascript Execution occured while performing executeScript",
					TestStepType.INTERNALERRORMESSAGE);
		}
	}

	/**
	 * Click link by specified text
	 */
	public boolean clickLinkByText(String linkText) {
		return click("//a[contains(text(),'" + linkText + "')]");
	}

	/**
	 * Click link by specified text and wait for 5 secs
	 */
	public void clickLinkByTextAndWait(String linkText) {
		clickLinkByText(linkText);
		wait(5000);
	}

	/**
	 * Click link by specified text and wait for specified secs
	 */
	public void clickLinkByTextAndWait(String linkText, int secsToWait) {
		clickLinkByText(linkText);
		wait(secsToWait * 1000);
	}

	/**
	 * - Clicks on element specified by locator. - Implicitly waits for the
	 * element to be present before click, so no need to explicitly wait for
	 * element using AjaxCondition - Logs to report on successful click - Logs
	 * to report if element not present - Waits for 5 secs if element just
	 * appeared before 1 sec, to allow complete page load
	 * 
	 * @param locator
	 * @return
	 */
	public boolean click(String locator) {
		WebElement element = waitForElement(locator);
		if (element != null) {
			try {
				element.click();
				// wait(500);
				// element.click();
				log("Click \"" + getLocatorName(locator) + "\"",
						TestStepType.INNER_SUBSTEP);
			} catch (Error e) {
				e.printStackTrace();
				return true;
			} catch (TimeoutException e) {
				e.printStackTrace();
				return true;
			} catch (StaleElementReferenceException e) {
				element = waitForElement(locator);
				element.click();
				log("Click \"" + getLocatorName(locator) + "\"",
						TestStepType.INNER_SUBSTEP);
			} catch (Exception e) {
				try {
					executeScript("arguments[0].click()", locator);
					return true;
				} catch (Exception e1) {
					e1.printStackTrace();
					fail("Unable to Click Element :"
							+ getLocatorName(locator)
							+ " Error:"
							+ (e.getMessage().length() > 100 ? e.getMessage()
									.substring(0, 99) + "..." : e.getMessage()));
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public void clickM1(String locator) {
		resetVisibilityCheckRemember();
		click(locator);
		setVisibilityCheckRemember();
	}

	/**
	 * Click and wait for 5 seconds until element is present
	 */
	public void clickUntilPresent(String locator) {
		while (waitForElementWithNameNoReport(locator, 5)) {
			clickAndWait(locator);
		}
	}

	/**
	 * Log message and stop Script with fail status
	 */
	public void failLog(String msg) {
		log(msg, TestStepType.ERRORMESSAGE);
		fail("Test Failed");
	}

	protected WebElement findElementM(String locator) {
		WebElement element = null;
		// WebElement element;
		if (locator.startsWith("/") || locator.startsWith("xpath=")
				|| locator.contains("//")) {
			element = searchElement(By.xpath(locator.replace("xpath=", "")));
		} else if (locator.startsWith("link=")) {
			element = searchElement(By.linkText(locator.replace("link=", "")));
		} else if (locator.startsWith("linkp=")) {
			element = searchElement(By.partialLinkText(locator.replace(
					"linkp=", "")));
		} else if (locator.startsWith("id=")) {
			element = searchElement(By.id(locator.replace("id=", "")));
		} else if (locator.startsWith("name=")) {
			element = searchElement(By.name(locator.replace("name=", "")));
		} else if (locator.startsWith("tag=")) {
			element = searchElement(By.tagName(locator.replace("tag=", "")));
		} else if (locator.startsWith("css=")) {
			element = searchElement(By.cssSelector(locator.replace("css=", "")));
		} else {
			element = searchElement(By.id(locator));
			if (element == null) {
				element = searchElement(By.name(locator));
				if (element == null) {
					element = searchElement(By.linkText(locator));
					if (element == null) {
						element = searchElement(By.partialLinkText(locator));
					}
				}
			}
		}
		return element;
	}

	/**
	 * Returns value of specified attribute for specified locator
	 */
	public String getAttribute(String locator, String attributeName) {
		WebElement element = waitForElement(locator);
		String attributeValue = "";
		if (element != null) {
			try {
				attributeValue = element.getAttribute(attributeName);
				log("\"" + attributeName + "\" attribute for \""
						+ getLocatorName(locator) + "\" is \"" + attributeValue
						+ "\"", TestStepType.DATA_CAPTURE);
			} catch (Exception e) {
				fail("Unable to get attribute " + attributeName + "for \""
						+ getLocatorName(locator) + "\"");
			}

		}
		return attributeValue;
	}

	/**
	 * Returns value of specified cookieName
	 */
	public String getCookieValueByName(String cookieName) {
		try {
			return getDriver().manage().getCookieNamed(cookieName).getValue();
		} catch (Exception e) {
			if (LoadProperties.DEBUG) {
				e.printStackTrace();
			}
			log("Unable to fetch Cookie the cookie " + cookieName,
					TestStepType.INNER_SUBSTEP);
		}
		return null;
	}

	public String getLocatorName(String locator) {
		if (LoadProperties.DISPLAYLOCATORNAMES == false) {
			return locator;
		}

		return objects.getProperty(locator, locator);
	}

	public String getLocatorValue(String locator) {
		if (displayLocatorsInReport) {
			return locator;
		}
		return "";
	}

	/**
	 * Returns url of current page.
	 */
	public String getPageUrl() {
		return getDriver().getCurrentUrl();
	}

	/**
	 * Returns text of specified locator
	 */
	public String getText(String locator) {
		return getText(locator, timeout);
	}

	/**
	 * Returns text of specified locator
	 */
	public String getText(String locator, int timeout) {
		WebElement element;
		try {
			element = waitForElement(locator, timeout);
		} catch (Exception e) {
			return "";
		}
		String text = "";
		try {
			text = element.getText().trim();

		} catch (Exception e) {
			fail("Unable to get text from Element :" + getLocatorName(locator));
		}
		if (locator.equals("//html")) {
			log("GetText from Element: \"" + getLocatorName(locator) + "\"",
					TestStepType.INNER_SUBSTEP);
		} else {
			log("GetText from Element: \"" + getLocatorName(locator) + "\", "
					+ text, TestStepType.INNER_SUBSTEP);
		}
		return text;
	}

	private void highlight(WebElement element) {
		try {
			if (highlight) {
				JavascriptExecutor js = (JavascriptExecutor) getDriver();
				js.executeScript(
						"arguments[0].setAttribute('style', arguments[1]);",
						element, "border: 2px solid red;");

			}
		} catch (Exception e) {
			log("Highlight Error");
		}

	}

	public void incrementCounter(String msg) {
		try {
			BufferedWriter f = new BufferedWriter(new FileWriter(
					"/var/lib/jenkins/jobs/registerCounter.txt", true));
			f.write("\r\n" + new Date() + ":" + msg);
			f.close();

		} catch (Exception e) {
			slog("Error while updating register counter.");
		}

	}

	/**
	 * Returns true if element present
	 */
	public boolean isElementPresent(String loc) {
		if (findElementM(loc) != null) {
			return true;
		} else {
			return false;
		}
	}

	public static String toSentence(String str) {
		ArrayList<Character> newStr = new ArrayList<Character>();
		newStr.add(Character.toUpperCase(str.charAt(0)));
		for (int i = 1; i < str.length(); i++) {
			char c = str.charAt(i);
			if (Character.isLetter(c)) {
				if (Character.isUpperCase(c)
						&& !Character.isUpperCase(str.charAt(i - 1))) {
					newStr.add(' ');
					newStr.add(c);
				} else if (i < str.length() - 1 && Character.isUpperCase(c)
						&& Character.isLowerCase(str.charAt(i + 1))) {
					newStr.add(' ');
					newStr.add(c);
				} else if (str.charAt(i) == '_') {
					newStr.add(' ');
				} else if (str.charAt(i - 1) == '_') {
					newStr.add(Character.toUpperCase(c));
				} else {
					newStr.add(c);
				}

			}
		}
		return newStr.toString().replace(", ", "").replace("_", " ")
				.replace("  ", " ");
	}

	public void logM(String methodName) {
		put("currentMethod", methodName);
		log("<b>Step: " + toSentence(methodName) + "</b>",
				TestStepType.MAIN_STEP);

	}

	public void log(String msg) {
		log(msg, TestStepType.INNER_SUBSTEP);
	}

	public void log(String msg, int innerSubstep) {
		String methodName = new Throwable().getStackTrace()[1].getMethodName();
		for (int i = 2; excludedMethodNames.contains(methodName); i++) {
			methodName = new Throwable().getStackTrace()[i].getMethodName();
		}
		if (!getString("currentMethod").equals(methodName)) {
			logM(methodName);
		}
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		switch (innerSubstep) {
		case TestStepType.MAIN_STEP:
			logC(msg, innerSubstep);
			break;
		case TestStepType.ERRORMESSAGE:
		case TestStepType.ERRORMESSAGEDETAILS:
		case TestStepType.EXCEPTION:
		case TestStepType.INTERNALERRORMESSAGE:
			get().put("testStatus", "false");
			getAssertList().add("FAIL");
			status = false;
			logWithScreenshot("<font color='red'>" + PREFIX + msg + "</font>",
					innerSubstep); // log
			// with
			// screenshot
			// for
		//	if (stopForSoftAssertions) {
		//		fail("Soft Assertion Failure : " + msg);
		//	}
			// failed cases
			break;
		case TestStepType.VERIFICATION_RESULT:
			logC(PREFIX + "Step-" + getI() + ":" + msg, innerSubstep); // log
																		// with
																		// screenshot
																		// for
			break;
		default:
			if (showActionLogs) {
				logC(PREFIX + "Step-" + getI() + ":" + msg, innerSubstep);
			}
			break;
		}
	}

	private void logC(String msg, int innerSubstep) {
		logC(msg);
	}

	private void logC(String msg) {
		reporter(msg, "");
	}

	public void reporter(String sValue, String sValue1) {
		// System.setProperty("org.uncommons.reportng.escape-output", "false");
		log.info(sValue + "," + sValue1);
		Reporter.log(sValue + sValue1 + "<br>");

	}

	/**
	 * Log msg in report with link to Screenshot
	 */
	public void logWithScreenshot(String msg) {
		logWithScreenshot(msg, TestStepType.VERIFICATION_STEP);
	}

	private String getError() {
		try {
			return "\nError:  "
					+ getDriver().findElement(By.xpath(error1)).getText();
		} catch (Exception e) {
			try {
				return "\nError:  "
						+ getDriver().findElement(By.xpath(error2)).getText();
			} catch (Exception e2) {
				return "";

			}
		}
	}

	/**
	 * Log msg in report as per testSteptype with link to Screenshot
	 */
	public synchronized void logWithScreenshot(String msg, int testStepType) {
		String snapshotName = "Snapshot_" + dateFormat.format(new Date())
				+ Integer.toString(++snapshot_counter) + LoadProperties.BROWSER;
		WebDriver driver;
		String image = null;
		if (takeScreenshots) {
			try {
				driver = new Augmenter().augment(getDriver());
				image = ((TakesScreenshot) driver)
						.getScreenshotAs(OutputType.BASE64);
				byte[] imageData = Base64.decodeBase64(image.getBytes());
				FileOutputStream out = new FileOutputStream(snapshotDir + "/"
						+ snapshotName + ".png");
				out.write(imageData);
				out.close();
			} catch (Exception ex) {
				if (ex.getMessage().contains("communicating")) {
					FileOperation.flog("status.html", msg);
					logC(PREFIX + "Step-" + getI() + ":" + msg, testStepType);
					throw new Error(ex.getMessage());
				} else {
					logC(PREFIX + "Step-" + getI() + ":" + "Snapshot Error : "
							+ ex.getMessage(), TestStepType.EXCEPTION);

				}
			}
			try {
				FileWriter out = new FileWriter(snapshotDir + "/"
						+ snapshotName + ".html");
				out.write(getDriver().getPageSource());
				out.close();
			} catch (Exception ex) {
				if (ex.getMessage().contains("communicating")) {
					FileOperation.flog("status.html", msg);
					logC(PREFIX + "Step-" + getI() + ":" + msg, testStepType);
					throw new Error(ex.getMessage());
				} else {
					logC(PREFIX + "Step-" + getI() + ":"
							+ "Page Source Error : " + ex.getMessage(),
							TestStepType.EXCEPTION);
				}
			}
			msg = msg + " <a href=\"" + "file:/" + snapshotDir + "/"
					+ snapshotName + ".png\" target=_blank>Snapshot</a>"
					+ " <a href=\"" + "file:/" + snapshotDir + "/"
					+ snapshotName + ".html" + "\" target=_blank>html</a>";

		}
		logC(PREFIX + "Step-" + getI() + ":" + msg, testStepType);

		switch (testStepType) {
		case TestStepType.ERRORMESSAGE:
		case TestStepType.ERRORMESSAGEDETAILS:
		case TestStepType.EXCEPTION:
		case TestStepType.INTERNALERRORMESSAGE:
			get().put("testStatus", "false");
			getAssertList().add("FAIL");
			FileOperation.flog("status.html", msg);
			//if (stopForSoftAssertions) {
			//	status = false;
				throw new Error(msg);
			//}
		}
	}

	/**
	 * Navigate to specified url with log in report
	 */
	public void navigate(String url) {
		log("Navigate to Url : " + url, TestStepType.INNER_SUBSTEP);
		getDriver().navigate().to(url);
	}

	/**
	 * Navigate back with log in report
	 */
	public void navigateBack() {
		log("Navigate Back", TestStepType.INNER_SUBSTEP);
		getDriver().navigate().back();
	}

	/**
	 * Navigate forward with log in report
	 */
	public void navigateForward() {
		log("Navigate Forward", TestStepType.INNER_SUBSTEP);
		getDriver().navigate().forward();
	}

	/**
	 * Refresh page with log in report
	 */
	public void refreshPage() {
		try {
			log("Refresh Page", TestStepType.INNER_SUBSTEP);
			getDriver().navigate().refresh();
			pause(2);
		} catch (Exception e) {

		}
	}

	/**
	 * Set Visibility checking while finding element to false
	 */
	public void resetVisibilityCheck() {
		this.visibilityCheck = false;
	}

	/**
	 * Set Visibility checking while finding element as before calling
	 * resetVisibilityCheck()
	 */
	public void resetVisibilityCheckRemember() {
		this.prevVisibilityCheck = visibilityCheck;
		this.visibilityCheck = false;
	}

	/**
	 * Scroll to specified element
	 */
	public void scrollToM(String locator) {
		WebElement element = waitForElement(locator);
		try {
			int y = element.getLocation().getY();
			if (y > 800) {
				y = 800;
			}
			((JavascriptExecutor) getDriver()).executeScript(
					"window.scrollBy(0," + y + ");", new Object[0]);
		} catch (WebDriverException wdexception) {
			if (LoadProperties.DEBUG) {
				wdexception.printStackTrace();
			}
			log("Javascript Execution exception occured while performing scrollTo action",
					TestStepType.INTERNALERRORMESSAGE);
		}
	}

	private WebElement searchElement(By by) {
		WebElement element = null;
		int count = 0;
		List<WebElement> elements;
		try {
			elements = getDriver().findElements(by);
			for (Iterator<WebElement> iterator = elements.iterator(); iterator
					.hasNext();) {
				element = (WebElement) iterator.next();
				try {
					if (element.isDisplayed()) {
						highlight(element);
						return element;
					}
				} catch (Exception e) {
				}

			}
			if (element.isDisplayed()) {
				// highlight(element);
			} else {
				if (visibilityCheck) {
					return null;
				}
				return elements.get(0);
			}
		} catch (Exception e) {
			try {
				if (count < 1 && e.getMessage().contains("alert")) {
					acceptAlert();
					count++;
					searchElement(by);
				}
			} catch (Exception e1) {

			}
			return null;
		}

		return element;
	}

	public String acceptAlert() {
		return acceptAlert(timeout / 2);
	}

	public String acceptAlert(int timeout) {
		for (int seconds = 0;; seconds++) {
			if (seconds >= timeout) {
				return "";
			} else {
				try {
					String alertText = getDriver().switchTo().alert().getText()
							.trim();
					getDriver().switchTo().alert().accept();
					return alertText;
				} catch (Exception e) {

				}
				wait(1000);
			}
		}

	}

	/**
	 * Select drop down with specified value value can be "id=zzz", option text,
	 * option value, option id Select value with first index if option with
	 * specified value not found Skip selection if value is "."
	 */
	public void select(String locator, String value) {
		if (value.equals(".")) {
			return; // if data value equals to "." then that wont be typed.
		}
		WebElement element = waitForElement(locator);
		if (element != null) {
			if (value.contains("id=")) {
				new Select(element).selectByIndex(Integer.parseInt(value.trim()
						.replace("id=", "")));
			} else if (value.contains("text=")) {
				waitForElementVisibleNoReport("//option[(text()='"
						+ value.trim().replace("text=", "") + "')]",
						timeout / 2);
				try {
					new Select(element).selectByVisibleText(value.trim()
							.replace("text=", ""));
				} catch (StaleElementReferenceException e) {
					element = waitForElement(locator);
					new Select(element).selectByVisibleText(value.trim()
							.replace("text=", ""));
				}
				log("Select \"" + getLocatorName(locator)
						+ "\" with option text:" + value,
						TestStepType.INNER_SUBSTEP);
			} else if (waitForElementVisibleNoReport("//option[(text()='"
					+ value + "')]", 1)) {
				new Select(element).selectByVisibleText(value);
				log("Select \"" + getLocatorName(locator)
						+ "\" with option text:" + value,
						TestStepType.INNER_SUBSTEP);
			} else if (value.contains("value=")) {
				waitForElementVisibleNoReport("//option[contains(@value,'"
						+ value.trim().replace("value=", "") + "')]",
						timeout / 2);
				new Select(element).selectByValue(value.trim().replace(
						"value=", ""));
				log("Select \"" + getLocatorName(locator)
						+ "\" with option value:" + value,
						TestStepType.INNER_SUBSTEP);
			} else if (waitForElementVisibleNoReport(
					"//option[contains(@value,'" + value + "')]", 1)) {
				new Select(element).selectByValue(value);
				log("Select \"" + getLocatorName(locator)
						+ "\" with option value:" + value,
						TestStepType.INNER_SUBSTEP);
			} else if (waitForElementVisibleNoReport("//option[contains(@id,'"
					+ value + "')]", 1)) {
				new Select(element).selectByIndex(Integer.parseInt(value));
				log("Select \"" + getLocatorName(locator)
						+ "\" with option id:" + value,
						TestStepType.INNER_SUBSTEP);
			} else if (value.contains("textp=")) {
				waitForElementVisibleNoReport("//option[(contains(text(),'"
						+ value.trim().replace("textp=", "") + "'))]",
						timeout / 2);
				new Select(element)
						.selectByVisibleText(getText("//option[(contains(text(),'"
								+ value.trim().replace("textp=", "") + "'))]"));
				log("Select \"" + getLocatorName(locator)
						+ "\" with option text:" + value,
						TestStepType.INNER_SUBSTEP);
			} else if (waitForElementVisibleNoReport(
					"//option[(contains(text(),'" + value + "'))]", 1)) {
				new Select(element)
						.selectByVisibleText(getText("//option[(contains(text(),'"
								+ value + "'))]"));
				log("Select \"" + getLocatorName(locator)
						+ "\" with option text:" + value,
						TestStepType.INNER_SUBSTEP);
			} else {
				new Select(element).selectByIndex(1);
				log("Select \"" + getLocatorName(locator)
						+ "\" with first index", TestStepType.INNER_SUBSTEP);
			}
		}
	}

	/**
	 * Select drop down with specified value Skip selection if value is "."
	 */
	public void selectValue(String locator, String value) {
		if (value.equals(".")) {
			return; // if data value equals to "." then that wont be typed. //to
					// reuse filling methods
		}
		WebElement element = waitForElement(locator);
		if (element != null) {
			new Select(element).selectByValue(value);
			log("Selected Element: \"" + getLocatorName(locator)
					+ "\" with option value:" + value,
					TestStepType.INNER_SUBSTEP);
		}
	}

	/**
	 * Set the attribute value of specified name with specified value for
	 * specified element
	 */
	public void setAttribute(WebElement element, String attributeName,
			String attributeValue) {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		js.executeScript("arguments[0].setAttribute('" + attributeName
				+ "', arguments[1]);", element, attributeValue);

	}

	/**
	 * Set visibility checking before finding element to true
	 */
	public void setVisibilityCheck() {
		this.visibilityCheck = true;
	}

	/**
	 * Set visibility checking before finding element to specified value
	 * true/false
	 */
	public void setVisibilityCheck(boolean visibilityCheck) {
		this.prevVisibilityCheck = this.visibilityCheck;
		this.visibilityCheck = visibilityCheck;
	}

	/**
	 * Set visibility checking before finding element to previous value before
	 * calling resetVisiblityCheckRemember()
	 */
	public void setVisibilityCheckRemember() {
		this.visibilityCheck = this.prevVisibilityCheck;
	}

	/**
	 * Log msg to console
	 */
	public void slog(String msg) {
		System.out.println(msg);

	}

	/**
	 * Stop driver
	 */
	public void stop() {

		if (getDriver() != null) {
			getDriver().quit();
		}
	}

	/**
	 * Type specified text in element Skip to type if text="."
	 */
	public WebElement type(String locator, String text) {
		if (text.equals(".")) {
			return null;
		}
		WebElement element = waitForElement(locator);
		if (element != null) {
			try {
				element.clear();
				element.sendKeys(text);
				wait(LoadProperties.TYPE_WAIT);
				log("Type \"" + text + "\" in \"" + getLocatorName(locator)
						+ "\"", TestStepType.INNER_SUBSTEP);
			} catch (Exception e) {
				try {
					element.sendKeys(text);
					wait(LoadProperties.TYPE_WAIT);
					log("Type Keys  \"" + text + "\" in \""
							+ getLocatorName(locator) + "\"",
							TestStepType.INNER_SUBSTEP);
				} catch (Exception e1) {
					try {
						setValue(element, text);
						wait(LoadProperties.TYPE_WAIT);
						log("Set Value \"" + text + "\" in \""
								+ getLocatorName(locator) + "\"",
								TestStepType.INNER_SUBSTEP);
					} catch (Exception e2) {
						fail("Unable to type in \"" + getLocatorName(locator)
								+ "\"" + " Error: " + e.getMessage());
					}
				}
			}
		}
		return element;
	}

	private void setValue(WebElement element, String text) {
		try {
			setAttribute(element, "value", text);
		} catch (Exception e) {
			fail("Unable to set value for \"" + element.getText() + "\""
					+ " Error: " + e.getMessage());
		}
	}

	public void setValue(String locator, String text) {
		WebElement element = waitForElement(locator);
		try {
			setAttribute(element, "value", text);
		} catch (Exception e) {
			fail("Unable to set value for \"" + getLocatorName(locator) + "\""
					+ " Error: " + e.getMessage());
		}
	}

	/**
	 * Un-check checkbox specified by locator
	 */
	public void uncheck(String locator) {
		WebElement element = waitForElement(locator);
		if (element != null) {
			try {
				if (element.isSelected()) {
					element.click();
				}
			} catch (Exception e) {
				fail("Exception while check");
			}
			log("UnCheck \"" + getLocatorName(locator) + "\"",
					TestStepType.INNER_SUBSTEP);
		}

	}

	/**
	 * Verify attribute Value of specified attribute matches expected value Log
	 * verification result to report
	 */
	public void verifyAttribute(String locator, String attributeName,
			String expectedValue) {
		String actualValue = getAttribute(locator, attributeName);
		if (actualValue.contains(expectedValue)) {
			log("Verified Expected Attribute value: " + expectedValue
					+ " for Element: " + getLocatorName(locator)
					+ " attribute: " + attributeName,
					TestStepType.VERIFICATION_RESULT);
		} else {
			log("Incorrect Attribute, Expected : " + expectedValue
					+ " \nActual: " + actualValue + " for Element: "
					+ getLocatorName(locator) + " attribute: " + attributeName,
					TestStepType.ERRORMESSAGE);
		}

	}

	/**
	 * Verify check box value of specified locator matches expected
	 * value(true/false) Log verification result to report
	 */
	public boolean verifyChecked(String locator, boolean expectedValue) {
		if (waitForElement(locator).isSelected()) {
			if (!expectedValue) {
				log("\"" + getLocatorName(locator) + "\" is selected",
						TestStepType.ERRORMESSAGE);
				return false;
			}
		} else {
			if (expectedValue) {
				log("\"" + getLocatorName(locator) + "\" is NOT selected",
						TestStepType.ERRORMESSAGE);
				return false;
			}
		}
		return false;
	}

	/**
	 * Verifies Current Page Url matches with expectedUrl Log Verification
	 * result to report if fail
	 * 
	 * @return
	 */
	public boolean verifyPageUrl(String expectedUrl) {
		String actualUrl = getPageUrl();
		if (actualUrl.contains(expectedUrl)) {
			log("Verified expected Url :" + expectedUrl,
					TestStepType.VERIFICATION_RESULT);
			return true;
		} else {
			log("Incorrect Page Url, Expected : " + expectedUrl + " \nActual: "
					+ actualUrl, TestStepType.ERRORMESSAGE);
			return false;
		}

	}

	/**
	 * Verifies Current Page Title matches with expectedTitle Log Verification
	 * result to report if fail
	 */
	public void verifyTitle(String expectedTitle) {
		String actualTitle = "";
		try {
			actualTitle = getDriver().getTitle();
		} catch (Exception e) {
			fail("WebDriver Error while getting title");
		}
		if (actualTitle.contains(expectedTitle)) {
			log("Expected Title: " + expectedTitle + " present.",
					TestStepType.VERIFICATION_RESULT);
		} else {
			log("Expected Title: " + expectedTitle + " NOT present."
					+ "Actual Title : " + actualTitle,
					TestStepType.ERRORMESSAGE);
		}
	}

	/**
	 * Verifies value of "value" attribute of element matches with expectedValue
	 * Log Verification result to report
	 */
	public void verifyValue(String locator, String expectedValue) {
		verifyAttribute(locator, "value", expectedValue);
	}

	/**
	 * Wait for specified milliseconds
	 */
	public void wait(int timeout) {
		try {
			Thread.sleep(timeout);
			if (timeout > 1000) {
				// log("Sleep: " + timeout, TestStepType.INNER_SUBSTEP);
			}
		} catch (InterruptedException ex) {
			log(ex.getMessage(), TestStepType.ERRORMESSAGE);
		}
	}

	/**
	 * Wait for element present for default timeout 30 seconds. Fail and Stop
	 * Script if not found and log in report
	 */
	public WebElement waitForElement(String locator) {
		return waitForElement(locator, timeout, false, false, true, true);
	}

	/**
	 * Wait for element present for specified timeout in seconds. Fail and Stop
	 * Script if not found and log in report
	 */
	public WebElement waitForElement(String locator, int timeout) {
		return waitForElement(locator, timeout, false, true, true, true);
	}

	/**
	 * Wait for element with specified options if the element has just appeared
	 * 1 second before on page indicating that the page is still loading, it
	 * waits for 5 seconds to allow page load completely and searches the
	 * element again to avoid stale element exception.
	 * 
	 * @param locator
	 *            - element locator
	 * @param timeout
	 *            - seconds to wait for element
	 * @param soft
	 *            - if true, log fail message in report and soft fail Script(At
	 *            End of Execution); if false - halts further script execution
	 *            if fail with fail log
	 * @param report
	 *            - log pass/fail result to report
	 * @param present
	 *            - if true, check for element presence; if false - check for
	 *            element absence
	 * @param visibilityCheck
	 *            - if true, check for isDisplayed(). Set this false when
	 *            sometimes element is visually present but isDisplayed()
	 *            returns false
	 * @return WebElement - returns found WebElement if found, else returns null
	 */
	public WebElement waitForElement(String locator, int timeout, boolean soft,
			boolean report, boolean present, boolean visibilityCheck) {
		WebElement element = null;
		boolean visibilityCheckPrev = this.visibilityCheck;
		// setVisibilityCheck(visibilityCheck);
		wait(LoadProperties.STEP_WAIT);
		for (int seconds = 0;; seconds++) {
			if (seconds >= timeout) {
				if (!present) {
					if (showVerifications && report) {
						log("UnExpected element : " + getLocatorName(locator)
								+ " NOT present.",
								TestStepType.VERIFICATION_RESULT);
					}
					break;
				} else {
					if (!soft) {
						fail("\"" + getLocatorName(locator)
								+ "\" not found on page, Wait:" + seconds
								+ getError());
					} else if (report) {
						log("\"" + getLocatorName(locator)
								+ "\" not found on page, Wait:" + seconds,
								TestStepType.ERRORMESSAGE);
					}
					break;
				}

			}
			element = findElementM(locator);
			if (element != null) {
				if (!present) {
					if (report) {
						log("UnExpected Element: \"" + getLocatorName(locator)
								+ " found on page, Wait:" + seconds,
								TestStepType.ERRORMESSAGE);
						element = null;
						break;
					}
				} else {
					if (seconds > 0) { // seconds>0 indicates that the element
										// has just appeared 1 second before on
										// page and page is still loading,
						wait(5000);// so need to wait for 5 seconds to allow the
									// page load completely.
						element = findElementM(locator); // searching the
															// element again
															// after 5 seconds
															// to avoid stale
															// element
															// exception.
						if (showVerifications && report) {
							log("Verify \"" + getLocatorName(locator)
									+ "\" present.",
									TestStepType.VERIFICATION_RESULT);
						}

					}
					break;
				}

			}
			wait(1000);
		}
		setVisibilityCheck(visibilityCheckPrev);
		return element;
	}

	/**
	 * Wait for element NOT present for 5 seconds by default Soft Fail Script(At
	 * End of Execution) if not found and log in report
	 * 
	 * @return WebElement if found else return null
	 */
	public WebElement waitForElementNotPresent(String locator) {
		return waitForElementNotPresent(locator, 5);
	}

	/**
	 * Wait for element NOT present for specified timeout in seconds Soft Fail
	 * Script(At End of Execution) if not found and log in report
	 * 
	 * @return WebElement if found else return null
	 */
	public WebElement waitForElementNotPresent(String locator, int timeout) {
		return waitForElement(locator, timeout, true, true, false, true);
	}

	/**
	 * Wait for element NOT present for specified timeout in seconds Soft Fail
	 * Script(At End of Execution) if not found and log in report if report is
	 * true
	 * 
	 * @return WebElement if found else return null
	 */
	public WebElement waitForElementNotPresent(String locator, int timeout,
			boolean report) {
		return waitForElement(locator, timeout, true, report, false, true);
	}

	/**
	 * Wait for element NOT present for 5 seconds by default No Action if
	 * element not found
	 * 
	 * @return WebElement if found else return null
	 */
	public WebElement waitForElementNotPresentNoReport(String locator) {
		return waitForElementNotPresentNoReport(locator, 5);
	}

	/**
	 * Wait for element NOT present for specified timeout in seconds No Action
	 * if element not found
	 * 
	 * @return WebElement if found else return null
	 */
	public WebElement waitForElementNotPresentNoReport(String locator,
			int timeout) {
		return waitForElement(locator, timeout, true, false, false, true);
	}

	/**
	 * Wait for element present without isDisplayed() check for 5 seconds by
	 * default No Action if element not found
	 * 
	 * @return WebElement if found else return null
	 */
	public boolean waitForElementPresentNoReportNoVisibilityCheck(String locator) {
		return waitForElementPresentNoReportNoVisibilityCheck(locator, 5);
	}

	/**
	 * Wait for element present without isDisplayed() check for specified
	 * timeout in seconds No Action if element not found
	 * 
	 * @return true if found else return false
	 */
	public boolean waitForElementPresentNoReportNoVisibilityCheck(
			String locator, int timeout) {
		if (waitForElement(locator, timeout, true, false, true, false) != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Wait for element present without isDisplayed() check for 5 seconds by
	 * default Soft Fail Script(At End of Execution) if not found and log in
	 * report
	 * 
	 * @return true if found else return false
	 */
	public boolean waitForElementPresentSoftNoVisibilityCheck(String locator) {
		return waitForElementPresentSoftNoVisibilityCheck(locator, 5);
	}

	/**
	 * Wait for element present without isDisplayed() check for specified
	 * timeout in seconds Soft Fail Script(At End of Execution) if not found and
	 * log in report
	 */
	public boolean waitForElementPresentSoftNoVisibilityCheck(String locator,
			int timeout) {
		if (waitForElement(locator, timeout, true, true, true, false) != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Soft Wait - continues the test on failure. Waits for an element to be
	 * present for specified timeout. If element is not present, then Sets the
	 * testStatus to false and it is checked at the end of test.
	 */
	public boolean waitForElementSoft(String locator) {
		return waitForElementSoft(locator, 5);
	}

	/**
	 * Soft Wait - continues the test on failure. Waits for an element to be
	 * present for specified timeout. If element is not present, then Sets the
	 * testStatus to false and it is validated at the end of test.
	 */
	public boolean waitForElementSoft(String locator, int timeout) {
		if (waitForElement(locator, timeout, true, true, true, true) != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean waitForElementVisible(String locator) {
		return waitForElementVisible(locator, 5);
	}

	public boolean waitForElementVisible(String locator, int timeout) {
		if (waitForElement(locator, timeout, true, true, true, true) != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean waitForElementVisibleNoHighlight(String locator) {
		boolean temp = highlight, visible;
		highlight = false;
		visible = waitForElementVisible(locator);
		highlight = temp;
		return visible;
	}

	public boolean waitForElementVisibleNoReport(String locator) {
		return waitForElementVisibleNoReport(locator, 5);
	}

	public boolean waitForElementVisibleNoReport(String locator, int timeout) {
		if (waitForElement(locator, timeout, true, false, true, true) != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean waitForElementWithNameNoReport(String locator, int timeout) {
		if (waitForElement(locator, timeout, true, false, true, false) != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean waitForText(String textToWait) {
		return waitForText(textToWait, timeout / 2, true, true);
	}

	public boolean waitForText(String textToWait, boolean report) {
		return waitForText(textToWait, timeout / 2, report, true);
	}

	/**
	 * Waits for Specified text to be present for specified timeout
	 * 
	 * @param textToWait
	 *            - text to wait for
	 * @param timeout
	 *            - time in seconds to wait for
	 * @return true if text found else false
	 */
	public boolean waitForText(String textToWait, int timeout) {
		return waitForText(textToWait, timeout, true, true);
	}

	private boolean waitForText(String textToWait, int timeout, boolean report,
			boolean exact) {
		String[] textsToWait = textToWait.trim().split("'");
		boolean found = true;
		for (int seconds = 0;; seconds++) {
			if (seconds >= timeout) {
				if (report) {
					log("Expected Text : \""
							+ textToWait
							+ "\" not displayed on page, \nActual Text: Check Html Link \nWait:"
							+ seconds + getError(), TestStepType.ERRORMESSAGE);
				}
				return false;
			}
			if (findElementM("//*[contains(" + (exact ? "text()" : ".") + ",'"
					+ textsToWait[0] + "')]") != null) {
				for (int i = 1; i < textsToWait.length; i++) {
					if (findElementM("//*[contains(" + (exact ? "text()" : ".")
							+ ",'" + textsToWait[i] + "')]") == null) {
						found = false;
						break;
					}
				}
				if (!found) {
					continue;
				}
				if (seconds > 0) { // seconds>0 indicates that the element has
									// just appeared 1 second before on page and
									// page is still loading,
					wait(5000);// so need to wait for 5 seconds to allow the
								// page load completely.
				}
				log("Text : \"" + textToWait + "\" present",
						TestStepType.INNER_SUBSTEP);
				return true;
			}
			wait(1000);
		}
	}

	public boolean waitForTextExact(String textToWait, boolean report) {
		return waitForText(textToWait, 5, report, true);
	}

	public boolean waitForTextExact(String textToWait, int timeout) {
		return waitForText(textToWait, timeout, true, true);
	}

	public boolean waitForTextInSection(String locator, String expectedText) {
		return waitForTextInSection(locator, expectedText, true, 5);
	}

	public boolean waitForTextInSection(String locator, String expectedText,
			int timeout) {
		return waitForTextInSection(locator, expectedText, true, timeout);
	}

	public boolean waitForTextInSectionNoReport(String locator,
			String expectedText) {
		return waitForTextInSection(locator, expectedText, false, 5);
	}

	public boolean waitForTextInSectionNoReport(String locator,
			String expectedText, int timeout) {
		return waitForTextInSection(locator, expectedText, false, timeout);
	}

	public boolean waitForTextInSection(String locator, String expectedText,
			boolean report, int timeout) {
		String actualText = getText(locator);
		for (int seconds = 0;; seconds++) {
			if (seconds >= timeout) {
				if (report) {
					log("Expected Text : \"" + expectedText
							+ "\" NOT displayed in Section :"
							+ getLocatorName(locator) + " Actual Text : \""
							+ actualText + "\"" + " Wait:" + seconds,
							TestStepType.ERRORMESSAGE);
				}
				return false;
			}
			if (isTextMatching(expectedText, actualText)) {
				if (seconds > 0) { // seconds>0 indicates that the element has
									// just appeared 1 second before on page and
									// page is still loading,
					wait(5000);// so need to wait for 5 seconds to allow the
								// page load completely.
				}
				log("Expected Text : \"" + expectedText
						+ "\" displayed in Section :" + getLocatorName(locator),
						TestStepType.VERIFICATION_RESULT);
				return true;
			}
			wait(1000);
			actualText = getText(locator);
		}
	}

	public boolean waitForTextNoReport(String textToWait) {
		return waitForText(textToWait, 5, false, false);
	}

	public boolean waitForTextNoReport(String textToWait, int timeout) {
		return waitForText(textToWait, timeout, false, false);
	}

	/**
	 * Verifies text not present
	 */
	public boolean waitForTextNotPresent(String textToWait) {
		return waitForTextNotPresent(textToWait, 5);
	}

	public boolean waitForTextNotPresent(String textToWait, int timeout) {
		textToWait = textToWait.trim();
		for (int seconds = 0;; seconds++) {
			if (seconds >= timeout) {
				log("UnExpected Text : \"" + textToWait
						+ "\" displayed on page, Wait:" + seconds,
						TestStepType.ERRORMESSAGE);
				return false;
			}
			if (findElementM("//*[contains(text(),'" + textToWait + "')]") == null) {
				if (seconds > 0) { // seconds>0 indicates that the element has
									// just appeared 1 second before on page and
									// page is still loading,
					wait(5000);// so need to wait for 5 seconds to allow the
								// page load completely.
				}
				return true;
			}
			wait(1000);
		}
	}

	/**
	 * Verifies specified text is NOT present word by word
	 */
	public boolean waitForTextNotPresentSplit(String textToWait) {
		String text[] = textToWait.split(",");
		for (int i = 0; i < text.length; i++) {
			if (waitForTextNotPresent(text[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Verifies specified text is present word by word
	 */
	public boolean waitForTextSplit(String textToWait) {
		String text[] = textToWait.split(",");
		for (int i = 0; i < text.length; i++) {
			if (waitForText(text[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Wait for specified title log to report on failure
	 */
	public int getXpathCount(String locator) {
		int count = 0;
		try {
			count = getDriver().findElements(By.xpath(locator)).size();
		} catch (Exception e) {
			return count;
		}
		return count;
	}

	public void hoverClickAndWait(String locator, int timeout) {
		hover(locator);
		wait(1000);
		clickAndWait(locator, timeout);
	}

	public void hover(String locator) {
		Actions builder = new Actions(getDriver());
		WebElement Element = waitForElement(locator);
		try {
			if (LoadProperties.BROWSER.toLowerCase().contains("firefox")
					|| LoadProperties.BROWSER.toLowerCase()
							.contains("explorer")) {
				builder.moveToElement(Element, 0, -800).build().perform();
			} else {
				builder.moveToElement(Element, 0, -1000).build().perform();
			}

		} catch (Exception e) {
			try {
				builder.moveToElement(Element, 0, -100).build().perform();
			} catch (Exception e1) {
				builder.moveToElement(Element, 0, -50).build().perform();
			}
		}
		reporter("Mouse hover on the expected element is done", "");
		wait(3000);
	}

	public boolean waitForTitle(String expectedTitle) {
		return waitForTitle(expectedTitle, timeout / 2, "");
	}

	public boolean waitForTitle(String expectedTitle, int timeout, String tName) {
		String actualTitle = "";
		for (int seconds = 0;; seconds++) {
			actualTitle = getDriver().getTitle().trim();
			if (seconds >= timeout) {
				log("Expected Value : " + expectedTitle + "\n"
						+ "Actual Value : " + actualTitle + "  <br> " + tName
						+ " :: FAIL", TestStepType.ERRORMESSAGE);
				getAssertList().add("FAIL");
				log.error(tName + "::::" + "FAILED");
				return false;
			}
			if (actualTitle.contains(expectedTitle.trim())) {
				System.setProperty("org.uncommons.reportng.escape-output",
						"false");
				log.info("driver value :: " + getDriver().toString());
				reporter("Expected Value :: " + expectedTitle + "</br>"
						+ "Actual Value :: " + actualTitle + "  <br> " + tName
						+ " :: PASS", "");
				getAssertList().add("PASS");
				log.info(tName + "::::" + "PASSED");
				return true;
			}
			wait(1000);
		}

	}

	/**
	 * Opens Specified Url
	 */
	public void open(String url) {
		log("Open Url: <a href=\"" + url + "\" target=_blank>" + url + "</a>",
				TestStepType.INNER_SUBSTEP);
		try {
			getDriver().get(url);
			String text = getDriver().getPageSource();
		} catch (TimeoutException e) {
		}
	}

	/**
	 * Verifies expectedAlertTextPattern and click ok on alert dialog
	 * 
	 * @param expectedAlertTextPattern
	 */
	public void verifyAlertText(String expectedAlertTextPattern) {
		try {
			wait(1000);
			String actualAlertText = acceptAlert();
			if (isTextMatching(expectedAlertTextPattern, actualAlertText)) {
				log("Expected Alert Text \"" + actualAlertText + "\" present.",
						TestStepType.VERIFICATION_RESULT);
			} else {
				log("Expected Alert Text \"" + expectedAlertTextPattern
						+ "\" not present, Actual Alert Text : "
						+ actualAlertText, TestStepType.ERRORMESSAGE);
			}
		} catch (Exception e) {
			log("Alert exception " + e.getMessage(), TestStepType.EXCEPTION);
			e.printStackTrace();
		}
	}

	/**
	 * Returns true if expectedTextPattern matches actualText, else return false
	 * 
	 * @param expectedTextPattern
	 * @param actualText
	 * @return
	 */
	public boolean isTextMatching(String expectedTextPattern, String actualText) {
		Pattern p = Pattern.compile(expectedTextPattern);

		Matcher m;
		m = p.matcher(actualText);
		if (m.find()) {
			return true;
		}
		return false;
	}

	public void StoringCurrentWindowHandle() throws Exception {
		put("WinhandleBefore", getDriver().getWindowHandle());

	}

	public void SwitchingToNewWindowOpened() {
		pause(5);
		for (String WinHandle : getDriver().getWindowHandles()) {
			getDriver().switchTo().window(WinHandle);
			pause(2);
		}
		getDriver().manage().window().maximize();
		pause(5);

	}

	/**
	 * Switches to First Frame on Page
	 */
	public void SwitchToFrame() {
		SwitchToFrame(0);
	}

	/**
	 * Switches to Frame with specified index
	 * 
	 * @param index
	 * @return
	 */
	public boolean SwitchToFrame(int index) {
		try {
			pause(5);
			getDriver().switchTo().frame(index);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Switches to Frame with specified id or name
	 * 
	 * @param idORname
	 */
	public void SwitchToFrame(String idORname) {
		try {
			put("WinHandle", getDriver().getWindowHandle());
			pause(5);
			getDriver().switchTo().frame(idORname);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * Switches to Frame with element specified locator
	 * 
	 * @param locator
	 *            - Element Locator
	 * @return
	 */
	public WebElement SwitchToFrameWithElement(String locator) {
		put("frameFound", "false");
		try {
			put("WinHandle", getDriver().getWindowHandle());
			for (int i = 0; i < 15; i++) {
				if (SwitchToFrame(i)) {
					if (waitForElementPresentNoReportNoVisibilityCheck(locator)) {
						put("frameFound", "true");
						pause(2);
						return waitForElement(locator);
					} else {
						// element = SwitchToFrameWithElement(locator);
						if (!Boolean.parseBoolean(getString("frameFound"))) {
							// getDriver().switchTo().parentFrame();
						}
					}
				} else {
					break;
				}
			}

		} catch (Exception e) {
			e.getMessage();
		}
		return null;
	}

	/**
	 * Switches to Frame with specified frame WebElement
	 * 
	 * @param idORname
	 */
	public void SwitchToFrame(WebElement we) {
		try {
			put("WinHandle", getDriver().getWindowHandle());
			pause(5);
			getDriver().switchTo().frame(we);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void ClosingNewWindowAndSwitchBack() {
		try {
			getDriver().close();
			getDriver().switchTo().window(getString("WinhandleBefore"));
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void SwitchBack() {
		try {
			getDriver().switchTo().window(getString("WinHandle"));
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void AcceptCertificateInIE() {
		if (LoadProperties.BROWSER.contains("explorer")
				&& getTitle().contains("Certificate Error")) {
			pause(3);
			try {
				getDriver()
						.get("javascript:document.getElementById('overridelink').click();");

			} catch (TimeoutException e) {
			}
			pause(5);

		}
	}

	public String getTitle() {
		try {
			return getDriver().getTitle();
		} catch (Exception e) {
			return "Error While Fetching Title";
		}
	}

	/**
	 * @author msonar This method is to provide thread sleep.
	 * 
	 * @param sec
	 */
	public void pause(int sec) {
		wait(sec * 1000);
		log("Pause: " + sec + " secs");
	}

	/**
	 * This method is used to close the browser
	 */
	public synchronized void browserClose() {
		log.info("browserClose");
		try {
			getDriver().quit();
			// remove();
		} catch (Exception e) {
			// Assert.assertTrue(false, e.getMessage());
			// log.error("Browser was already closed");
		}
	}

	/**
	 * @author msonar This method is to generate random alpha-numeric string
	 */
	public String getRandomAlphaNumeric(int len) {
		StringBuffer sb = new StringBuffer(len);
		for (int i = 0; i < len; i++) {
			int ndx = (int) (Math.random() * (ALPHA_NUM.length()));
			sb.append(ALPHA_NUM.charAt(ndx));
		}
		return sb.toString();
	}

	/**
	 * @author msonar This method is to generate random numeric string
	 */
	public String getRandomNumeric(int len) {
		StringBuffer sb = new StringBuffer(len);
		for (int i = 0; i < len; i++) {
			int ndx = (int) (Math.random() * (NUMERICS.length()));
			sb.append(NUMERICS.charAt(ndx));
		}
		return sb.toString();
	}

	/**
	 * Verifies Selected Item in Select List matches with expectedValue Logs
	 * Verification result to report
	 */
	public void verifySelectedValue(String locator, String expectedValue) {
		String actualValue = "";
		if (locator.contains("//")) {
			actualValue = getText(locator + "//option[@selected='selected']");
		} else {
			actualValue = getText("//select[@*='" + locator
					+ "']//option[@selected='selected']");
		}
		if (actualValue.trim().equals(expectedValue)) {
			log("Verify value \"" + expectedValue
					+ "\" selected in Select List \"" + getLocatorName(locator)
					+ "\"", TestStepType.VERIFICATION_RESULT);
		} else {
			log("Value \"" + expectedValue
					+ "\" NOT selected in Select List \""
					+ getLocatorName(locator) + "\", Actual Value : \""
					+ actualValue + "\"", TestStepType.ERRORMESSAGE);
		}
	}

	/**
	 * @author dhekumar This method is to verify blog post date
	 */
	@SuppressWarnings("deprecation")
	public Date verifyDateFormat(String date, int DateFormate, Locale locale) {

		date = date.replace("th", "");
		date = date.replace("st", "");
		date = date.replace("nd", "");
		date = date.replace("rd", "");
		DateFormat dfLong = DateFormat.getDateInstance(DateFormate, locale);
		Date blogDate = new Date(date);
		try {
			dfLong.parse(date);
			log("Verified Date String: " + date + " in Expected format:"
					+ DateFormate + "," + locale,
					TestStepType.VERIFICATION_RESULT);
		} catch (ParseException e) {
			log("Date String " + date + " NOT in Expected format:"
					+ DateFormate + "," + locale,
					TestStepType.VERIFICATION_RESULT);
		}

		return blogDate;
	}

	public void hover(String locator, int offSet) {
		Actions builder = new Actions(getDriver());
		WebElement Element = waitForElement(locator);
		try {
			if (LoadProperties.BROWSER.toLowerCase().contains("firefox")
					|| LoadProperties.BROWSER.toLowerCase()
							.contains("explorer")) {
				builder.moveToElement(Element, 0, offSet).build().perform();
			} else {
				builder.moveToElement(Element).build().perform();
			}

		} catch (Exception e) {
			try {
				builder.moveToElement(Element, 0, -100).build().perform();
			} catch (Exception e1) {
				builder.moveToElement(Element, 0, -50).build().perform();
			}
		}
		reporter("Mouse hover on the expected element is done", "");
		wait(3000);
	}

	/**
	 * Type specified text in element Skip to type if text="."
	 */
	public void typeAndEnter(String locator, String text) {

		type(locator, text).sendKeys(Keys.ENTER);

	}

	public int verifyXpathCount(String locator, int expectedCount) {
		int actualCount = getXpathCount(locator);
		if (expectedCount == actualCount) {
			log("Verified count of xpath: " + getLocatorName(locator)
					+ ", count: " + expectedCount,
					TestStepType.VERIFICATION_STEP);
		} else {
			log("Count of xpath: " + getLocatorName(locator)
					+ ", not eqaul to expected count " + expectedCount
					+ " Actual Count: " + actualCount,
					TestStepType.ERRORMESSAGE);
		}

		return actualCount;
	}

	public void setData(String key, String value) {
		DataHandler.setData(key.trim(), value.trim());
	}

	public String getData(String key) {
		String value = DataHandler.getData(key);
		log(key + ":" + value);
		return value;
	}

	/*
	 * 
	 */
	public void checkEmail(String emailId) {
		String Guerilla = "https://www.guerrillamail.com";
		open(Guerilla + "/ajax.php?f=set_email_user&email_user=" + emailId
				+ "&lang=en&domain=guerrillamail.com");
		pause(2);
		open(Guerilla);
		pause(3);
	}

	public void verifyDisabled(String locator) {
		WebElement element = waitForElement(locator);
		if (!element.isEnabled()) {
			log("Expected Element is disabled.",
					TestStepType.VERIFICATION_RESULT);
		} else {
			log("Expected Element is not disabled.", TestStepType.ERRORMESSAGE);
		}
	}

	public boolean verifyDisabledValue(String locator) {
		WebElement element = waitForElement(locator);
		boolean sEnabled = false;
		if (!element.isEnabled()) {
			sEnabled = true;
			log("Expected Element is disabled.",
					TestStepType.VERIFICATION_RESULT);
		} else {
			log("Expected Element is not disabled.", TestStepType.ERRORMESSAGE);
		}
		return sEnabled;
	}

	public void verifyEnabled(String locator) {
		WebElement element = waitForElement(locator);
		if (element.isEnabled()) {
			log("Expected Element is enabled.",
					TestStepType.VERIFICATION_RESULT);
		} else {
			log("Expected Element is not enabled.", TestStepType.ERRORMESSAGE);
		}
	}

	public ArrayList<String> getSelectOptions(String locator) {
		ArrayList<String> options = new ArrayList<String>();
		resetVisibilityCheckRemember();
		for (int i = 1; i <= new Select(waitForElement(locator)).getOptions()
				.size(); i++) {
			options.add(getText(locator + "//option[" + i + "]").trim());

		}
		setVisibilityCheckRemember();
		return options;
	}

	public void verifyTableData(String key, String keySubXapath, int colNo,
			String expectedText, boolean contains) {
		try {
			if (contains) {
				waitForTextInSection("//*[contains(text(),'" + key + "')]//"
						+ keySubXapath + "parent::tr//td[" + colNo + "]",
						expectedText);
			} else {
				waitForTextInSection("//*[text()='" + key + "']//"
						+ keySubXapath + "parent::tr//td[" + colNo + "]",
						expectedText);
			}
		} catch (Exception e) {
		}
	}

	public void verifyTableData(String key, int colNo, String expectedText) {
		verifyTableData(key, "..//", colNo, expectedText, false);
	}

	public void verifyTableHead(String key, int colNo, String expectedText) {
		waitForTextInSection("//*[text()='" + key + "']//parent::tr//th["
				+ colNo + "]", expectedText);
	}

	public String getFormattedDate(String format, String timeZone) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
		String formattedDate = sdf.format(new Date());
		return formattedDate;
	}

	public boolean verifyTextMatching(String expectedText, String actualText) {
		if (expectedText.equals(actualText)) {
			log("Actual Text : \"" + actualText + "\" matches with , "
					+ " Expected Text : \"" + expectedText + "\"",
					TestStepType.VERIFICATION_RESULT);
			return true;
		} else {
			log("Actual Text : \"" + actualText + "\" doesn't match with , "
					+ " Expected Text : \"" + expectedText + "\"",
					TestStepType.ERRORMESSAGE);
			return false;
		}
	}

	public boolean verifyTextNotMatching(String expectedText, String actualText) {
		if (expectedText.equals(actualText)) {
			log("Actual Text : \"" + actualText + "\" matches with , "
					+ " Expected Text : \"" + expectedText + "\"",
					TestStepType.ERRORMESSAGE);
			return false;
		} else {
			log("Actual Text : \"" + actualText + "\" doesn't match with , "
					+ " Expected Text : \"" + expectedText + "\"",
					TestStepType.VERIFICATION_RESULT);
			return true;
		}
	}
}
