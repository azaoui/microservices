package com.salesforce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import com.force.sdk.oauth.context.ForceSecurityContextHolder;

import java.util.Map;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private LoginService loginService;
        
    @RequestMapping(value="")
    public String LoginToCloudSole(Map<String, Object> map) 
    {
    	ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		session.setAttribute("endpoint", ForceSecurityContextHolder.get().getEndPointHost());
		session.setAttribute("sessionId",  ForceSecurityContextHolder.get().getSessionId());
		session.setAttribute("userName", ForceSecurityContextHolder.get().getUserName());
		session.setAttribute("showSObjects", loginService.showSObjects());
	
		return "contact-listing";
    }
}
