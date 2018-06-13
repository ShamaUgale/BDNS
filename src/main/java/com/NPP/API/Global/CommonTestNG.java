package com.NPP.API.Global;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

public class CommonTestNG extends CommonMethods{
	
	public static String globalAuthToken= null;
	
	@BeforeTest
	public static void setUp(){
		if(globalAuthToken==null){
			globalAuthToken=CommonMethods.CoreLogin(getNPPApiUser("Login_User"), getNPPApiUser("Login_password"));		
		}
	}
	
	public static void removeAuthToken(){
		globalAuthToken="";
	}
	
	public static void resetAuthToken(){
		globalAuthToken=null;
	}

	public static void setInvalidAuthToken(){
		globalAuthToken="ABCXYZ123";
	}
}
