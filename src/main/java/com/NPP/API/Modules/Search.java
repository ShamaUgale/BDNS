package com.NPP.API.Modules;
import java.util.HashMap;
import java.util.Map;

import com.NPP.API.Global.CommonMethods;
import com.jayway.restassured.response.Response;

public class Search extends CommonMethods {	
	public static Response globalSearch(String authToken, Map searchqueryParameter){
	  Response res=getWithQueryparameters(authToken, searchqueryParameter, getNPPEnpoint("globalSearch_endpoint"));
	  return res;  	  
	 }
}
