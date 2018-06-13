package com.NPPCore.global;

import java.awt.AWTException;

import com.saf.global.TestStepType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import com.saf.global.Global;

public class GlobalCommonMethods extends Global {

	public void hoverClick(String locator) {

		Actions action = new Actions(getDriver());
		WebElement Element = waitForElement(locator);
		action.moveToElement(Element).build().perform();
	}

	public void SelectNew(String locator, String VisibleText) {

		WebElement element = waitForElement(locator);
		Select dd = new Select(element);
		dd.selectByVisibleText(locator);
	}

	// Method for Today's Date
	public String todayDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		String tDate = null;
		tDate = dateFormat.format(date);
		return tDate;
	}

	// CLOSING THE WINDOW AND BEING SWITCHED BACK TO SPECIFIED WINDOW
	public void ClosingNewWindowAndSwitchBack(String Winhandle) {
		try {

			getDriver().close();
			getDriver().switchTo().window(Winhandle);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	// SWITCHING BACK TO SPECIFIED WINDOW
	public void SwitchBack(String Winhandle) {
		try {
			getDriver().switchTo().window(Winhandle);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	// TO STORE THE CURRENT WINDOW HANDLE
	public String CurrentWindowHandle() throws Exception {
		return (getDriver().getWindowHandle());

	}

	public void SelectByIndex(String locator, int index) {

		WebElement element = waitForElement(locator);
		Select dd = new Select(element);
		dd.selectByIndex(index);
	}

	public int getNumberOfWindowsOpened() {
		Set<String> win = getDriver().getWindowHandles();
		int size = win.size();
		return size;
	}

	// SWITCH TO PARENT FRAME

	public void SwitchToParentFrame() {
		// getDriver().switchTo().parentFrame();

	}

	// COMING TO MAIN PAGE OUT OF ALL FRAME
	public void DefaultContent() {
		getDriver().switchTo().defaultContent();
	}

	public void AcceptAlert() {
		acceptAlert();
		log("Accept Alert clicked", TestStepType.VERIFICATION_STEP);
	}

	public void dismissalert() {
		dismissAlert(2);
		log("Alert cancel clicked", TestStepType.VERIFICATION_STEP);

	}

	public String dismissAlert(int timeout) {
		for (int seconds = 0;; seconds++) {
			if (seconds >= timeout) {
				return "";
			} else {
				try {
					String alertText = getDriver().switchTo().alert().getText()
							.trim();
					getDriver().switchTo().alert().dismiss();
					return alertText;
				} catch (Exception e) {

				}
				wait(1000);
			}
		}

	}

	// TO DELETE RECORD FROM DATABASE
	public void DeleteFromDataBase(String URL, String Userame, String Password,
			String Query) {
		Connection conn = null;
		Statement stmt = null;
		try {

			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(URL, Userame, Password);
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			stmt.executeUpdate(Query);

			stmt.close();
			conn.close();

		} catch (SQLException se) {
			se.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}
		log("Database manupulation is done successfully");
	}

	// public void InitializeNew(String browser, String version, String
	// platform) throws IOException {
	// // LOGGER.info("Entering initialize function .... ");
	// this.browser = LoadProperties.BROWSER;
	// dr = defineBrowser(browser, version, platform);
	// // driver = new EventFiringWebDriver(dr);
	// get().setWebDriver(dr);
	// // getDriver().manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
	// getDriver().manage().timeouts().pageLoadTimeout(timeout,
	// TimeUnit.SECONDS);
	// if (browser.equalsIgnoreCase("explorer")) {
	// //getDriver().manage().deleteAllCookies();
	// getDriver().manage().window().maximize();
	// } else {
	// getDriver().manage().window().maximize();
	// }
	// open(url);
	// wait(2);
	// log.info(browser + " Browser has been Initialized");
	// }

	// public void Screenshot(Scenario scenario) {
	// if (scenario.isFailed()) {
	// // Take a screenshot...
	// byte[] screenshot = ((TakesScreenshot)
	// getDriver()).getScreenshotAs(OutputType.BYTES);
	// scenario.embed(screenshot, "image/png"); // ... and embed it in the
	// report.
	// }
	// }

	public void dragAndDrop(String source, String target) {
		Actions builder = new Actions(getDriver());
		Action dragAndDrop = builder.clickAndHold(waitForElement(source, 5))
				.moveToElement(waitForElement(target, 5))
				.release(waitForElement(target, 5)).build();
		dragAndDrop.perform();
		pause(4);
	}

	public void dragAndDropElement(String sourceElementLocator,
			String targetElementLocator) throws InterruptedException,
			AWTException {
		WebElement sourceElement = findElementM(sourceElementLocator);
		WebElement targetElement = findElementM(targetElementLocator);
		try {
			if (sourceElement.isDisplayed() && targetElement.isDisplayed()) {
				Actions action = new Actions(getDriver());
				// action.dragAndDrop(sourceElement, targetElement).build()
				// .perform();

				action.clickAndHold(waitForElement(sourceElementLocator))
						.moveToElement(waitForElement(targetElementLocator))
						.release(waitForElement(targetElementLocator)).build();
				action.perform();
				pause(10);

			} else {
				log("Element was not displayed to drag.");
			}
		} catch (StaleElementReferenceException e) {
			System.out.println("Element with " + sourceElement + "or"
					+ targetElement + "is not attached to the page document. "
					+ e.getStackTrace());
		} catch (NoSuchElementException e) {
			System.out.println("Element " + sourceElement + "or"
					+ targetElement + " was not found in DOM. "
					+ e.getStackTrace());
		} catch (Exception e) {
			System.out
					.println("Error occurred while performing drag and drop operation."
							+ e.getStackTrace());
		}

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

	public boolean verifyTextContains(String textToCheck, String entireText) {
		if (entireText.contains(textToCheck)) {
			log("Actual Text : \"" + entireText + "\" contains , "
					+ " Text : \"" + textToCheck + "\"",
					TestStepType.VERIFICATION_RESULT);
			return true;
		} else {
			log("Actual Text : \"" + entireText + "\" does not contains , "
					+ " Text : \"" + textToCheck + "\"",
					TestStepType.VERIFICATION_RESULT);
			return true;
		}
	}

	public boolean isElementDisplayed(WebElement element) {
		if (element.isDisplayed()) {
			log("Element \"" + element.getText() + "\"" + " is displayed.",
					TestStepType.VERIFICATION_STEP);
			return true;
		} else {
			log("Element \"" + element.getText() + "\"" + " is not displayed.",
					TestStepType.ERRORMESSAGE);
			return false;
		}
	}

	public boolean isElementClickable(String loc) {
		WebElement ele = findElementM(loc);
		if (ele.isEnabled()) {
			return true;
		} else {
			log("Element is not clickable", TestStepType.ERRORMESSAGE);
			return false;
		}

	}

	public List<WebElement> getListOfWebElements(WebElement itemContainer,
			String itemList) {
		List<WebElement> elementList = itemContainer.findElements(By
				.xpath(itemList));
		return elementList;
	}

	public List<WebElement> getElementsList(String itemList) {
		List<WebElement> elementList = getDriver().findElements(
				By.xpath(itemList));
		return elementList;
	}

	public String extractStringFromAlphaNumberString(String string) {
		return string;

	}

	public String removeMultipleSpaces(String str, String replace) {
		Pattern ptn = Pattern.compile("\\s+");
		Matcher mtch = ptn.matcher(str);
		return mtch.replaceAll(replace).toString().trim();
	}

	public String removeNumbersAtndOfStringFromString(String str, String replace) {
		Pattern ptn = Pattern.compile("\\d*$");
		Matcher mtch = ptn.matcher(str);
		return mtch.replaceAll(replace).toString().trim();
	}

	public boolean verifyElementNotDisplayed(String locator) {
		WebElement element = waitForElement(locator);
		if (!element.isDisplayed()) {
			log("Element \"" + element.getText() + "\"" + " is not displayed.",
					TestStepType.VERIFICATION_STEP);
			long seconds = System.currentTimeMillis() / (1000L);
			System.out.println("Notification Invisible at " + seconds);
			put("notificationInvisible", seconds);
			return true;
		} else {
			log("Element \"" + element.getText() + "\"" + " is displayed.",
					TestStepType.ERRORMESSAGE);
			return false;
		}
	}
}
