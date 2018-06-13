package com.NPP.API.Modules;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.NPP.API.Global.CommonMethods;
import com.jayway.restassured.response.Response;


public class Role extends CommonMethods{

	public static Response createCustomRole(String authToken,String data){
		Response res=postWithFormParameters(authToken, data,getNPPEnpoint("createCustomRole_endPoint"));
		return res;
	}
	
	
	//	public static String createCustomRole_Json(String accountId){
	//       
	//		String name="CustomeRoleThroughAPI:-" + new DateTime( DateTimeZone.UTC ) ;
	//        String json = "{\"accountId\": \""+accountId+"\",\"baseRoleId\": \""+getNPPApiDataProperty("validbaseRoleId")+"\",\"name\": \""+name+"\",\"propagable\": \""+getNPPApiDataProperty("propagable")+"\",\"addPermissions\": [{\"appId\": \""+getNPPApiDataProperty("appId1")+"\",\"permission\": \""+getNPPApiDataProperty("appPermission1")+"\"},{\"appId\": \""+getNPPApiDataProperty("appId2")+"\",\"permission\":\""+getNPPApiDataProperty("appPermission2")+"\"}]}";
	//		return json;
	//	}
	//	
	public static String createCustomRole_JsonWithMandatoryDetails(String accountId,String baseRoleId,boolean propagable){
		  		
       String name="CustomeRoleThroughAPI:-" + new DateTime( DateTimeZone.UTC ) ;
       String json = "{\"accountId\": \""+accountId+"\",\"baseRoleId\": \""+baseRoleId+"\",\"name\": \""+name+"\",\"propagable\": \""+propagable+"\"}";
	   return json;
	   }
	 
	 public static String createCustomRoleJson_AdminMinusTheme(String accountId, String baseRoleId){
		 String name="CustomeRoleThroughAPI:-" + new DateTime( DateTimeZone.UTC ) ;
		 String json = "{\"accountId\": \""+accountId+"\",\"baseRoleId\": \""+baseRoleId+"\",\"name\": \""+name+"\",\"propagable\": \"true\",\"deletePermissions\": [{\"appId\": \"npp\",\"permission\": \"theme_manage\"}]}";
		 return json;
	 }
}
