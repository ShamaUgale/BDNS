package com.NPP.API.Global;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.testng.Assert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.opera.core.systems.scope.services.Core;
import com.saf.global.LoadProperties;

public class CommonMethods extends LoadProperties{

	public static Response Login_respone=null;
	
	//ADDED BY REEMA
	
	public static Response postWithQueryParameters(String authToken,Map queryParameters,String URI){
    Response res= setAuthToken(authToken).parameters(queryParameters).
                     when().
                     post(URI);           
        return res;
    }

   public static String generateURIWithPathParameter(String URI, Object param){
		String s1= URI;
		String s2=s1.replace("{pathParameter}", param.toString());
		return s2;
	}
	
	public static void setBaseURL(){
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.baseURI  =getNPPApiConfigProperty("BaseURL");
	}	
	
	public static String CoreLogin(String Uname, String password){
		setBaseURL();
		
		return getNodeValue(given()
				.accept("application/json")
				.contentType("application/x-www-form-urlencoded; charset=UTF-8")
				.parameters("email",Uname, "password", password, "rememberMe",false)
				.post(getNPPEnpoint("Login_endpoint")),"token")
				.toString();
		}
	

	public static String  getAuthToken(){
		setBaseURL();
		String authToken= CoreLogin(getNPPApiUser("Login_User"), getNPPApiUser("Login_password"));
		return authToken;
			
		
	}
	
	public static RequestSpecification setAuthToken(String authToken){
		return given().
				headers("Set-Cookie", authToken, "AuthToken", authToken);		
	}
	
	public static void verifyStatusCode(Response res, int expectedStatusCode){		
		int status= res.getStatusCode();
		res.then().assertThat().statusCode(expectedStatusCode);		
	}	
	
		
	public static RequestSpecification setFormParameters(String formParametersJson){
		String APIBody = formParametersJson;
		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.setBody(APIBody);
		builder.setContentType("application/json; charset=UTF-8");
		RequestSpecification requestSpec = builder.build();
		requestSpec.log();
		return requestSpec;	
		
	}
	
	public static Response postWithFormParameters(String authToken,String formParametersJson,String URI){
		Response res= setAuthToken(authToken).
				spec(setFormParameters(formParametersJson)).when().
				post(URI);		
		return res;
	}

	public static Response getWithQueryparameters(String authToken,Map queryParameters,String URI){
		Response res= setAuthToken(authToken).parameters(queryParameters).
				when().
				get(URI);
		return res;
	}
	
	public static Response getWithPathParameters(String authToken,String URI){
		Response res= setAuthToken(authToken).
				when().
				get(URI);
		return res;
	}
		
	public static Response getWithNoParameters(String authToken,String URI){
		Response res= setAuthToken(authToken).
				when().
				get(URI);
		return res;
	}
	
	public static Response putWithFormParameters(String authToken,String formParametersJson,String URI){
		Response res= setAuthToken(authToken).
				spec(setFormParameters(formParametersJson)).when().
				put(URI);		
		return res;
	}
	
	public static Response putWithPathParameters(String authToken,String URI){
		Response res= setAuthToken(authToken)
				.when()
				.put(URI);
		return res;
	}
	
	public static Response deleteWithPathParameters(String authToken,String URI){
		Response res= setAuthToken(authToken).
				when().
				delete(URI);
		return res;
	}
	
	/*
	 * 
	 */
	public static void verifyNodevalue(Response res, String node,Object expectedNodeValue){
		res.then().body(node,equalTo(expectedNodeValue));
		
	}
	
	public static void verifyNodeExists(Response res, String node){
		Assert.assertEquals(res.asString().contains(node), true);		
	}
	
	public static void verifyNodevalues(Response res, String node, Object expectedNodeValue){
		List values=getNodeValues(res, node);		
		Iterator it= values.iterator();
		while(it.hasNext()){
			Object val=it.next();
			Assert.assertEquals(val, expectedNodeValue, "The staus expected was 'Active but it is '"+val+"'");
		}
		
	}
	
	
    public static boolean  VerifyStringContains( String str, String subStr, boolean caseSensitive){
			if (caseSensitive){
				if(str.contains(subStr)){
			}
			return true;
			}
			else{
			return false;			
			}
    }
    
	public static boolean isNodePresent(Response res, String node){
		JsonPath j= new JsonPath(res.asString());
		if(j.getJsonObject(node)==null){
			return false;
		}else{
			return true;
		}
	}
	
	public static void assertNodePresent(Response res,String node){
		Assert.assertEquals(isNodePresent(res, node), true);
	}
	
	public static void assertNodeNotPresent(String node,Response res){
		Assert.assertEquals(isNodePresent(res, node), false);
	}
	
	public static Object getNodeValue(Response res,String node){
		String json= res.asString();
		return JsonPath.with(json).get(node);
	}
	
	public static List getNodeValues(Response res,String node){
		String json= res.asString();
		return JsonPath.with(json).get(node);
	}
	
	public static boolean isInList(List li, Object o){
		return li.contains(o);
	}
	
	public static String randomstring(){
		return "SGTI-" + new DateTime( DateTimeZone.UTC );
	}
	
	public static RequestSpecification setFormParameters(){
		  RequestSpecBuilder builder = new RequestSpecBuilder();
		  builder.setContentType("application/json; charset=UTF-8");
		  RequestSpecification requestSpec = builder.build();
		  requestSpec.log().all();
		  return requestSpec; 
	}
		 
	public static Response postWithPathParameters(String authToken,String URI){
		    Response res= setAuthToken(authToken).spec(setFormParameters()).
		      when().
		      post(URI);
		    return res;
	}
	
	public static Response deleteWithQueryParameters(String authToken,Map queryParameters,String URI){
		  Response res= setAuthToken(authToken).parameters(queryParameters).
		    when().
		    delete(URI);
		  return res;
	}

	
	public static void wait(int timeInSec) throws InterruptedException{
		Thread.sleep(10*1000);
	}
}
