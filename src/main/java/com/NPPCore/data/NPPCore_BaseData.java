package com.NPPCore.data;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class NPPCore_BaseData {
	public static final Random rand = new Random();
	public static final int randNumb = rand.nextInt();
	public static final String randNumber = Integer.toString(randNumb);

	// INPUTS FOR LOGIN TO PlatformOne
	public static final String P1_Login_Email_Input2 = "rajat.tandon@neustar.biz";
	public static final String P1_Login_Email_Input = "zaid.momin@neustar.biz";
	public static final String P1_Login_Password_Input = "Neustar1!";
	public static final String P1_Login_Password_Input2 = "Searsnpp1";
	public static final String P1_Login_InvalidEmail_Input = "email.invalid@test.com";
	public static final String P1_Login_InvalidPassword_Input = "password.invalid@test.com";
	public static final String P1_Login_InvalidEmailFormat_Input = "InvalidEmailFormat.com";
	public static final String P1_Login_ReadOnlyUser_Email_Input = "rajat.tandon+ro@neustar.biz";
	public static final String P1_Login_AccountManager_Email_Input = "rajat.tandon+am@neustar.biz";
	public static final String P1_Login_CustomerService_Email_Input = "rajat.tandon+csr@neustar.biz";
	public static final String P1_Login_ServiceManager_Email_Input = "rajat.tandon+sm@neustar.biz";
	public static final String P1_Login_InvalidEmailFormat_ErrorMessage_Input = "Invalid email entered";
	public static final String P1_Login_Expected_TagLine_Message = "Accurate data.Personalized dialogue.";
	public static final String P1_Login_ExpectedDescription_Message = "A centralized marketing solution that gives you a complete, accurate, real-time portrait of your customer";
	public static final String P1_Login_ExpectedDescription1_Message = "and enables real-time activation of customer and media intelligence.";
	public static final String P1_login_AuthenticationErrorMessage = "Authentication failed. For security reasons, your account may become locked if excessive failed logins are detected.";
	public static final String P1_Login_SessionTimedOutToastMessage_Input = "Session timed out";

	// INPUTS FOR LOGOUT
	public static final String P1_Logout_LoggedOut_SuccessMessage = "You have logged out successfully.";

	// INPUTS FOR RESET PASSWORD
	public static final String P1_ResetPassword_ResetPasswordHelp_Text = "Forgot your password? Enter the e-mail address you have on file to receive an e-mail with instructions on how to change your password.";
	public static final String P1_ResetPassword_EmailSentMessage = "A password reset email will be sent to your inbox";
	public static final String P1_ResetPassword_InvalidEmailEnteredMessage = "Invalid email entered";
	public static final String P1_ResetPassword_EnterEmailIdErrorMessage = "Please enter a valid email address.";

	// INPUTS FOR ADVERTISERS
	public String P1_Advertisers_AdvertiserName_Input = "Advertiser_"
			+ new DateTime(DateTimeZone.UTC);
	public String P1_Advertisers_SpecialChar_Input = "%^$$$$^^^%_"
			+ new DateTime(DateTimeZone.UTC);
	public String P1_Audience_AudienceName_Input = "Audience_"
			+ new DateTime(DateTimeZone.UTC);

	public static final String P1_Audience_ClonedAudienceName_Input = "Cloned-SGTI_Audience"
			+ randNumber.substring(0, 7);
	public static final String P1_Audience_NewAdvertiserName_Input = "Cloned-SGTI_Advertiser"
			+ randNumber.substring(0, 7);
	public static final String P1_Audience_SearchAudiencePlanner_Input = "Mobile";
	public static final String P1_Audience_SearchAudiencePlanner_Input1 = "Mayo";
	public static final String P1_Audience_MultiTerm_SearchAudiencePlanner_Input = "Computer & Electronics";
	public static final String P1_Audience_MultiTerm_SearchAudiencePlanner_Input1 = "Food & Drug";
	public static final String P1_Audience_ProfileNumber_Input = "1";
	public static final String P1_Audience_RestrictedSearchAudiencePlanner_Input = "Alcohol";

	// INPUTS FOR CLONED TAXONOMY
	public static final String P1_Audience_CloneTaxonomyName_Input = "SGTITaxonomy"
			+ randNumber.substring(0, 7);

	// INPUTS FOR CREATE CATEGORY
	public static final String P1_Audience_CreatecategoryName_Input = "SGTICategory"
			+ randNumber.substring(0, 7);

	// INPUTS FOR CREATE SEGMENT
	public static final String P1_Audience_CreateSegmentName_Input = "SGTISegment"
			+ randNumber.substring(0, 7);

	// INPUTS FOR EXPLORE TAB
	public static final List<String> P1_ExploreTab_AdAdvisorCategoryName_Input = Arrays
			.asList("All Elements", "Apparel", "Automotive",
					"Computers & Electronics", "Demographics", "Dining",
					"Discounts", "Education", "Financial", "Food & Drug",
					"Grocery", "Home & Leisure", "Lifestyles", "Media",
					"Mobile", "Shopping", "Travel");
	public static final String P1_ExploreTab_AllAudiences_Description_Input = "To get started targeting your customers online, explore your data by clicking here. You can build audiences with just your data, or you can expand your reach with Neustar AdAdvisor Audiences working alongside your own.";
	public static final String P1_ExploreTab_Audiences_Description_Input = "For brand advertisers and the technology partners that serve them, Neustar's real-time segmentation engine brings verified offline consumer data insights to the world of online display ad targeting. We connect our clients to the highest quality online audiences available anywhere.";
	public static String P1_ExploreTab_Search_Description_Input = "Use this page to explore further into sub-categories or use any profiles and segments here to build a custom audience.";
	public static final String P1_ExploreTab_NoSearchResult_Text_Input = "Your search did not result in any matches.";
	public static final String P1_ExploreTab_AdAdvisorAudienceTreeStructure_Input = "Taxonomies > AdAdvisor Audiences > ";
	public static final String P1_ExploreTab_RestrictedAdAdvisorAudiencesName_Input = "Restricted AdAdvisor Audiences";

	// INPUTS FOR AUDIENCES TAB
	public static final String P1_AudiencesTab_AudienceNameForSearch_Input = "AudienceForSearch";
	public static final String P1_AudienceBuilder_ReachData_Text_Input = "Reach data is not available for this Audience.";
	public static final String P1_AudienceBuilder_DragProfilesText_Input = "Drag profiles and segments here to create an audience in Audience Container.";
	public static final String P1_ProfileRemovalConfirmWindow_AreYouSure_Text_Locator = "Are you sure you want to remove this Profile?";
	public static final String P1_SegmentRemovalConfirmWindow_AreYouSure_Text_Locator = "Are you sure you want to remove this Segment?";
	public static final String P1_AudienceCancelation_ConfirmWindow_Message_Locator = "Are you sure you want to close the Audience Builder? Any unsaved work will be lost.";
	public static final String P1_AudienceDeleteion_ConfirmWindow_Message_Locator = "Do you want to delete audience TodeleteAud?";
	public static final List<String> P1_DemographicFilters_Filters_Input = Arrays
			.asList("Age", "Income", "Home Ownership", "Has Children",
					"Married", "Cost of Living", "Urbanicity");
	public static final String P1_AudienceDetails_NoFiltersText_Input = "There are no filters applied to this audience";
	public static final String P1_Audience_NoSearchResult_Text_Input = "There are no audiences. Use the 'Create Audience' button to create one";

	// INPUTS FOR PROFILE DETAILS PAGE
	public static final String P1_ProfileDetailsPage_PlatforReach_ToolTip_Input = "These counts include counts of browsers or devices that may be able to be reached through each of our partner platforms.  Difference in reach for each platform is determined by the overlap of the platform media and the existing audience. For the most accurate reach numbers, please contact your platform partner.";
	public static final String P1_ProfileDetailsPage_RestrictedDescription_Text_Input = "By using audience segments in connection with alcohol advertising, you represent and warrant that you will comply with the Neustar AdAdvisor Policy on Alcohol Advertising";

	// INPUT FOR VERIFY TARGETING
	public static final String P1_Targeting_Input = "The Audience Planning dashboard drives precise campaign planning and builds audiences by overlaying a wide range of attributes to meet specific campaign objectives";

	public static final String P1_AudienceBuilder_ProfileAlreadyInAudience_Text_Input = "Profile is already in Audience";
	public static final String P1_AudienceBuilder_SegmentAlreadyInAudience_Text_Input = "Segment is already in Audience";
	public static final String P1_Advertiser_DeletionToastMessage_Input = "Advertiser deleted successfully.";
	public static final String P1_Audience_DeletionToastMessage_Input = "Audience deleted successfully.";
	public static final String P1_EmptyAudience_ToastMessage_Input = "Name is required.";
	public static final String P1_Audience_AddedToastMessage_Input = "Audience added successfully.";
	public static final String P1_Advertiser_AdditionToastMessage_Input = "Advertiser added successfully.";
	public static final String P1_Advertiser_EditToastMessage_Input = "Advertiser saved successfully.";
	public static final String P1_Advertiser_DuplicateAdvertiserToastMessage_Input = "A duplicate advertiser name was found.";
	public static final String P1_Advertiser_FavoriteToastMessage_Input = "Advertiser added to favorites";
	public static final String P1_Advertisers_UnfavoriteToastMessage_Input = "Advertiser removed from favorites";
	public static final String P1_Advertiser_DeletionConfirmWindow_Text_Input = "Are you sure you want to delete this Advertiser?";
	public static final String P1_Advertiser_NoAdvertisers_Text_Input = "There are no advertisers. Use the 'Create Advertiser' button to create one";
	public static final String P1_Advertiser_InvalidAdvertiserName_Input = "SGTI_InvalidAdvertiser";
	public static final String P1_EditFilters_ExitAdjustReach_Text_Input = "Are you sure you want to exit Adjust Reach? Any unsaved progress will be lost.";
	public static final String P1_AdjustReach_Unsaved_Text_Input = "Are you sure you want to cancel? Any unsaved progress will be lost.";

	// INPUT FOR ACCOUNT
	public static final String P1_Account_PasswordTab_Empty_Input = "";
	public static final String P1_Account_PasswordTab_OldPassword_Input = "Neustar1!";
	public static final String P1_Account_PasswordTab_NewPassword_Input = "Neustar1!";
	public static final String P1_Account_PasswordTab_ConfirmNewPassword_Input = "Neustar1!";
	public static final String P1_Account_PasswordTab_IncorrectOldPassword_Input = "NppSears";
	public static final String P1_Account_PasswordTab_NewPasswordsDontMatch_ToastMessage_Input = "New passwords";
	public static final String P1_Account_PasswordTab_PasswordChanged_ToastMessage_Input = "Password changed successfully";
	public static final String P1_Account_PasswordTab_CurrentPasswordIncorrect_ToastMessage_Input = "Current Password is incorrect.";
	public static final String P1_Account_PasswordTab_FillTheInputs_ToastMessage_Input = "Please fill out form before submitting";
	public static final String P1_Account_PasswordTab_PasswordFormat_ToastMessage_Input = "Password must have at least 1 lower-case letter, 1 upper-case letter and a digit.";
	public static final String P1_Account_PasswordTab_PasswordLength_ToastMessage_Input = "Password must be between 8 and 50 characters.";

	// INPUT FOR VERIFY ADVERTISER
	public static final String P1_New_Advertiser = "SGTIAdvertiser433442";
	public static final String P_Advertiser_Value = "Coke";
}