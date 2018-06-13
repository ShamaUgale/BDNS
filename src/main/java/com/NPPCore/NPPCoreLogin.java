package com.NPPCore;

import com.NPPCore.data.NPPCore_BaseData;
import com.saf.global.TestStepType;

public class NPPCoreLogin extends NPPCoreWebElementLocators {

	// FOR LOGIN
	public void platformOnelogin(String UserName, String Password) {
		log("Entering the Email. ", TestStepType.INNER_SUBSTEP);
		type(P1_Login_Email_Textbox_Locator, UserName);
		log("Entering the Password. ", TestStepType.INNER_SUBSTEP);
		type(P1_Login_Password_Textbox_Locator, Password);
		log("Clicking on LOGIN button.", TestStepType.INNER_SUBSTEP);
		click(P1_Login_Button_Locator);
	}

	// FOR LOGOUT
	public void platformOnelogout() {
		log("Clicking on Profile Icon Arrow Link. ", TestStepType.INNER_SUBSTEP);
		clickAndWait(P1_Logout_ArrowButton_Locator, 2);
		pause(2);
		log("Clicking on LOGOUT Link. ", TestStepType.INNER_SUBSTEP);
		click(P1_Logout_Button_Locator);
		verifyAuthenticationMessage(NPPCore_BaseData.P1_Logout_LoggedOut_SuccessMessage);
	}

	// Below code verifies all the elements on Login Page
	public void verifyLoginPage() {
		log("Verifying all the Login Page Elements",
				TestStepType.VERIFICATION_STEP);
		waitForElement(P1_Login_NeustarText_Locator);
		waitForElement(P1_Login_NeustarPlatformOneText_locator);
		waitForElement(P1_Login_TagLine_Locator);

		String tagLine = getText(P1_Login_TagLine_Locator).toString().trim();
		String[] actTagLine = tagLine.split("\n");
		String actualTagLine = actTagLine[0].trim() + actTagLine[1].trim();
		verifyTextMatching(
				NPPCore_BaseData.P1_Login_Expected_TagLine_Message,
				actualTagLine);

		String actualDescription = getText(P1_Login_DescriptionText_Locator)
				.trim();
		verifyTextContains(actualDescription,
				NPPCore_BaseData.P1_Login_ExpectedDescription_Message);
		verifyTextContains(actualDescription,
				NPPCore_BaseData.P1_Login_ExpectedDescription1_Message);

		waitForElement(P1_Login_ContactUs_Button_Locator);
		waitForElement(P1_Login_LoginText_Locator);
		waitForElement(P1_Login_Email_Label_Locator);
		waitForElement(P1_Login_Email_Textbox_Locator);
		waitForElement(P1_Login_Password_Label_Locator);
		waitForElement(P1_Login_Password_Textbox_Locator);
		waitForElement(P1_Login_RememberMe_Checkbox_Locator);
		waitForElement(P1_Login_ForgotPassword_Link_Locator);
		waitForElement(P1_Login_Button_Locator);
	}

	// Below code verifies the Authentication Error Message on Login Page.
	public void verifyAuthenticationMessage(String message) {
		log("Verifying message: " + message, TestStepType.VERIFICATION_STEP);
		waitForElement(NPPCoreWebElementLocators.P1_Login_AuthenticationMessage_Locator
				.format(message));
	}

	// Below code clicks on Forgot Password link.
	public void clickForgotYourPasswordLink() {
		log("Clicking on Forgot your password link.",
				TestStepType.INNER_SUBSTEP);
		clickAndWait(P1_Login_ForgotPassword_Link_Locator);
	}

	// Below function verifies Reset Password Page.
	public void verifyResetPasswordPage() {
		
	}

	
}
