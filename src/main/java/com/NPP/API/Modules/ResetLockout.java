package com.NPP.API.Modules;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.NPP.API.Global.CommonMethods;
import com.jayway.restassured.response.Response;

public class ResetLockout extends CommonMethods { 
	
    public static Response resetLockout (String authToken, String userId)
    {
    	String URI=generateURIWithPathParameter(getNPPEnpoint("resetLockout_endPoint"), userId);
    	Response res=postWithPathParameters(authToken, URI);
        return res;
    }


}
