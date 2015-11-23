package com.salesforce;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.force.api.ApiConfig;
import com.force.api.ApiSession;
import com.force.api.ApiVersion;
import com.force.api.DescribeSObjectBasic;
import com.force.api.ForceApi;
import com.force.sdk.oauth.context.ForceSecurityContextHolder;
import com.force.sdk.oauth.context.SecurityContext;

/**
 * @author Ahmed Zaoui (dev.zaouiahmed@gmail.com)
 */

@Service
public class LoginServiceImp implements LoginService {
	
	
	@Autowired
	ApplicationConfiguration applicationConfiguration;
	private static List<String> sObjectNames = new ArrayList<String>();
	
	
	
	@Override
	public ForceApi LoginToSalesforce() {
		SecurityContext sc = ForceSecurityContextHolder.get();
        ApiSession apiSession = new ApiSession();
        apiSession.setAccessToken(sc.getSessionId());
        apiSession.setApiEndpoint(sc.getEndPointHost());

        return new ForceApi(apiSession);

	}

	@Override
	public List<String> showSObjects() {
		if (sObjectNames.isEmpty())
		{
			for (DescribeSObjectBasic describeObject : LoginToSalesforce().describeGlobal().getSObjects())
			{
				if (describeObject.isQueryable())
					sObjectNames.add(describeObject.getName());
			}
			return sObjectNames;
		}
		else
		{
			return sObjectNames;
		}
	
	}

	@Override
	public ForceApi LoginToSalesforce(String accessToken, String endPoint) {
		// TODO Auto-generated method stub
		return null;
	}

}
