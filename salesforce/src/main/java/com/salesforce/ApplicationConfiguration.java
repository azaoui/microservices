package com.salesforce;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ahmed Zaoui
 *
 */
@ConfigurationProperties(prefix = "salesforce", locations = "classpath:application.properties")
public class ApplicationConfiguration {

	private String redirectUri;

	private String clientSecret;

	private String clientId;

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

}