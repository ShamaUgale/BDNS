package com.NPP.API.Modules;

import java.util.HashMap;
import java.util.Map;

import com.NPP.API.Global.CommonMethods;
import com.jayway.restassured.response.Response;

public class Product extends CommonMethods {


   public static Response assignProduct( String authToken,String appid,String productCode,String accountId,boolean isPropogable){

		 Map queryParameters= new HashMap();
		 queryParameters.put("propagable", isPropogable);
		 String endPoint="/rest/product/"+appid+"/"+productCode+"/assign/"+accountId;
		 Response res= postWithQueryParameters(authToken, queryParameters,endPoint );
		 return res;
		}
}
