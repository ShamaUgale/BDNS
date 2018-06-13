package com.NPP.API.Test;

import static com.jayway.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.NPP.API.Global.CommonTestNG;
import com.jayway.restassured.response.Response;

public class test extends CommonTestNG {

	// sample test
	@Test
	public void logintest(){
		String auth=CoreLogin("shama.ugale@neustar.biz", "Password1!");
		Response res=given()
		.accept("application/json")
		.contentType("application/x-www-form-urlencoded; charset=UTF-8")
		.parameters("email","shama.ugale@neustar.biz", "password", "Password1!", "rememberMe",false)
		.post(getNPPEnpoint("Login_endpoint"));
		
		System.out.println(res.asString());
		String accID=getNodeValue(res, "accountId").toString();
		System.out.println("Account id id :    "+accID);
		Map m= new HashMap();
		m.put("accountId", accID);
		Response res1=getWithQueryparameters(auth, m, "rest/product");
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5");
		System.out.println(res1.asString());
		
	}
	
}
