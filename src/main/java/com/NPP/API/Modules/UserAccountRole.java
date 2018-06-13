package com.NPP.API.Modules;

import java.util.Map;

import com.NPP.API.Global.CommonMethods;
import com.jayway.restassured.response.Response;

public class UserAccountRole extends CommonMethods {

	public static Response addToAccount(String authToken,Map user_addqueryParameter){
		Response res=postWithQueryParameters(authToken, user_addqueryParameter,getNPPEnpoint("addToAccount_endPoint"));
		return res;
	}
	
	public static Response removeFromAccount(String authToken,Map user_removequeyParameter){
		Response res=deleteWithQueryParameters(authToken, user_removequeyParameter,getNPPEnpoint("removeFromAccount_endPoint"));
		return res;
	}
	
	
	
	
	
	
	
	
}
