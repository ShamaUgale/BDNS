package com.NPP.API.Modules;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;


import com.NPP.API.Global.CommonMethods;
import com.jayway.restassured.response.Response;


public class User extends CommonMethods{
	
	//TO CREATE USER
	public static Response CreateUser(String authToken,String data)
	{
		Response res=postWithFormParameters(authToken,data,getNPPEnpoint("createUser_endPoint"));
		return res;
    }
	
	//TO CREATE USER AND GET USER ID
	public static String CreateUserAndgetId(String authToken, String data)
	{
		Response res=postWithFormParameters(authToken,data,getNPPEnpoint("createUser_endPoint"));
		String UserId=getNodeValue(res, "id").toString();
		return UserId;
	}

	//TO CREATE USER WITH ALL INPUTS
	public static String CreateUserWithAllInputs_json(String title, String phn,String mob,String fax, String accId,String roleId)
	{
		String firstName = "Test User First Name" + new DateTime( DateTimeZone.UTC );
	    String lastName = "Test User Last Name" + new DateTime( DateTimeZone.UTC );
		String email = UUID.randomUUID().toString()+"@test.com";
		String json="{\"email\":\""+email+"\",\"firstName\":\""+firstName+"\",\"lastName\":\""+lastName+"\",\"title\":\""+title+"\",\"phone\":\""+phn+"\",\"mobile\":\""+mob+"\",\"fax\":\""+fax+"\",\"accountId\":\""+accId+"\",\"roleId\":\""+roleId+"\",\"address\": {\"address1\": \"street1\",\"address2\": \"Near Main Tower\",\"city\": \"NY\",\"stateOrProvince\": \"NY\",\"country\": \"US\",\"postalCode\": \"245657\"}}";
		                                                                                                                                                                                                                         
		return json ;
	}
	

     //TO CREATE USER WITH MINIMUM INPUT
	public static String CreateUserWithMinInputs_json(String accId, String roleId)
	{
		      String firstName = "Test User First Name" + new DateTime( DateTimeZone.UTC );
			  String lastName = "Test User Last Name" + new DateTime( DateTimeZone.UTC );
			  String email = UUID.randomUUID().toString()+"@test.com";
			  String json = "{\"email\":\""+email+"\",\"firstName\":\""+firstName+"\",\"lastName\":\""+lastName+"\",\"accountId\":\""+accId+"\",\"roleId\":\""+roleId+"\",\"address\":{\"address1\":\"Line 1\",\"city\":\"NY\",\"stateOrProvince\":\"NY\",\"country\":\"US\",\"postalCode\":\"478653\"}}";
			  return json;
	}

	 public static String createUserJson(String accId, String roleId){
		  String firstName = "Test User First Name" + new DateTime( DateTimeZone.UTC );
		  String lastName = "Test User Last Name" + new DateTime( DateTimeZone.UTC );
		  String email = UUID.randomUUID().toString()+"@test.com";
		  String json = "{\"email\":\""+email+"\",\"firstName\":\""+firstName+"\",\"lastName\":\""+lastName+"\",\"accountId\":\""+accId+"\",\"roleId\":\""+roleId+"\",\"address\":{\"address1\":\"Line 1\",\"city\":\"NY\",\"stateOrProvince\":\"NY\",\"country\":\"US\",\"postalCode\":\"478653\"}}";
		  return json;
		 }
	 
	 public static String createUserJson(String email,String accId, String roleId){
		  String firstName = "Test User First Name" + new DateTime( DateTimeZone.UTC );
		  String lastName = "Test User Last Name" + new DateTime( DateTimeZone.UTC );
		  String json = "{\"email\":\""+email+"\",\"firstName\":\""+firstName+"\",\"lastName\":\""+lastName+"\",\"accountId\":\""+accId+"\",\"roleId\":\""+roleId+"\",\"address\":{\"address1\":\"Line 1\",\"city\":\"NY\",\"stateOrProvince\":\"NY\",\"country\":\"US\",\"postalCode\":\"478653\"}}";
		  return json;
		 }

   public static Response listUsers(String authToken, Map queryParameter){
    	
    	 Response res=getWithQueryparameters(authToken, queryParameter,getNPPEnpoint("listUsers_endPoint"));
    	 return res;
    }

   //GET MY ACCOUNT ID
    public static String getMyAccountId(String authToken,String userEmailId){
		 Map<String,Object> param=new HashMap<String,Object>();
		 param.put("search",userEmailId );
		 Response res=listUsers(authToken,param);
		 String accId=getNodeValue(res,"results.accountId[0]").toString();
		 return accId;
	  } 	    
    
    public static Response suspendUser(String authToken,String id){
    	String URI= generateURIWithPathParameter(getNPPEnpoint("suspend_User"), id);
    	Response res= postWithPathParameters(authToken, URI);
    	return res;
    }
    
    public static Response deleteUser(String authToken,String id){
    	String URI= generateURIWithPathParameter(getNPPEnpoint("delete_User"), id);
    	Response res= deleteWithPathParameters(authToken, URI);
    	return res;
    }
    
   }

