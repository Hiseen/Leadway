package com.leadway.leadway_server.services;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.leadway.leadway_server.entities.AutoLoginData;
import com.leadway.leadway_server.repositories.AutoLoginDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.WebUtils;

import java.util.Arrays;
import java.util.Optional;

@Component
public class InterceptionService implements HandlerInterceptor {

    @Autowired
    private AutoLoginDataRepository autoLoginDataRepository;

    @Autowired
    EncryptionService encryptionService;

    private final String[] passPaths={"/error", "/api/user-auth" , "/api/login", "/api/register", "/api/verify"};


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
    	    	
    	String requestPath = request.getRequestURI();
    	System.out.println("Path = " + requestPath);
    	if (requestPath.equals("/")) {
    		return true;
    	}
    	String [] paths = requestPath.split("/");
    	String firstPath = paths[1];
    	System.out.println("First path = " + firstPath);
    	
    	if (!firstPath.equals("api")) {
    		return true;
    	}
    	
        if(Arrays.stream(passPaths).anyMatch(str->str.equals(request.getRequestURI()))) {
            //if it's one of the passPaths, no need to check token or session.
            return true;
        }
        Cookie cookie = WebUtils.getCookie(request,"token");
        if (cookie == null) {
            response.getWriter().write("false");
        	return false;
        }

		String tokenString = cookie.getValue();
		
		String decrypted = encryptionService.AESDecrypt(tokenString);
		String[] result = decrypted.split(":");
		Long id = Long.parseLong(result[0]);
		Long token = Long.parseLong(result[1]);
		Optional<AutoLoginData> data = autoLoginDataRepository.findById(id);
		if (data.isPresent() && data.get().getToken().equals(token) && data.get().getExpirationTime() > System.currentTimeMillis()) {
		    //found data and it does not expire.
		    return true;
		}
        
        response.getWriter().write("false");
        return false;
    }
}


@Component
class InterceptionServiceConfiguration implements WebMvcConfigurer {
    @Autowired
    InterceptionService interceptService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptService);
    }
}
