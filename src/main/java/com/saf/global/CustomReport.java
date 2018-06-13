package com.saf.global;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.saf.util.FileOperation;
import com.saf.util.Reasons;



public class CustomReport extends ActionMethods{
	private WebDriver driver;
	

	
	

	public void VerifyText(String sExpected, String sActual, List<String> statusValue, WebDriver driver, String sTestcaseName) {
		this.driver = driver;
		log.info("Expected ::::"+sExpected);
		log.info("Actual ::::"+sActual);
		if (sActual.contains(sExpected)) {
			/*Reasons.reporter("Expected Value " + sExpected + "</br>" + "Actual Value"
					+ sActual + "::PASS", "");*/
			System.setProperty("org.uncommons.reportng.escape-output", "false");
			log.info("driver value :: "+driver.toString());
			reporter("Expected Value :: " + sExpected + "</br>" + "Actual Value :: " + sActual + " :: PASS","");
			statusValue.add("PASS");
			log.info(sTestcaseName+"::::"+"PASSED");
		} else {
			/*Reasons.reporter("Expected Value " + sExpected + "\n" + "Actual Value"	+ sActual + "::Fail" + AssertionresultOutput(driver,sTestcaseName),"");*/
			reporter("<font color='red'>"+"Expected Value " + sExpected + "\n" + "Actual Value"	+ sActual + " :: Fail" + AssertionresultOutput(driver,sTestcaseName)+"</font>","");
			statusValue.add("FAIL");
			log.error(sTestcaseName+"::::"+"FAILED");
		}
	}
	
	public void VerifyBodyText(String sExpected, String sActual, List<String> statusValue, WebDriver driver, String sTestcaseName) {
		this.driver = driver;
		log.info("Expected ::::"+sExpected);
		if (sActual.contains(sExpected)) {
			/*Reasons.reporter("Expected Value " + sExpected + "</br>" + "Actual Value"
					+ sActual + "::PASS", "");*/
			System.setProperty("org.uncommons.reportng.escape-output", "false");
			log.info("driver value :: "+driver.toString());
			reporter("Expected Value :: " + sExpected + " is present.</br>" + sTestcaseName + " :: PASS","");
			statusValue.add("PASS");
			log.info(sTestcaseName+" :: "+"PASSED");
		} else {
			/*Reasons.reporter("Expected Value " + sExpected + "\n" + "Actual Value"	+ sActual + "::Fail" + AssertionresultOutput(driver,sTestcaseName),"");*/
			reporter("<font color='red'>"+"Expected Value " + sExpected + "is not present.</br>" + sTestcaseName + " :: FAIL" + AssertionresultOutput(driver,sTestcaseName)+"</font>","");
			statusValue.add("FAIL");
			log.error(sTestcaseName+" :: "+"FAILED");
		}
	}
	
	public void VerifyUrls(String sExpected, String sActual, List<String> statusValue, WebDriver driver, String sTestcaseName) {
		this.driver = driver;
		log.info("Expected ::::"+sExpected);
		log.info("Actual ::::"+sActual);
		if (sActual.equals(sExpected)) {
			System.setProperty("org.uncommons.reportng.escape-output", "false");
			log.info("driver value :: "+driver.toString());
			reporter("Expected Value :: " + sExpected + "</br>" + "Actual Value :: " + sActual + "::PASS","");
			statusValue.add("PASS");
			log.info(sTestcaseName+"::::"+"PASSED");
		} else {
			reporter("<font color='red'>"+"Expected Value " + sExpected + "\n" + "Actual Value"	+ sActual + "::Fail" + AssertionresultOutput(driver,sTestcaseName)+"</font>","");
			statusValue.add("FAIL");
			log.error(sTestcaseName+"::::"+"FAILED");
		}
	}
	
