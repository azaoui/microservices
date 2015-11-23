package com.salesforce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.force.sdk.oauth.context.ForceSecurityContextHolder;
import com.force.sdk.oauth.context.SecurityContext;



/**
 * @author Ahmed Zaoui
 *
 */
public class OAuthServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@Autowired
	ApplicationConfiguration applicationConfiguration;
	@Autowired
    private LoginService loginService;
	
	private String clientId;
	private String clientSecret;
	private String redirectUri;
	private String authUrl;
	private String environment;
	private String tokenUrl;

	public void init(ServletConfig config) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				config.getServletContext());
		clientId = applicationConfiguration.getClientId();
		clientSecret = applicationConfiguration.getClientSecret();
		redirectUri = applicationConfiguration.getRedirectUri();
		environment = OAuthConstants.SF_PROD;
		

			try {
				authUrl = OAuthConstants.SF_PROD + ResourcePath.AUTHORIZE.getPath()
						+ "?response_type=code&client_id=" + clientId
						+ "&redirect_uri="
						+ URLEncoder.encode(redirectUri, "UTF-8");
				tokenUrl = environment + ResourcePath.TOKEN.getPath();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		super.init(config);

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter writer = response.getWriter();
		String accessToken = (String) request.getSession().getAttribute(
				OAuthConstants.ACCESS_TOKEN);
		String instanceUrl = (String) request.getSession().getAttribute(
				OAuthConstants.INSTANCE_URL);

		if (accessToken == null || instanceUrl == null) {
			if (request.getRequestURI().endsWith("oauth")) {
				// we need to send the user to authorize
				response.sendRedirect(authUrl);
				return;
			}
		
		
		 
			   
				String code = request.getParameter("code");

				HttpClient httpclient = new HttpClient();

				PostMethod post = new PostMethod(tokenUrl);
				
				post.addParameter(OAuthConstants.CODE_KEY, code);
				post.addParameter(OAuthConstants.GRANT_TYPE_KEY,
						OAuthConstants.AUTHORIZATION_CODE);
				post.addParameter(OAuthConstants.CLIENT_ID_KEY, clientId);
				post.addParameter(OAuthConstants.CLIENT_SECRET_KEY,
						clientSecret);
				post.addParameter(OAuthConstants.REDIRECT_URI_KEY,
						redirectUri);
				try {
					httpclient.executeMethod(post);

					try {

						InputStream rstream = post.getResponseBodyAsStream();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(rstream, "UTF-8"));
						String json = reader.readLine();
						JSONTokener tokener = new JSONTokener(json);
						JSONObject authResponse = new JSONObject(tokener);

						accessToken = authResponse
								.getString(OAuthConstants.ACCESS_TOKEN);

						instanceUrl = authResponse
								.getString(OAuthConstants.INSTANCE_URL);
						
					} catch (JSONException e) {
						e.printStackTrace();
						throw new ServletException(e);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (HttpException e) {
					e.printStackTrace();
					throw new ServletException(e);
				} finally {
					post.releaseConnection();
				}
			
		request.getSession().setAttribute(OAuthConstants.ACCESS_TOKEN, accessToken);
		request.getSession().setAttribute(
				OAuthConstants.INSTANCE_URL, instanceUrl);
		//SecurityContext sc = ForceSecurityContextHolder.get(true);
		loginService.LoginToSalesforce(accessToken,instanceUrl);
		response.sendRedirect("/");
		
		return;
		
		 }
	}
	

		

	
}
