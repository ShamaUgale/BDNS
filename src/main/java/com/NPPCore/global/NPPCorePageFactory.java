package com.NPPCore.global;

//import com.NPP.PlatformOne.PlatformOneHomePageVerification;
//import com.NPP.PlatformOne.PlatformOneAudience;
//import com.NPP.PlatformOne.PlatformOneLoginPageVerification;
import com.NPPCore.NPPCoreAccount;
import com.NPPCore.NPPCoreLogin;
import com.NPPCore.NPPCore_Verification;
import com.NPPCore.global.GlobalCommonMethods;

public class NPPCorePageFactory extends NPPCore_URL {

	public NPPCoreLogin PlatformOneLogin() {
		return new NPPCoreLogin();
	}

	public NPPCore_Verification PlatformOne_Verification() {
		return new NPPCore_Verification();
	}

	public GlobalCommonMethods GlobalCommonMethods() {
		return new GlobalCommonMethods();
	}
}