	public void VerifyElement(WebElement sActual, List<String> statusValue, WebDriver driver, String sTestcaseName){
		this.driver = driver;
		log.info("Actual Element is ::"+sActual);
		if(sActual!=null){
			System.setProperty("org.uncommons.reportng.escape-output", "false");
			log.info("driver value :: "+driver.toString());
			reporter("Actual Element is " + sActual + ":: present","");
			statusValue.add("PASS");
			log.info(sTestcaseName+" :: "+"PASSED");
		}else{
			log.info("Actual Element is not present :: "+sActual);
			reporter("<font color='red'>" + "Actual Value" + sActual + "::Fail" + AssertionresultOutput(driver,sTestcaseName)+"</font>","");
			statusValue.add("FAIL");
			log.error(sTestcaseName+"::::"+"FAILED");
		}
	}
	
	public void VerifyElementText(WebElement sActual, List<String> statusValue, WebDriver driver, String sTestcaseName){
		this.driver = driver;
		log.info("Actual Text is ::"+sActual);
		if(sActual.getText()!=null){
			System.setProperty("org.uncommons.reportng.escape-output", "false");
			log.info("driver value :: "+driver.toString());
			reporter("Actual Text is " + sActual + ":: present","");
			statusValue.add("PASS");
			log.info(sTestcaseName+" :: "+"PASSED");
		}else{
			log.info("Actual Text is :: "+sActual);
			reporter("<font color='red'>" + "Actual Value" + sActual + "::Fail" + AssertionresultOutput(driver,sTestcaseName)+"</font>","");
			statusValue.add("FAIL");
			log.error(sTestcaseName+"::::"+"FAILED");
		}
	}
	
	 
	/**
	 * This Method is used to get the Absolute path of the File
	 * @param sFilepath
	 * @return :It returns the Path of the File
	 */
	 public String getFilePath(String sFilepath) {
		char cforwardslash = (char) 47;
		char cbackslash = (char) 92;
		String sPath = System.getProperty("user.dir").replace(cbackslash, cforwardslash) + sFilepath;

		File file = new File(sPath);
		/*
		 * if (file.exists()) { sPath = file.getAbsolutePath();
		 * log.info("The File Path is " + sPath); } else {
		 * log.error("File not Found"); }
		 */
		log.info("The File path is :: " + file.getAbsolutePath());
		return file.getAbsolutePath();
	}


	 public String AssertionresultOutput(WebDriver driver, String sTestcaseName) {
		logWithScreenshot(sTestcaseName);
		 return "";
		}
	
	/**
	 * This Method Provides the Reasons for the Test-Case Faliure in the Report and also Request&Response 
	 * @param ls
	 * @param file
	 * @param fileOperation
	 * @param sTestcaseName
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void checkinglist(List<String> statusValue,FileOperation fileOperation,String sTestcaseName,String sFilepath,String sdestPath) throws IOException, InterruptedException {
			//  reporter("val1","val2");
		
		log.info("driver value is" +driver.toString());
		  if(driver.toString().contains("Firefox"))
		  {
			  log.info("The value of driver is----------------->"+driver.toString());
			  log.info("Browser is  FireFox-------");
			   for (String  temp: statusValue) {
					if (temp.equals("FAIL")) {
						Thread.sleep(5000);
						fileOperation.moveFile(sFilepath, sdestPath,sTestcaseName);
						fileOperation.checkFileStatus(sdestPath,sTestcaseName);
						statusValue.clear();
						throw new Reasons("FAIL");
					}
					
					else
					{
						log.info("The Test Case is Passed......In the Pass Method");
						fileOperation.deleteResponseFile(sFilepath);
					}
					
				}
		  }
		  else
		  {
			  log.info("Browser is not FireFox-------");
			  for (String  temp: statusValue) {
				  if (temp.equals("FAIL")) {
						statusValue.clear();
						throw new Reasons("FAIL");
					}
				}
		  }
	}
			
   public void checkinglist(List<String> ls) {
	//  reporter("val1","val2");
	   for (String  temp: ls) {
			if (temp.equals("FAIL")) {
				throw new Reasons("FAIL");
			}
		}
	   
   }
}