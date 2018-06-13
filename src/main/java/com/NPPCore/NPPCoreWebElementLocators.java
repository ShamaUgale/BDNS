package com.NPPCore;

import com.NPPCore.global.GlobalCommonMethods;
import com.saf.global.Locator;

public class NPPCoreWebElementLocators extends GlobalCommonMethods {

	// LOCATORS FOR LOGIN PAGE
	public String P1_Login_NeustarText_Locator = "//a[@class='neustar-logo']/img";
	public String P1_Login_NeustarPlatformOneText_locator = "//h1[@class='standardcase']";
	public String P1_Login_TagLine_Locator = "//h1[@class='standardcase']//parent::div//following-sibling::h3";
	public String P1_Login_DescriptionText_Locator = "//h1[@class='standardcase']//parent::div//following-sibling::h3/following-sibling::p[1]";
	public String P1_Login_ContactUs_Button_Locator = "//h1[@class='standardcase']//parent::div//following-sibling::h3/following-sibling::p[2]/a";
	public String P1_Login_LoginText_Locator = "//div[@ng-show='showLogin']//h3";
	public String P1_Login_Email_Label_Locator = "//div[@ng-show='showLogin']//label[@for='inputEmail']";
	public String P1_Login_Email_Textbox_Locator = "id=inputEmail";
	public static final String P1_Login_InvalidEmailFormatErrorMessage_Locator = "//div[@ng-show='showLogin']//input[@id='inputEmail']//following-sibling::div";
	public String P1_Login_Password_Label_Locator = "//div[@ng-show='showLogin']//label[@for='inputPassword']";
	public String P1_Login_Password_Textbox_Locator = "id=inputPassword";
	public String P1_Login_RememberMe_Checkbox_Locator = "//label[@for='remember-me']";
	public String P1_Login_ForgotPassword_Link_Locator = "//a[contains(.,'Forgot')]";

	public String P1_Login_Button_Locator = "css=button.btn.btn-primary";
	public static final Locator P1_Login_AuthenticationMessage_Locator = new Locator(
			"//p[@ng-repeat='message in messageSet.messages'][contains(.,'$0')]");
	public String P1_Login_InvalidEmailEntered_ErrorMessage_Locator = "(//div[@class='error-message'])[1]";

	// LOCATORS FOR LOGOUT
	public String P1_Logout_ArrowButton_Locator = "//span[@class='nuxicon nuxicon-trending-down']";
	public String P1_Logout_Button_Locator = "link=Logout";

	
	public String P1_Audience_SearchResultContainerList_Locator = "//div[@value='audience']";
	public Locator P1_Audience_SearchedAudienceName_Locator = new Locator(
			"//div[@value='audience'][$0]//div[@class='tile-name']/span[1]");
	public Locator P1_Audinece_SearchedAudienceImage_Locator = new Locator(
			"//div[@value='audience'][$0]//div[@class='tile-icon header']");
	public Locator P1_Audience_SearchedAudience_AudienceKeyword_Locator = new Locator(
			"//div[@value='audience'][$0]//span[@class='entity-type ng-binding']");
	public Locator P1_Audience_SearchedAudience_AdvertiserKeyword_Locator = new Locator(
			"//div[@value='audience'][$0]//div[@class='details']//span[1]");
	public Locator P1_Audience_SearchedAudience_AdvertiserName_Locator = new Locator(
			"//div[@value='audience'][$0]//div[@class='details']//span[2]");
	public Locator P1_Audience_SearchedAudience_Summary_Locator = new Locator(
			"//div[@value='audience'][$0]//div[@class='summary ']");
	

}
