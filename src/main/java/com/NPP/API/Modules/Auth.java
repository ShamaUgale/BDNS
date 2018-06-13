package com.NPP.API.Modules;


import java.util.HashMap;
import java.util.Map;

import com.NPP.API.Global.CommonMethods;
import com.jayway.restassured.response.Response;
import static com.jayway.restassured.RestAssured.given;
public class Auth extends CommonMethods{

	
	public static String switchAccountId(String authToken,String id){
	  String URI=generateURIWithPathParameter(getNPPEnpoint("switchAccount_endPoint"),id);
	  String request = "{\"accountId\": \""+id+"\"}";
	  Response res=putWithFormParameters(authToken, request, URI);
	  return getNodeValue(res,"accountId").toString();
	}
	
	public static Response switchAccount(String authToken,String id){
		 String URI=generateURIWithPathParameter(getNPPEnpoint("switchAccount_endPoint"),id);
		  String request = "{\"accountId\": \""+id+"\"}";
		  Response res=putWithFormParameters(authToken, request, URI);
		  return res;
	}

	public static Response login(String email, String password, boolean rememberMe){ 
	Response res= given()
                   .accept("application/json")
                   .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                   .parameters("email",""+email+"", "password", password, "rememberMe", rememberMe)
                   .post(getNPPEnpoint("Login_endpoint"));
                    return res; 
    }


public static Response updatePassword(String authToken, String currentPassword, String newPassword){
	Map<String, String> queryParameters= new HashMap<String, String>();
	queryParameters.put("currentPassword", currentPassword);
	queryParameters.put("password", newPassword);

	Response res=postWithQueryParameters(authToken, queryParameters, getNPPEnpoint("update_Password"));
	return res;
}



	
	    






}

