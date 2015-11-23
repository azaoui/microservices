package com.salesforce;

import java.util.List;

import com.force.api.ForceApi;

/**
 * @author Ahmed Zaoui (dev.zaouiahmed@gmail.com)
 */

public interface LoginService 
{
	public ForceApi LoginToSalesforce(String accessToken,String endPoint);
	public List<String> showSObjects();
	ForceApi LoginToSalesforce();
}