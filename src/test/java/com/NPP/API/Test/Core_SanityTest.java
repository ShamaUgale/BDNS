package com.NPP.API.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.NPP.API.Global.CommonMethods;
import com.NPP.API.Global.CommonTestNG;
import com.NPP.API.Modules.Accounts;
import com.NPP.API.Modules.Auth;
import com.NPP.API.Modules.Product;
import com.NPP.API.Modules.Products;
import com.NPP.API.Modules.ResetLockout;
import com.NPP.API.Modules.Role;
import com.NPP.API.Modules.Search;
import com.NPP.API.Modules.User;
import com.NPP.API.Modules.UserAccountRole;
import com.jayway.restassured.response.Response;


public class Core_SanityTest extends CommonTestNG {
	
	public final static Object PRODUCT_CANNOT_BE_ASSIGNED_WARNING_MSG_CUSTOMROLES="Product Custom Roles cannot be assigned to this account.";
	public final static Object PRODUCT_CANNOT_BE_ASSIGNED_WARNING_MSG_THEMING="Product Custom Theme Management cannot be assigned to this account.";
	public final static Object USER_LOCKEDOUT_WARNING_MSG="User has been locked out due to excessive failed login attempts. Please contact your administrator to reset the lockout.";
    
	@Test(description="Verifying that a removed user U1 of an account A1 is being added to another account A2")
	public static void removeuserFromA1andAddingtoA2(){
		//CREATING TWO ACCOUNT A1 AND A2
    	String rootAccId=User.getMyAccountId(globalAuthToken,getNPPApiUser("Login_User"));
    	String A1=Accounts.createAccountAndGetId(globalAuthToken, Accounts.jsonForCreateAccount(rootAccId,getNPPApiDataProperty("statusActive")));
		String A2 = Accounts.createAccountAndGetId(globalAuthToken, Accounts.jsonForCreateAccount(rootAccId,getNPPApiDataProperty("statusActive")));
	    
	   
	    //GLOBAL SEARCH BY ACCOUNT
	    Map searchAccountParameter=new HashMap();
		searchAccountParameter.put("entityType",getNPPApiDataProperty("entityTypeAccount"));
		searchAccountParameter.put("accountId",A1);
		searchAccountParameter.put("deepSearch",true);
		searchAccountParameter.put("pageNumber","1");
		searchAccountParameter.put("pageSize","20");
	    Response res=Search.globalSearch(globalAuthToken, searchAccountParameter);
	    verifyNodevalue(res, "results.accountDocument.id[0]",Integer.parseInt(A1));
	
	    //CREATING USER U1
	    String userId= User.createUserJson(A1,getNPPApiDataProperty("roleIdAdmin"));
        res=User.CreateUser(globalAuthToken, userId);
        String uid=getNodeValue(res, "id").toString();
        String emailId=getNodeValue(res,"email").toString();
    
       //GLOBAL SEARCH BY USER    
        Map searchUserParameter=new HashMap();
 		searchUserParameter.put("entityType",getNPPApiDataProperty("entityTypeUser"));
 		searchUserParameter.put("accountId",A1);
 		searchUserParameter.put("deepSearch",true);
 		searchUserParameter.put("pageNumber","1");
 		searchUserParameter.put("pageSize","20");
 		res=Search.globalSearch(globalAuthToken, searchUserParameter);
 		verifyNodevalue(res, "results.userDocument.id[0]",Integer.parseInt(uid));

        //REMOVING USER U1 FROM ACCOUNT A1
        Map removeUserParameter=new HashMap();
		removeUserParameter.put("userId",uid);
		removeUserParameter.put("accountId",A1);
        res=UserAccountRole.removeFromAccount(globalAuthToken, removeUserParameter);
        verifyStatusCode(res,204);
        
        //ADDING U1 TO ACCOUNT A2       
        Map addUserParameter=new HashMap();
  		addUserParameter.put("email",emailId);
  		addUserParameter.put("accountId",A2);
  		addUserParameter.put("roleId",getNPPApiDataProperty("roleIdAdmin"));
	    res=UserAccountRole.addToAccount(globalAuthToken, addUserParameter);
	    verifyStatusCode(res,204);
	}
	

	@Test(description="Verifying in an account that has users of different role , meaning that 1 user has some custom role and 1 user has global role")
	public static void assigningDifferentRoles(){
		
		//CREATING AN ACCOUNT
		String rootAccId=User.getMyAccountId(globalAuthToken,getNPPApiUser("Login_User") );	
		String A1=Accounts.createAccountAndGetId(globalAuthToken, Accounts.jsonForCreateAccount(rootAccId,getNPPApiDataProperty("statusActive")));
	
		//CREATING  AN USER U1
	    String createJson= User.createUserJson(A1,getNPPApiDataProperty("roleIdAdmin"));
        Response res=User.CreateUser(globalAuthToken, createJson);
        Response productAssignResponse=Product.assignProduct(globalAuthToken, "npp", "CustomRoles", A1, true);
        String uid=getNodeValue(res, "id").toString();
        String emailId=getNodeValue(res,"email").toString();
       
         //LOGIN WITH THAT USER U1
	    String auth1=CoreLogin(emailId,"Password1!");
	   
	    //CREATING CUSTOM ROLE C1		
       String customRoleJson=Role.createCustomRole_JsonWithMandatoryDetails(A1, getNPPApiDataProperty("roleIdAdmin"),true);
       Response CustomRoleResponse=Role.createCustomRole(auth1, customRoleJson);
       String customRoleId=getNodeValue(CustomRoleResponse, "id").toString();
       	
       //CREATING NEW USER U2  AND ASSIGNING CUSTOME ROLE ID C1 TO IT 
       String createJson2= User.createUserJson(A1,customRoleId);
       res=User.CreateUser(auth1, createJson2);
       String uId1=getNodeValue(res, "id").toString();
       verifyNodevalue(res, "roleId",Integer.parseInt(customRoleId));
    
       //CREATING USER AND ASSIGNING GLOBAL ROLE ID  TO IT
       String createJson3= User.createUserJson(A1,getNPPApiDataProperty("roleIdAdmin"));
       res=User.CreateUser(auth1, createJson3);
       String uId2=getNodeValue(res, "id").toString();
       verifyNodevalue(res,"roleId",Integer.parseInt(getNPPApiDataProperty("roleIdAdmin")));
	
       }
	
