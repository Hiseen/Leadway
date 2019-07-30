package com.leadway.leadway_server.services;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class CookieService {
	public void setTokenCookie(HttpServletResponse httpResponse, String token, boolean rememberMe, int expiry) {
		Cookie cookie = new Cookie("token", token);
		// if remember me is not selected when login, cookie will expire, else the cookie is permanent
		if (!rememberMe) {
			cookie.setMaxAge(expiry);			
		}
		//cookie.setSecure(true);  // requires HTTPS?
		cookie.setPath("/"); // work for whole domain
		httpResponse.addCookie(cookie);
	}
	
	public void removeTokenCookie(HttpServletResponse httpResponse, String token) {
		Cookie cookie = new Cookie("token", token);
		cookie.setMaxAge(0);		
		cookie.setPath("/"); // work for whole domain
		httpResponse.addCookie(cookie);
	}
}
