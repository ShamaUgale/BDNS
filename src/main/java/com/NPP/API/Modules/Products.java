package com.NPP.API.Modules;

import java.util.HashMap;
import java.util.Map;

import com.NPP.API.Global.CommonMethods;
import com.jayway.restassured.response.Response;

public class Products  extends CommonMethods{
	
	
	//TO LIST ACCOUNT PRODUCTS
    public static Response ListAccountProducts(String authToken, Map queryParameters, String URI){           
    	   Response res=getWithQueryparameters(authToken, queryParameters, getNPPEnpoint("listAccountProducts_endPoint"));
           return res;
    }

    public static Response removeProducts(String authToken, Map queryParameters, String appId, String productCode, String accId, boolean isforceCascade){           
    	Map <String, Object> queryParam=new  HashMap <String, Object> ();
    	queryParam.put("forceCascade", isforceCascade);
    	String product_endPoint="/rest/product/"+appId+"/"+productCode+"/remove/"+accId;
    	Response res=deleteWithQueryParameters(authToken, queryParameters, product_endPoint);
        return res;
      }
    
    public static Response assignProduct( String authToken,String appid,String productCode,String accountId,boolean isPropogable){
        Map queryParameters= new HashMap();
    	   queryParameters.put("propagable", isPropogable);
    	   String endPoint="/rest/product/"+appid+"/"+productCode+"/assign/"+accountId;
    	   Response res= postWithQueryParameters(authToken, queryParameters, endPoint );
    	   return res;
    	  }
         
 }