       @Test(description="Verifying if the user U1 gets switched to different account A2")
       public void userSwitchingToDifferentAccount(){
    	
    	 //CREATING ACCOUNTS   
        String rootAccId=User.getMyAccountId(globalAuthToken,getNPPApiUser("Login_User"));
    	String A1=Accounts.createAccountAndGetId(globalAuthToken, Accounts.jsonForCreateAccount(rootAccId,getNPPApiDataProperty("statusActive")));
   		String A2 = Accounts.createAccountAndGetId(globalAuthToken, Accounts.jsonForCreateAccount(rootAccId,getNPPApiDataProperty("statusActive")));
	    String A3=Accounts.createAccountAndGetId(globalAuthToken, Accounts.jsonForCreateAccount(rootAccId,getNPPApiDataProperty("statusActive")));
      	String A4=Accounts.createAccountAndGetId(globalAuthToken, Accounts.jsonForCreateAccount(rootAccId,getNPPApiDataProperty("statusActive")));
     	    
   		//CREATING AN USER U1 IN A1 
	    String createJson= User.createUserJson(A1,getNPPApiDataProperty("roleIdAdmin"));
        Response res=User.CreateUser(globalAuthToken, createJson);
        String uId=getNodeValue(res, "id").toString();
        String emailId=getNodeValue(res,"email").toString();
  
       //ADDING U1 TO ACCOUNT A2   
        Map addUserParameter1=new HashMap();
        addUserParameter1.put("email",emailId);
  		addUserParameter1.put("accountId",A2);
  		addUserParameter1.put("roleId",getNPPApiDataProperty("roleIdServiceManager"));
	    res=UserAccountRole.addToAccount(globalAuthToken, addUserParameter1);
	    verifyStatusCode(res,204);
        
        //ADDING U1 TO ACCOUNT A3       
	    Map addUserParameter2=new HashMap();
	    addUserParameter2.put("email",emailId);
  		addUserParameter2.put("accountId",A3);
  		addUserParameter2.put("roleId",getNPPApiDataProperty("roleIdServiceManager"));
	    res=UserAccountRole.addToAccount(globalAuthToken, addUserParameter2);
	    verifyStatusCode(res,204);
       
  	    //ADDING U1 TO ACCOUNT A4       
	    Map addUserParameter3=new HashMap();
        addUserParameter3.put("email",emailId);
  		addUserParameter3.put("accountId",A4);
  		addUserParameter3.put("roleId",getNPPApiDataProperty("roleIdServiceManager"));
	    res=UserAccountRole.addToAccount(globalAuthToken, addUserParameter3);
	    verifyStatusCode(res,204);    
       
	   //LOGIN WITH THE USER U1
	    String Auth1=CoreLogin(emailId,getNPPApiDataProperty("password"));
  
       //SWITCHING TO ACCOUNT A2
        Response switchAccountResp=Auth.switchAccount(Auth1, A2);
        String Auth2=getNodeValue(switchAccountResp,"token").toString();
        verifyNodevalue(switchAccountResp, "accountId", Integer.parseInt(A2));
        verifyStatusCode(switchAccountResp, 200);
      }	
	
       
     @Test(description="Verifying Global search by different combination")
     public static void  globalSearch(){
    	 
    	 //CREATING ACCOUNT   
    	 String rootAccId=User.getMyAccountId(globalAuthToken,getNPPApiUser("Login_User") );
    	 Response res=Accounts.createAccount(globalAuthToken, Accounts.jsonForCreateAccount(rootAccId,getNPPApiDataProperty("statusActive")));
    	 String accId=getNodeValue(res,"id").toString();
    	 String accName=getNodeValue(res,"name").toString();
    
    	 //GLOBAL SEARCH BY ACCOUNT NAME
   	     Map searchAccountParameter=new HashMap();
   		 searchAccountParameter.put("entityType",getNPPApiDataProperty("entityTypeAccount"));
   		 searchAccountParameter.put("search",accName);
   		 searchAccountParameter.put("deepSearch",true);
   		 searchAccountParameter.put("pageNumber","1");
   		 searchAccountParameter.put("pageSize","20");
   	     res=Search.globalSearch(globalAuthToken, searchAccountParameter);
   	     verifyStatusCode(res, 200);

 	     //CREATING AN USER
 	     String createJson= User.createUserJson(accId,getNPPApiDataProperty("roleIdAdmin"));
         res=User.CreateUser(globalAuthToken, createJson);
         String uId=getNodeValue(res, "id").toString();
         String emailId=getNodeValue(res,"email").toString();
         String firstName=getNodeValue(res,"firstName").toString();   
         
         //GLOBAL SEARCH BY USER'S FIRST NAME    
         Map searchUserParameter=new HashMap();
  		 searchUserParameter.put("entityType",getNPPApiDataProperty("entityTypeUser"));
  		 searchUserParameter.put("search",firstName);
  		 searchUserParameter.put("deepSearch",true);
  		 searchUserParameter.put("pageNumber","1");
  		 searchUserParameter.put("pageSize","20");
  		 res=Search.globalSearch(globalAuthToken, searchUserParameter);  
    	 verifyStatusCode(res, 200);
   	 
        //CREATING AN ACCOUNT AND A USER WITH SAME EMAIL ID AND VERIFYING THORUGH GLOBAL SEARCH
        Response res1=Accounts.createAccount(globalAuthToken, Accounts.jsonForCreateAccount(rootAccId,getNPPApiDataProperty("statusActive")));
  	    String newaccId=getNodeValue(res1,"id").toString();
  	    String accountEmailId=getNodeValue(res1,"contactEmail").toString();
  	    
        //CREATING  AN USER
        String createJson1=User.createUserJson(accountEmailId, newaccId,getNPPApiDataProperty("roleIdAdmin")); 
        res1=User.CreateUser(globalAuthToken, createJson1);
        verifyStatusCode(res1,200);
        
        //GLOBAL SEARCH BY EMAILID 
        Map searchParameter=new HashMap();
	    searchParameter.put("search",accountEmailId);
		searchParameter.put("deepSearch",true);
		searchParameter.put("pageNumber","1");
		searchParameter.put("pageSize","20");
		res1=Search.globalSearch(globalAuthToken, searchParameter);  
 	  }
       
 
     @Test(description="Verifying that an existing user U1 in account A1 is being added to another account")
     public void traversalOfacocuntAndUser(){
    	 
    	 //CREATING ACCOUNT A1   
    	 String rootAccId=User.getMyAccountId(globalAuthToken,getNPPApiUser("Login_User") );
    	 String A1=Accounts.createAccountAndGetId(globalAuthToken, Accounts.jsonForCreateAccount(rootAccId,getNPPApiDataProperty("statusActive")));
   		
   	    //CREATING AN USER U1 IN A1
	    String createJson= User.createUserJson(A1,getNPPApiDataProperty("roleIdAdmin"));
        Response res=User.CreateUser(globalAuthToken, createJson);
        String uId=getNodeValue(res, "id").toString();
        String emailId=getNodeValue(res,"email").toString();
    
        //CREATING ACCOUNT A2
        String A2=Accounts.createAccountAndGetId(globalAuthToken, Accounts.jsonForCreateAccount(rootAccId,getNPPApiDataProperty("statusActive")));
   		
     	//ADDING U1 TO ACCOUNT A2      
	    Map addUserParameter=new HashMap();
        addUserParameter.put("email",emailId);
  		addUserParameter.put("accountId",A2);
  		addUserParameter.put("roleId",getNPPApiDataProperty("roleIdServiceManager"));
	    res=UserAccountRole.addToAccount(globalAuthToken, addUserParameter);
	    verifyStatusCode(res,204);         
       }
     
