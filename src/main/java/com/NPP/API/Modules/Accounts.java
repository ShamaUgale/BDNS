package com.NPP.API.Modules;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;


import com.NPP.API.Global.CommonMethods;
import com.jayway.restassured.response.Response;

public class Accounts extends CommonMethods{

	//TO GET ACCOUNT DETAILS
	public static Response getAccountDetails(String authToken, String id){
		String URI = generateURIWithPathParameter(getNPPEnpoint("getAccountDetails_endPoint"),id);
		Response res = getWithPathParameters(authToken,URI);
		return res;		
	}
	
    //TO GET PARENT ACCOUNT IDs
    public static Response getParentAccountIds (String authToken,String id)
    {
    	String URI=generateURIWithPathParameter(getNPPEnpoint("getParentAccountIds_endPoint"),id);
    	Response res=getWithPathParameters(authToken, URI);
        return res; 
    }
    
    //ADD TO ACCOUNT
    public static Response addToAccount(String authToken, Map queryParameters){
    	Response res=postWithQueryParameters(authToken, queryParameters, getNPPEnpoint("addToAccount_endPoint"));
    	return res;
    }
    
    //To GET AN ACCOUNT HIERARCHY
    public static Response getAccountHierarchy (String authToken,String id, Map queryParameters)
    {
        String URI=generateURIWithPathParameter(getNPPEnpoint("getAccountHierarchy_endPoint"),id);
    	Response res=getWithQueryparameters(authToken, queryParameters, URI);
        return res;
    }
    
    public static Response removeFromAccount(String authToken,Map user_removequeyParameter)
    {
          Response res=deleteWithQueryParameters(authToken, user_removequeyParameter,getNPPEnpoint("userAccountRole_endpoint"));
          return res;
    }
    
   
    public static String jsonForCreateAccount(String status){            
        String name = "Test Account" + new DateTime( DateTimeZone.UTC );
        String email = UUID.randomUUID().toString()+"@test.com";
        String json = "{\"name\":\""+name+"\",\"status\":\""+status+"\",\"shortname\":\""+name+"\",\"contactPerson\":\""+getNPPApiDataProperty("contactPerson")+"\",\"contactEmail\":\""+email+"\",\"contactPhone\":\"9764\",\"address\":{\"address1\":\"Line 1\",\"address2\":\"Line 2\",\"city\":\"NY\",\"stateOrProvince\":\"NY\",\"country\":\"US\",\"postalCode\":\"2\"},\"billingAddress\":{\"address1\":\"Line 1\",\"address2\":\"Line 2\",\"city\":\"NY\",\"stateOrProvince\":\"NY\",\"country\":\"US\",\"postalCode\":\"283905\"}}";
        return json;
    }

    //TO CREATE ACCOUNT
	public static Response createAccount(String authToken, String request){		
		Response res = postWithFormParameters(authToken, request, getNPPEnpoint("createAccount_endPoint"));
		return res;
	}
	
	//TO CREATE AN ACCOUNT AND GET THE ID
	public static String createAccountAndGetId(String authToken, String request){		
		Response res = postWithFormParameters(authToken, request, getNPPEnpoint("createAccount_endPoint"));
		return getNodeValue(res, "id").toString();
	}
	
	//TO GENERATE JSON FOR CREATE ACCOUNT
	public static String jsonForCreateAccount(String parentAccID,String status){		
		String name = "Test Account " + new DateTime( DateTimeZone.UTC );
		String contactPerson = "Test Person " + name;
		String email = UUID.randomUUID().toString()+"@test.com";
		String json = "{\"parentAccountId\":\""+parentAccID+"\",\"name\":\""+name+"\",\"status\":\""+status+"\",\"shortname\":\""+name+"\",\"contactPerson\":\""+contactPerson+"\",\"contactEmail\":\""+email+"\",\"contactPhone\":\"9764\",\"address\":{\"address1\":\"Line 1\",\"address2\":\"Line 2\",\"city\":\"NY\",\"stateOrProvince\":\"NY\",\"country\":\"US\",\"postalCode\":\"2\"},\"billingAddress\":{\"address1\":\"Line 1\",\"address2\":\"Line 2\",\"city\":\"NY\",\"stateOrProvince\":\"NY\",\"country\":\"US\",\"postalCode\":\"283905\"}}";
		return json;
	}
	
	//TO SUSPEND AN ACCCOUNT
	public static Response suspendAccount(String authToken, String id, Map request){
		String URI = generateURIWithPathParameter(getNPPEnpoint("suspendAccount_endPoint"),id);
		Response res = postWithQueryParameters(authToken, request, URI);
		return res;
	}
	
	//TO DELETE AN ACCCOUNT
	public static Response deleteAccount(String authToken, String id, Map request){
		String URI = generateURIWithPathParameter(getNPPEnpoint("deleteAccount_endPoint"),id);
		Response res = deleteWithQueryParameters(authToken, request, URI);
		return res;
	}
	
	//TO ACTIVATE AN ACCOUNT
	public static Response activateAccount(String authToken, String id, Map request){
		String URI = generateURIWithPathParameter(getNPPEnpoint("activateAccount_endPoint"),id);
		Response res = postWithQueryParameters(authToken, request, URI);
		return res;
	}
	
    //TO LIST ACCOUNTS
	public static Response listAccounts(String authToken, Map listAccountRequest){
		Response res=getWithQueryparameters(authToken, listAccountRequest, getNPPEnpoint("listAccount_endPoint"));
		return res;
	}
	
    //TO LIST ACCOUNT PRODUCTS
	public static Response ListAccountProducts(String authToken, Map queryParameters, String URI){           
       Response res=getWithQueryparameters(authToken, queryParameters, URI);
       return res;
	}

}