     //////////////////// ADDED BY SANTOSH /////////////////////////////////
 	@Test(description="To verify account suspend followed by delete account")
 	public static void suspendAccountFollowedByDeletion() throws InterruptedException{

 		//PREREQUISITE, CREATE ACCOUNT AND READ ITS ID
 		String rootAccountId = User.getMyAccountId(globalAuthToken, getNPPApiUser("Login_User"));
 		String createRequest = Accounts.jsonForCreateAccount(rootAccountId, getNPPApiDataProperty("statusActive"));
 		Response res = Accounts.createAccount(globalAuthToken,createRequest);
 		String accId = getNodeValue(res, "id").toString();
 		String accName = getNodeValue(res, "name").toString();
 		verifyStatusCode(res, 200);
 		wait(10);
 		
 		//LIST ACCOUNT
 		Map<String, Object> listRequest = new HashMap<String, Object>();
 		listRequest.put("search", accName);
 		listRequest.put("parentAccountId", rootAccountId);
 		res = Accounts.listAccounts(globalAuthToken, listRequest);
 		verifyNodevalue(res, "results.id[0]", Integer.parseInt(accId));
 		
 		//SEARCH THE ACCOUNT
 		Map<String, Object> searchRequest = new HashMap<String, Object>();
 		searchRequest.put("search",accName);
 		searchRequest.put("entityType",getNPPApiDataProperty("entityTypeAccount"));
 		searchRequest.put("accountId",rootAccountId);
 		res = Search.globalSearch(globalAuthToken, searchRequest);
 		Assert.assertTrue((Integer) getNodeValue(res, "totalCount")>0);
 	
 		
 		//SUSPEND THE ACCOUNT
 		Map<String, Object> SuspendRequest = new HashMap<String, Object>();
 		SuspendRequest.put("suspendReason", getNPPApiDataProperty("reasonAbuse"));
 		SuspendRequest.put("cascade", "false");
 		res = Accounts.suspendAccount(globalAuthToken, accId, SuspendRequest);
 		verifyStatusCode(res, 204);
 		
 		//VERIFY STATUS AS SUSPENDED
 		res = Accounts.getAccountDetails(globalAuthToken, accId);
 		verifyNodevalue(res, "status", getNPPApiDataProperty("statusSuspended"));
 		
 		//DELETE THE ACCOUNT
 		Map<String, Object> deleteRequest = new HashMap<String, Object>();
 		deleteRequest.put("deleteReason", getNPPApiDataProperty("reasonAbuse"));
 		deleteRequest.put("cascade", "false");
 		res = Accounts.deleteAccount(globalAuthToken, accId, deleteRequest);
 		verifyStatusCode(res, 204);
 		
 		//VERIFY STATUS AS DELETED
 		res = Accounts.getAccountDetails(globalAuthToken, accId);
 		verifyNodevalue(res, "status", getNPPApiDataProperty("statusDeleted"));
 		
 	}
 	
 	@Test(description = "Traverse an account through its life cycle")
 	public static void traverseAccountThroughLifeCycle(){
 		String rootAccountId = User.getMyAccountId(globalAuthToken, getNPPApiUser("Login_User"));
 		//CREATING A PENDING ACCOUNT
 		String createRequest = Accounts.jsonForCreateAccount(rootAccountId, getNPPApiDataProperty("statusPending"));
 		Response res = Accounts.createAccount(globalAuthToken,createRequest);
 		String accId = getNodeValue(res, "id").toString();
 		verifyStatusCode(res, 200);
 		
 		//ACTIVATING THE ACCOUNT
 		Map<String, Object> activateRequest = new HashMap<String, Object>();
 		activateRequest.put("cascade", "false");
 		res = Accounts.activateAccount(globalAuthToken, accId, activateRequest);
 		
 		//VERIFY STATUS AS ACTIVATE
 		res = Accounts.getAccountDetails(globalAuthToken, accId);
 		verifyNodevalue(res, "status", getNPPApiDataProperty("statusActive"));
 		
 		//SUSPEND THE ACCOUNT
 		Map<String, Object> SuspendRequest = new HashMap<String, Object>();
 		SuspendRequest.put("suspendReason", getNPPApiDataProperty("reasonAbuse"));
 		SuspendRequest.put("cascade", "false");
 		res = Accounts.suspendAccount(globalAuthToken, accId, SuspendRequest);
 		verifyStatusCode(res, 204);
 		
 		//VERIFY STATUS AS SUSPENDED
 		res = Accounts.getAccountDetails(globalAuthToken, accId);
 		verifyNodevalue(res, "status", getNPPApiDataProperty("statusSuspended"));
 		
 		//DELETE THE ACCOUNT
 		Map<String, Object> deleteRequest = new HashMap<String, Object>();
 		deleteRequest.put("deleteReason", getNPPApiDataProperty("reasonAbuse"));
 		deleteRequest.put("cascade", "false");
 		res = Accounts.deleteAccount(globalAuthToken, accId, deleteRequest);
 		verifyStatusCode(res, 204);
 		
 		//VERIFY STATUS AS DELETED
 		res = Accounts.getAccountDetails(globalAuthToken, accId);
 		verifyNodevalue(res, "status", "Deleted");
 	}
 	
 	@Test(description = "Create new account then creating subaccounts under that then assigning product/services in the subaccounts")
 	public void createSubaccountsAssignProductTillGrandchild() throws InterruptedException{
 		//CREATE ACCOUNT
 		String rootAccountId = User.getMyAccountId(globalAuthToken, getNPPApiUser("Login_User"));
 		String createRequestA1 = Accounts.jsonForCreateAccount(rootAccountId, getNPPApiDataProperty("statusActive"));
 		Response res = Accounts.createAccount(globalAuthToken,createRequestA1);
 		String accIdA1 = getNodeValue(res, "id").toString();
 		verifyStatusCode(res, 200);
 		wait(10);
 		
 		//CREATE ADMIN USER IN ABOVE ACCOUNT
 		String requestA1U1 = User.createUserJson(accIdA1, getNPPApiDataProperty("roleIdAdmin"));
 		res = User.CreateUser(globalAuthToken, requestA1U1);
 		verifyStatusCode(res, 200);
 		String emailIdA1U1 = getNodeValue(res, "email").toString();
 		
 		//ASSIGN PRODUCT TO ABOVE ACCOUNT
 		res = Product.assignProduct(globalAuthToken, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeSubAccounts"), accIdA1, true);
 		verifyStatusCode(res, 204);
 		Product.assignProduct(globalAuthToken, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeCustomRoles"), accIdA1, true);
 		verifyStatusCode(res, 204);
 		
 		//LOGIN TO ABOVE ACCOUNT (accIdA1) with userIdU1
 		String localAuthTokenForA1U1 = CoreLogin(emailIdA1U1, getNPPApiDataProperty("password"));
 		
 		//CREATE ACCOUNT AA1
 		String createRequestAA1 = Accounts.jsonForCreateAccount(accIdA1, getNPPApiDataProperty("statusActive"));
 		res = Accounts.createAccount(localAuthTokenForA1U1,createRequestAA1);
 		verifyStatusCode(res, 200);
 		String accIdAA1 = getNodeValue(res, "id").toString();
 		
 		//CREATE ACCOUNT AA2
 		String createRequestAA2 = Accounts.jsonForCreateAccount(accIdA1, getNPPApiDataProperty("statusActive"));
 		res = Accounts.createAccount(localAuthTokenForA1U1,createRequestAA2);
 		verifyStatusCode(res, 200);
 		String accIdAA2 = getNodeValue(res, "id").toString();
 		
 		//CREATE USER FOR ITS OWN ACCOUNT (U1 IS CREATING USER FOR ITS OWN ACCOUNT A1)
 		String requestA1U2 = User.createUserJson(accIdA1, getNPPApiDataProperty("roleIdAdmin"));
 		res = User.CreateUser(localAuthTokenForA1U1, requestA1U2);
 		verifyStatusCode(res, 200);
 		
 		//CREATE ADMIN USER FOR AA1
 		String requestAA1U1 = User.createUserJson(accIdAA1, getNPPApiDataProperty("roleIdAdmin"));
 		res = User.CreateUser(localAuthTokenForA1U1, requestAA1U1);
 		verifyStatusCode(res, 200);
 		String emailIdAA1U1 = getNodeValue(res, "email").toString();
 		
 		//CREATE ADMIN USER FOR AA2
 		String requestAA2U1 = User.createUserJson(accIdAA2, getNPPApiDataProperty("roleIdAdmin"));
 		res = User.CreateUser(localAuthTokenForA1U1, requestAA2U1);
 		verifyStatusCode(res, 200);
 		
 		//ASSIGN PRODUCTS TO AA1
 		res = Product.assignProduct(localAuthTokenForA1U1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeSubAccounts"), accIdAA1, true);
 		verifyStatusCode(res, 204);
 		res = Product.assignProduct(localAuthTokenForA1U1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeCustomRoles"), accIdAA1, true);
 		verifyStatusCode(res, 204);
 		
 		//ASSIGN PRODUCTS TO AA2
 		res = Product.assignProduct(localAuthTokenForA1U1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeSubAccounts"), accIdAA2, true);
 		verifyStatusCode(res, 204);
 		res = Product.assignProduct(localAuthTokenForA1U1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeCustomRoles"), accIdAA2, true);
 		verifyStatusCode(res, 204);
 		
 		//LOGIN TO AA1 WITH AA1U1
 		String localAuthTokenForAA1U1 = CoreLogin(emailIdAA1U1, getNPPApiDataProperty("password"));
 		
 		//CREATE ACCOUNT AAA1
 		String createRequestAAA1 = Accounts.jsonForCreateAccount(accIdAA1, getNPPApiDataProperty("statusActive"));
 		res = Accounts.createAccount(localAuthTokenForAA1U1,createRequestAAA1);
 		verifyStatusCode(res, 200);
 		String accIdAAA1 = getNodeValue(res, "id").toString();
 		
 		//CREATE ACCOUNT AAA2
 		String createRequestAAA2 = Accounts.jsonForCreateAccount(accIdAA1, getNPPApiDataProperty("statusActive"));
 		res = Accounts.createAccount(localAuthTokenForAA1U1,createRequestAAA2);
 		verifyStatusCode(res, 200);
 		String accIdAAA2 = getNodeValue(res, "id").toString();
 		
 		//CREATE ADMIN USER(AAA1U1) IN AAA1
 		String requestAAA1U1 = User.createUserJson(accIdAA1, getNPPApiDataProperty("roleIdAdmin"));
 		res = User.CreateUser(localAuthTokenForAA1U1, requestAAA1U1);
 		verifyStatusCode(res, 200);
 		
 		//CREATE ADMIN USER(AAA2U1) IN AAA2
 		String requestAAA2U1 = User.createUserJson(accIdAAA2, getNPPApiDataProperty("roleIdAdmin"));
 		res = User.CreateUser(localAuthTokenForAA1U1, requestAAA2U1);
 		verifyStatusCode(res, 200);
 		
 		//ASSIGN PRODUCTS TO AAA1
 		res = Product.assignProduct(localAuthTokenForAA1U1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeSubAccounts"), accIdAAA1, true);
 		verifyStatusCode(res, 204);
 		Product.assignProduct(localAuthTokenForAA1U1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeCustomRoles"), accIdAAA1, true);
 		verifyStatusCode(res, 204);
 		
 		//ASSIGN PRODUCTS TO AAA2
 		Product.assignProduct(localAuthTokenForAA1U1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeSubAccounts"), accIdAAA2, true);
 		verifyStatusCode(res, 204);
 		Product.assignProduct(localAuthTokenForAA1U1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeCustomRoles"), accIdAAA2, true);
 		verifyStatusCode(res, 204);
 		
 	}
 	
 	@Test(description="Create account and sub accounts in it and create custom in subaccount")
 	public void createAccountSubAccountsAndCreateCustomRolesInIt(){
 		//CREATE ACCOUNT
 		String rootAccountId = User.getMyAccountId(globalAuthToken, getNPPApiUser("Login_User"));
 		String createRequestA1 = Accounts.jsonForCreateAccount(rootAccountId, getNPPApiDataProperty("statusActive"));
 		Response res = Accounts.createAccount(globalAuthToken,createRequestA1);
 		String accIdA1 = getNodeValue(res, "id").toString();
 		verifyStatusCode(res, 200);
 		
 		//CREATE ADMIN USER IN ABOVE ACCOUNT
 		String requestA1U1 = User.createUserJson(accIdA1, getNPPApiDataProperty("roleIdAdmin"));
 		res = User.CreateUser(globalAuthToken, requestA1U1);
 		verifyStatusCode(res, 200);
 		String emailIdA1U1 = getNodeValue(res, "email").toString();
 		
 		//ASSIGN PRODUCT TO ABOVE ACCOUNT
 		res = Product.assignProduct(globalAuthToken, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeSubAccounts"), accIdA1, true);
 		verifyStatusCode(res, 204);
 		Product.assignProduct(globalAuthToken, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeCustomRoles"), accIdA1, true);
 		verifyStatusCode(res, 204);
 		
 		//LOGIN TO ABOVE ACCOUNT (accIdA1) with userIdU1
 		String localAuthTokenForA1U1 = CoreLogin(emailIdA1U1, getNPPApiDataProperty("password"));
 		
 		//CREATE ACCOUNT AA1
 		String createRequestAA1 = Accounts.jsonForCreateAccount(accIdA1, getNPPApiDataProperty("statusActive"));
 		res = Accounts.createAccount(localAuthTokenForA1U1,createRequestAA1);
 		verifyStatusCode(res, 200);
 		String accIdAA1 = getNodeValue(res, "id").toString();
 		
 		//CREATE ACCOUNT AA2
 		String createRequestAA2 = Accounts.jsonForCreateAccount(accIdA1, getNPPApiDataProperty("statusActive"));
 		res = Accounts.createAccount(localAuthTokenForA1U1,createRequestAA2);
 		verifyStatusCode(res, 200);
 		String accIdAA2 = getNodeValue(res, "id").toString();
 		
 		//CREATE USER FOR ITS OWN ACCOUNT (U1 IS CREATING USER FOR ITS OWN ACCOUNT A1)
 		String requestA1U2 = User.createUserJson(accIdA1, getNPPApiDataProperty("roleIdAdmin"));
 		res = User.CreateUser(localAuthTokenForA1U1, requestA1U2);
 		verifyStatusCode(res, 200);
 		
 		//CREATE ADMIN USER FOR AA1
 		String requestAA1U1 = User.createUserJson(accIdAA1, getNPPApiDataProperty("roleIdAdmin"));
 		res = User.CreateUser(localAuthTokenForA1U1, requestAA1U1);
 		verifyStatusCode(res, 200);
 		String emailIdAA1U1 = getNodeValue(res, "email").toString();
 		
 		//CREATE ADMIN USER FOR AA2
 		String requestAA2U1 = User.createUserJson(accIdAA2, getNPPApiDataProperty("roleIdAdmin"));
 		res = User.CreateUser(localAuthTokenForA1U1, requestAA2U1);
 		verifyStatusCode(res, 200);
 		
 		//ASSIGN PRODUCTS TO AA1
 		res = Product.assignProduct(localAuthTokenForA1U1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeSubAccounts"), accIdAA1, true);
 		verifyStatusCode(res, 204);
 		res = Product.assignProduct(localAuthTokenForA1U1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeCustomRoles"), accIdAA1, true);
 		verifyStatusCode(res, 204);
 		
 		//ASSIGN PRODUCTS TO AA2
 		res = Product.assignProduct(localAuthTokenForA1U1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeSubAccounts"), accIdAA2, true);
 		verifyStatusCode(res, 204);
 		res = Product.assignProduct(localAuthTokenForA1U1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeCustomRoles"), accIdAA2, true);
 		verifyStatusCode(res, 204);
 		
 		//LOGIN TO AA1 WITH AA1U1
 		String localAuthTokenForAA1U1 = CoreLogin(emailIdAA1U1, getNPPApiDataProperty("password"));
 		
 		//CREATE CUSTOM ROLE IN AA1
 		String customRoleRequest = Role.createCustomRoleJson_AdminMinusTheme(accIdAA1, getNPPApiDataProperty("roleIdAdmin"));
 		res = Role.createCustomRole(localAuthTokenForAA1U1, customRoleRequest);
 		verifyStatusCode(res, 200);
 		
 		//CREATE ACCOUNT AAA1
 		String createRequestAAA1 = Accounts.jsonForCreateAccount(accIdAA1, getNPPApiDataProperty("statusActive"));
 		res = Accounts.createAccount(localAuthTokenForAA1U1,createRequestAAA1);
 		verifyStatusCode(res, 200);
 		String accIdAAA1 = getNodeValue(res, "id").toString();
 		
 		//CREATE ACCOUNT AAA2
 		String createRequestAAA2 = Accounts.jsonForCreateAccount(accIdAA1, getNPPApiDataProperty("statusActive"));
 		res = Accounts.createAccount(localAuthTokenForAA1U1,createRequestAAA2);
 		verifyStatusCode(res, 200);
 		String accIdAAA2 = getNodeValue(res, "id").toString();
 		
 		//CREATE ADMIN USER(AAA1U1) IN AAA1
 		String requestAAA1U1 = User.createUserJson(accIdAA1, getNPPApiDataProperty("roleIdAdmin"));
 		res = User.CreateUser(localAuthTokenForAA1U1, requestAAA1U1);
 		verifyStatusCode(res, 200);
 		
 		//CREATE ADMIN USER(AAA2U1) IN AAA2
 		String requestAAA2U1 = User.createUserJson(accIdAAA2, getNPPApiDataProperty("roleIdAdmin"));
 		res = User.CreateUser(localAuthTokenForAA1U1, requestAAA2U1);
 		verifyStatusCode(res, 200);
 		
 		//ASSIGN PRODUCTS TO AAA1
 		res = Product.assignProduct(localAuthTokenForAA1U1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeSubAccounts"), accIdAAA1, true);
 		verifyStatusCode(res, 204);
 		Product.assignProduct(localAuthTokenForAA1U1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeCustomRoles"), accIdAAA1, true);
 		verifyStatusCode(res, 204);
 		
 		//ASSIGN PRODUCTS TO AAA2
 		Product.assignProduct(localAuthTokenForAA1U1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeSubAccounts"), accIdAAA2, true);
 		verifyStatusCode(res, 204);
 		Product.assignProduct(localAuthTokenForAA1U1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeCustomRoles"), accIdAAA2, true);
 		verifyStatusCode(res, 204);
 		
 	}
 	
 	@Test(description = "Create a subsccount in to an account to whick subaccount product is not assigned")
 	public void verifySubAccountIsNotCreatedIfSubAccountProductIsNotAssigned(){
 		
 		//CREATE ACCOUNT
 		String rootAccountId = User.getMyAccountId(globalAuthToken, getNPPApiUser("Login_User"));
 		String createRequestA1 = Accounts.jsonForCreateAccount(rootAccountId, getNPPApiDataProperty("statusActive"));
 		Response res = Accounts.createAccount(globalAuthToken,createRequestA1);
 		String accIdA1 = getNodeValue(res, "id").toString();
 		verifyStatusCode(res, 200);
 		
 		//CREATE ADMIN USER IN ABOVE ACCOUNT
 		String requestA1U1 = User.createUserJson(accIdA1, getNPPApiDataProperty("roleIdAdmin"));
 		res = User.CreateUser(globalAuthToken, requestA1U1);
 		verifyStatusCode(res, 200);
 		String emailIdA1U1 = getNodeValue(res, "email").toString();
 		
 		//ASSIGN PRODUCT TO ABOVE ACCOUNT (NOT THE SUBACCOUNT)
 		Product.assignProduct(globalAuthToken, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeCustomRoles"), accIdA1, true);
 		//verifyStatusCode(res, 204);
 		
 		//CREATE SUBACCOUNT AND OBSERVE THE ERROR
 		String createRequestA2 = Accounts.jsonForCreateAccount(accIdA1, getNPPApiDataProperty("statusActive"));
 		res = Accounts.createAccount(globalAuthToken,createRequestA2);
 		verifyStatusCode(res, 400);//NEED TO CONFIRM ABOUT THIS STATUS CODE
 		
 		//LOGIN TO ABOVE ACCOUNT (accIdA1) with userIdU1
 		String localAuthTokenForA1U1 = CoreLogin(emailIdA1U1, getNPPApiDataProperty("password"));
 		
 		//CREATE SUBACCOUNT AFTER LOGIN AND OBSERVE THE ERROR
 		String createRequestA3 = Accounts.jsonForCreateAccount(accIdA1, getNPPApiDataProperty("statusActive"));
 		res = Accounts.createAccount(localAuthTokenForA1U1,createRequestA3);
 		verifyStatusCode(res, 400);//NEED TO CONFIRM ABOUT THIS STATUS CODE
 	}
//************************************************************************************************************* 	
// ADDED BY SEEMA
 	
      @Test(description="Verify User Lockout functionality", groups="sanityFlow")
      public void ResetLogOut(){
      //TO CREATE AN ACCOUNT
	  String ParentAccId=Accounts.createAccountAndGetId (globalAuthToken, Accounts.jsonForCreateAccount(getNPPApiDataProperty("statusActive")));
	  	 
	  //TO CREATE AN ADMIN USER FOR ACCOUNT
	  Response Parent_AccountUser=User.CreateUser(globalAuthToken, User.CreateUserWithAllInputs_json(getNPPApiDataProperty("account_Title"), getNPPApiDataProperty("account_Phone"), getNPPApiDataProperty("account_Mobile"), getNPPApiDataProperty("account_Fax"), ParentAccId, getNPPApiDataProperty("roleIdAdmin")));
	  String UserId=getNodeValue(Parent_AccountUser, "id").toString();
      String email=getNodeValue(Parent_AccountUser, "email").toString();
      String valid_Password="Password1!";
      String invalid_Password="invalidPassword";
	 
      //TO LOCK USER BY FIRING WRONG PASSWORD FIVE TIMES
      for (int i =0; i<5 ; i++)
      {
        Response loginWithInvalidPwd=Auth.login(email, invalid_Password, true);
      }
      
      Response loginWithInvalidPwd=Auth.login(email, valid_Password, true);
      verifyNodevalue(loginWithInvalidPwd, "value[0]", USER_LOCKEDOUT_WARNING_MSG);
     
      //TO RESET LOCKED USER           
      Response ResetLockoutRes=ResetLockout.resetLockout(globalAuthToken, UserId);
      verifyStatusCode(ResetLockoutRes, 204);
        
      //TO VERIFY IF USER CAN LOGIN TO APPLICATION AFTER RELEASING THE LOCK
      Response LoginWithValidPwd=Auth.login(email, valid_Password, true);
      verifyStatusCode(LoginWithValidPwd, 200);
    		  
      }
//************************************************************************************************************************************************************* 
	  @Test(description="Verify that a user is in multiple account having different roles", groups="sanityFlow")
      public void UserInMultipleAccountsWithDifferentRoles(){
	  
	   //TO CREATE AN ACCOUNT
	   String ParentAccId=Accounts.createAccountAndGetId (globalAuthToken, Accounts.jsonForCreateAccount(getNPPApiDataProperty("statusActive")));
	  	  
	   //TO CREATE AN ADMIN USER FOR ACCOUNT
	   Response Parent_AccountUser=User.CreateUser(globalAuthToken, User.CreateUserWithAllInputs_json(getNPPApiDataProperty("account_Title"), getNPPApiDataProperty("account_Phone"), getNPPApiDataProperty("account_Mobile"), getNPPApiDataProperty("account_Fax"), ParentAccId, getNPPApiDataProperty("roleIdAdmin")));
	   String UserId=getNodeValue(Parent_AccountUser, "id").toString();
	   String ParentUser_Email=getNodeValue(Parent_AccountUser, "email").toString();
		   
	   //TO CREATE FIRST SUB ACCOUNT
	   String SubAccId1=Accounts.createAccountAndGetId(globalAuthToken, Accounts.jsonForCreateAccount( getNPPApiDataProperty("statusActive")));
      
	   //TO CREATE AN ADMIN USER FOR FIRST SUB ACCOUNT
	   Response SubAccount_User1=User.CreateUser(globalAuthToken,User.CreateUserWithAllInputs_json(getNPPApiDataProperty("account_Title"), getNPPApiDataProperty("account_Phone"), getNPPApiDataProperty("account_Mobile"), getNPPApiDataProperty("account_Fax"), SubAccId1, getNPPApiDataProperty("roleIdAdmin")));
       String SubAccUser1=getNodeValue(SubAccount_User1,"id").toString();
	   String SubAccUser1_Email=(String) getNodeValue(SubAccount_User1, "email");
	  	   
	   //TO CREATE SECOND ACCOUNT 
	   String SubAccId2=Accounts.createAccountAndGetId (globalAuthToken, Accounts.jsonForCreateAccount(getNPPApiDataProperty("statusActive")));
	   
	   //TO CREATE AN ADMIN USER FOR SECOND SUB ACCOUNT
	   Response SubAccount_User2=User.CreateUser(globalAuthToken,User.CreateUserWithAllInputs_json(getNPPApiDataProperty("account_Title"), getNPPApiDataProperty("account_Phone"), getNPPApiDataProperty("account_Mobile"), getNPPApiDataProperty("account_Fax"), SubAccId2, getNPPApiDataProperty("roleIdAdmin")));
       String SubAccUser2=getNodeValue(SubAccount_User2, "id").toString();
	   String SubAccUser2_Email=getNodeValue(SubAccount_User2, "email").toString();	   
	   
	   //ADD PARENT USER TO FIRST SUB ACCOUNT AS ADMINISTRATOR
	   Map <String , Object> addToAccQueryParam1= new HashMap <String , Object>(); 
       addToAccQueryParam1.put("email", ParentUser_Email);
       addToAccQueryParam1.put("accountId", SubAccId1);
       addToAccQueryParam1.put("roleId", 1002);
	   Response addToSubAccount1=Accounts.addToAccount(globalAuthToken, addToAccQueryParam1);
	   verifyStatusCode(addToSubAccount1, 204);
	
        //ADD FIRST SUB ACCOUNTT USER TO ITS SUB ACCOUNT AS READONLY
	    Map <String , Object> addToAccQueryParam2= new HashMap <String , Object>(); 
	    addToAccQueryParam2.put("email", SubAccUser1_Email);
	    addToAccQueryParam2.put("accountId", SubAccId2);
	    addToAccQueryParam2.put("roleId", 1003);
	    Response addToSubAccount2=Accounts.addToAccount(globalAuthToken, addToAccQueryParam2);
	    verifyStatusCode(addToSubAccount2, 204);
  
	    //TO LIST USER TO VERIFY ADMINISTATOR ROLE FOR ADDED USER 
		Map <String , Object> listUserQueryParam1= new HashMap <String , Object>(); 
		listUserQueryParam1.put("accountId", SubAccId1);
		listUserQueryParam1.put("deepSearch", false);
		listUserQueryParam1.put("sortField","email.raw");
		listUserQueryParam1.put("sortOrder",true);
		listUserQueryParam1.put("pageNumber", 1);
		listUserQueryParam1.put("pageSize", 20);
		listUserQueryParam1.put("roleId", 1002);
		listUserQueryParam1.put("appId" , "npp");
		Response ListUserForSubAcc=User.listUsers(globalAuthToken, listUserQueryParam1);
		List Expected_EmailId1=getNodeValues(ListUserForSubAcc, "results.email");
		isInList(Expected_EmailId1, ParentUser_Email );
		List allRoleNameslist1=getNodeValues(ListUserForSubAcc, "results.roleName");
		isInList(allRoleNameslist1, "Customer Service");
	    
	
    	//TO LIST USER TO VERIFY READONLY ROLE FOR ADDED USER
		Map <String , Object> listUserqueryParam2= new HashMap <String , Object>(); 
		listUserqueryParam2.put("accountId", SubAccId2);
		listUserqueryParam2.put("deepSearch", false);
		listUserqueryParam2.put("sortField","email.raw");
		listUserqueryParam2.put("sortOrder",true);
		listUserqueryParam2.put("pageNumber", 1);
		listUserqueryParam2.put("pageSize", 20);
		listUserqueryParam2.put("roleId", 1003);
		listUserqueryParam2.put("appId" , "npp");
		Response ListUserForSubAcc2=User.listUsers(globalAuthToken, listUserqueryParam2);
		List Expected_EmailId2=getNodeValues(ListUserForSubAcc2, "results.email");
		isInList(Expected_EmailId2, SubAccUser1_Email );
		List allRoleNameslist2=getNodeValues(ListUserForSubAcc2, "results.roleName");
		isInList(allRoleNameslist2, "Read Only");
		
        }
	  
//***********************************************************************************************************************************************************
	   	   
	   @Test(description="To verify if the service is removed for an account then it can not be assigned to for all its sub accounts", groups="sanityFlow")
       public void SuspendedServiceOfParentAccCannotBeAssignedToChildAccount(){
   	
       //TO CREATE AN ACCOUNT
	   String ParentAccId=Accounts.createAccountAndGetId (globalAuthToken, Accounts.jsonForCreateAccount(getNPPApiDataProperty("statusActive")));
	  
	   //TO CREATE AN ADMIN USER FOR ACCOUNT
	   Response Parent_AccountUser=User.CreateUser(globalAuthToken, User.CreateUserWithAllInputs_json(getNPPApiDataProperty("account_Title"), getNPPApiDataProperty("account_Phone"), getNPPApiDataProperty("account_Mobile"), getNPPApiDataProperty("account_Fax"), ParentAccId, getNPPApiDataProperty("roleIdAdmin")));
	   String UserId=getNodeValue(Parent_AccountUser, "id").toString();
	   String ParentUser_Email=getNodeValue(Parent_AccountUser, "email").toString();	
	  
	   //TO ASSIGN PRODUCTS TO THIS ACCOUNT
  	   Response assignProducts_SubAccounts1=Products.assignProduct(globalAuthToken, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeSubAccounts"), ParentAccId, true);
  	   verifyStatusCode(assignProducts_SubAccounts1, 204);
  	  
  	   Response assignProducts_CustomRoles1=Products.assignProduct(globalAuthToken, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeCustomRoles"), ParentAccId, true); 
  	   verifyStatusCode(assignProducts_CustomRoles1, 204);
     
  	   Response assignProducts_Theming1=Products.assignProduct(globalAuthToken, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeTheming"), ParentAccId, true); 
  	   verifyStatusCode(assignProducts_Theming1, 204);
  	  	
	   //TO CREATE FIRST SUB ACCOUNT
	   String localAuthToken1=CoreLogin(ParentUser_Email, "Password1!");
       String SubAccId1=Accounts.createAccountAndGetId (localAuthToken1, Accounts.jsonForCreateAccount(getNPPApiDataProperty("statusActive")));
        
 
       //TO CREATE AN ADMIN USER FOR FIRST SUB ACCOUNT
       Response SubAccount_User1=User.CreateUser(localAuthToken1,User.CreateUserWithAllInputs_json(getNPPApiDataProperty("account_Title"), getNPPApiDataProperty("account_Phone"), getNPPApiDataProperty("account_Mobile"), getNPPApiDataProperty("account_Fax"),SubAccId1, getNPPApiDataProperty("roleIdAdmin")));
       String SubAccUserId1=getNodeValue(SubAccount_User1,"id").toString();
       String SubAccUser1_Email=(String) getNodeValue(SubAccount_User1, "email");
 		
		  		
	   //TO ASSIGN PRODUCTS TO SUB ACCOUNT
	   Response assignProducts_SubAccounts2=Products.assignProduct(localAuthToken1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeSubAccounts"), SubAccId1, true);
	   verifyStatusCode(assignProducts_SubAccounts2, 204);
		
	   Response assignProducts_CustomRoles2=Products.assignProduct(localAuthToken1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeCustomRoles"), SubAccId1, true); 
	   verifyStatusCode(assignProducts_CustomRoles2, 204);
	
	   Response assignProducts_Theming2=Products.assignProduct(localAuthToken1, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeTheming"), SubAccId1, true); 
	   verifyStatusCode(assignProducts_Theming2, 204);
	   
	    //TO CREATE SUBACCOUNTS TO FIRST SUB ACCOUNTS
	    String localAuthToken2=CoreLogin(SubAccUser1_Email, "Password1!");
	    String SubAccId2=Accounts.createAccountAndGetId (localAuthToken2, Accounts.jsonForCreateAccount(getNPPApiDataProperty("statusActive")));
	   
	    //LOGIN WITH PARENT ACCOUNT AND REMOVE PRODUCT
 		Map <String , Object> queryParameter=new HashMap <String , Object>();
 		Response removeCustomRolesFromParentAcc=Products.removeProducts(globalAuthToken, queryParameter,getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeCustomRoles"), SubAccId1 , true);
 		verifyStatusCode(removeCustomRolesFromParentAcc, 204);

 		
 		Map <String,Object> queryParameter1=new HashMap <String, Object>();
 		queryParameter1.put("forceCascade", "isforceCascade");
 		Response removeThemingFromParentAcc=Products.removeProducts(globalAuthToken, queryParameter1,getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeTheming"), SubAccId1, true);
 		verifyStatusCode(removeThemingFromParentAcc, 204);

	  	//TO ASSIGN PRODUCTS TO CHILD ACCOUNT
	  	Response assignCustomRolesToSubAcc=Products.assignProduct(localAuthToken2, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeCustomRoles"), SubAccId2, true);
		verifyStatusCode(assignCustomRolesToSubAcc, 400);
        verifyNodevalue(assignCustomRolesToSubAcc, "value[0]", PRODUCT_CANNOT_BE_ASSIGNED_WARNING_MSG_CUSTOMROLES);
	  	
	  		
	  	Response assignThemingToSubAcc=Products.assignProduct(localAuthToken2, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeTheming"), SubAccId2, true); 
	  	verifyStatusCode(assignThemingToSubAcc, 400);
	  	verifyNodevalue(assignThemingToSubAcc, "value[0]", PRODUCT_CANNOT_BE_ASSIGNED_WARNING_MSG_THEMING); 
		}
	   
//**********************************************************************************************************************************************************
     
        @Test(description="Verify the cascading effect of suspension and deletion of Account", groups="sanityFlows")
	       
	    public void ToVerifyCasecadingEffectOfSuspensionAndDeletionAccrossAccounts(){
	    
        //TO CREATE AN ACCOUNT
	    String ParentAccId=Accounts.createAccountAndGetId (globalAuthToken, Accounts.jsonForCreateAccount(getNPPApiDataProperty("statusActive")));
	    
	    //TO CREATE AN ADMIN USER FOR ACCOUNT
	    Response Parent_AccountUser=User.CreateUser(globalAuthToken, User.CreateUserWithAllInputs_json(getNPPApiDataProperty("account_Title"), getNPPApiDataProperty("account_Phone"), getNPPApiDataProperty("account_Mobile"), getNPPApiDataProperty("account_Fax"), ParentAccId, getNPPApiDataProperty("roleIdAdmin")));
        String UserId=getNodeValue(Parent_AccountUser, "id").toString();
	    String email=getNodeValue(Parent_AccountUser, "email").toString();
	    
	    //TO ASSIGN PRODUCTS TO THIS ACCOUNT
        Response assignProductToAcc=Products.assignProduct(globalAuthToken, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeSubAccounts"), ParentAccId, true);
        verifyStatusCode(assignProductToAcc, 204);
		
		//TO CREATE AN ACTIVE CHILD ACCOUNT
		String localAuthToken1=CoreLogin(email,"Password1!");
		String SubAccId=Accounts.createAccountAndGetId (localAuthToken1, Accounts.jsonForCreateAccount(ParentAccId , getNPPApiDataProperty("statusActive")));
		
	    	
	    //TO SUSPEND PARENT ACCOUNT
	   	Map <String, Object> queryParam=new HashMap <String, Object> ();
	    queryParam.put("suspendReason", getNPPApiDataProperty("reasonOther"));
	    queryParam.put("cascade", true );
	    Response suspendParentAccount=Accounts.suspendAccount(globalAuthToken, ParentAccId, queryParam);
	    verifyStatusCode(suspendParentAccount,204);  
		
        //TO GET ACCOUNT DETAILS OF PARENT ACCOUNT
		Response getAccDetailsOfParentAcc=Accounts.getAccountDetails(globalAuthToken, ParentAccId);
		verifyStatusCode(getAccDetailsOfParentAcc, 200);
		verifyNodevalue(getAccDetailsOfParentAcc, "status", "Suspended");
		
		//TO GET ACCOUNT DETAILS OF ACTIVE CHILD ACCOUNT
		Response getAccDetailsOfSubAcc=Accounts.getAccountDetails(globalAuthToken, SubAccId); 
		verifyStatusCode(getAccDetailsOfSubAcc, 200);
		verifyNodevalue(getAccDetailsOfSubAcc, "status", "Suspended");
	   
		//TO DELETE PARENT ACCOUNT 
		Map <String, Object> deleteQueryParam = new HashMap <String, Object>();
		deleteQueryParam.put("deleteReason" , getNPPApiDataProperty("reasonAbuse"));
	    deleteQueryParam.put("cascade", true );
	    Response deleteParentAccount=Accounts.deleteAccount(globalAuthToken, ParentAccId, deleteQueryParam);
	    verifyStatusCode(deleteParentAccount, 204);
		
	     //TO GET ACCOUNT DETAILS OF PARENT ACCOUT
      	 Response getDetailsOfParentAcc=Accounts.getAccountDetails(globalAuthToken, ParentAccId);
      	 verifyStatusCode(getDetailsOfParentAcc, 200);
       	 verifyNodevalue(getDetailsOfParentAcc, "status", "Deleted");
      	 	   
      	 //TO GET ACCOUNT DETAILS OF ACTIVE CHILD ACCOUT
      	 Response getDetailsOfSubAcc=Accounts.getAccountDetails(globalAuthToken, SubAccId); 
      	 verifyStatusCode(getDetailsOfSubAcc, 200);
      	 verifyNodevalue(getDetailsOfParentAcc, "status", "Deleted");  
}
     
//*********************************************************************************************************************
        @Test(description="Verify when parent account is suspended then its all sub account should also  be suspended irrespective of status of sub accounts", groups="sanityFlow")
 	    public void CascadingEffectofSuspensionOfAccounts(){

 	   //TO CREATE AN ACCOUNT
 	   String ParentAccId=Accounts.createAccountAndGetId (globalAuthToken, Accounts.jsonForCreateAccount(getNPPApiDataProperty("statusActive")));
 	  
 	   //TO CREATE AN ADMIN USER FOR ACCOUNT
 	   Response Parent_AccountUser=User.CreateUser(globalAuthToken, User.CreateUserWithAllInputs_json(getNPPApiDataProperty("account_Title"), getNPPApiDataProperty("account_Phone"), getNPPApiDataProperty("account_Mobile"), getNPPApiDataProperty("account_Fax"), ParentAccId, getNPPApiDataProperty("roleIdAdmin")));
 	   String UserId=getNodeValue(Parent_AccountUser, "id").toString();
 	   String ParentUser_Email=(String) getNodeValue(Parent_AccountUser, "email");
 	  
 		//TO ASSIGN PRODUCTS TO THIS ACCOUNT
 		Response assignProducts_SubAccounts=Products.assignProduct(globalAuthToken, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeSubAccounts"), ParentAccId, true);
		verifyStatusCode(assignProducts_SubAccounts, 204); 
 		
 		Response assignProducts_CustomRoles=Products.assignProduct(globalAuthToken, getNPPApiDataProperty("appIdNPP"), getNPPApiDataProperty("productCodeCustomRoles"), ParentAccId, true); 
 		System.out.println(assignProducts_CustomRoles.asString());
 		verifyStatusCode(assignProducts_CustomRoles, 204);  
 		
 	    //TO CREATE AN ACTIVE CHILD ACCOUNT 
        String Active_SubAccount=Accounts.createAccountAndGetId (globalAuthToken,Accounts.jsonForCreateAccount( ParentAccId, getNPPApiDataProperty("statusActive")));
 	
 	    //TO CREATE AN CHILD ACCOUNT WITH PENDING STATUS
 		String Pending_SubAccount=Accounts.createAccountAndGetId (globalAuthToken, Accounts.jsonForCreateAccount( ParentAccId, getNPPApiDataProperty("statusPending")));
 		
 	   //TO SUSPEND PARENT ACCOUNT
 	  	Map<String, Object> queryParam=new HashMap<String, Object>();
 	  	queryParam.put("suspendReason", getNPPApiDataProperty("reasonNonpayment"));
 	
 	    queryParam.put("cascade", true );
 	    Response suspendParentAcc=Accounts.suspendAccount(globalAuthToken, ParentAccId, queryParam);
 	    verifyStatusCode(suspendParentAcc,204);  
 	    
 	    //TO GET ACCOUNT DETAILS OF PARENT ACCOUT
 		Response detailsOfParentAcc=Accounts.getAccountDetails(globalAuthToken, ParentAccId);
 		verifyStatusCode(detailsOfParentAcc, 200);
 		verifyNodevalue(detailsOfParentAcc, "status", "Suspended");
 		
 		//TO GET ACCOUNT DETAILS OF ACTIVE CHILD ACCOUT
 		Response detailsOfActiveSubAcc=Accounts.getAccountDetails(globalAuthToken, Active_SubAccount); 
 		verifyStatusCode(detailsOfActiveSubAcc, 200);
 		verifyNodevalue(detailsOfActiveSubAcc, "status", "Suspended");
 		
 		//TO GET ACCOUNT DETAILS OF PENDING CHILD ACCOUT
 		Response detailsOfPendingSubAcc=Accounts.getAccountDetails(globalAuthToken, Pending_SubAccount);
 		verifyStatusCode(detailsOfPendingSubAcc, 200);
 		verifyNodevalue(detailsOfPendingSubAcc, "status", "Pending");		

   }  
        
        @Test(description="Create an account , create a user into it, suspend a user and then delete a user")
        public void suspendAndDeleteAUser(){
        	String suLoginAuth=CommonMethods.CoreLogin(getNPPApiUser("Login_User"), getNPPApiUser("Login_password"));
        	String A1=Accounts.createAccountAndGetId(suLoginAuth, Accounts.jsonForCreateAccount(User.getMyAccountId(suLoginAuth,getNPPApiUser("Login_User")),getNPPApiDataProperty("statusActive")));
        	String userJson= User.createUserJson(A1,getNPPApiDataProperty("roleIdAdmin"));
             String userId=User.CreateUserAndgetId(suLoginAuth, userJson);

           verifyStatusCode(User.suspendUser(suLoginAuth,userId), 204) ;
            
            verifyStatusCode(User.deleteUser(suLoginAuth, userId), 204); 
        }
        
        @Test(description="Create an account , create a user into it, change the password and verify login with new password")
        public void simulateChangePasswordFlow(){
        	String suLoginAuth=CommonMethods.CoreLogin(getNPPApiUser("Login_User"), getNPPApiUser("Login_password"));
        	String A1=Accounts.createAccountAndGetId(suLoginAuth, Accounts.jsonForCreateAccount(User.getMyAccountId(suLoginAuth,getNPPApiUser("Login_User")),getNPPApiDataProperty("statusActive")));
        	 String userJson= User.createUserJson(A1,getNPPApiDataProperty("roleIdAdmin"));
             Response userRes= User.CreateUser(suLoginAuth, userJson);
             String emailId=getNodeValue(userRes,"email").toString();   
             String newLoginAuth=CoreLogin(emailId,"Password1!");
             Response updatePassword= Auth.updatePassword(newLoginAuth, "Password1!", "Password12!");
             verifyStatusCode(updatePassword, 204);
             Response newLogin=Auth.login(emailId, "Password12!", false);
             verifyStatusCode(newLogin, 200);           
        }
        

        
}





