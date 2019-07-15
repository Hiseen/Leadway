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

    private final String[] passPaths={"/error", "/user-auth" , "/login", "/register", "logout"};


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        if(Arrays.stream(passPaths).anyMatch(str->str.equals(request.getRequestURI()))) {
            //if it's one of the passPaths, no need to check token or session.
            return true;
        }
        Cookie cookie = WebUtils.getCookie(request,"token");
        if (cookie == null) {
            response.getWriter().write("false");
        	return false;
        }
	    return true;

	    // check these when in the mapping controller (since we need to redo this to get user info again)
//		String tokenString = cookie.getValue();
//		
//		String decrypted = encryptionService.AESDecrypt(tokenString);
//		String[] result = decrypted.split(":");
//		Long id = Long.parseLong(result[0]);
//		Long token = Long.parseLong(result[1]);
//		Optional<AutoLoginData> data = autoLoginDataRepository.findById(id);
//		if (data.isPresent() && data.get().getToken().equals(token) && data.get().getExpirationTime() > System.currentTimeMillis()) {
//		    //found data and it does not expire.
//		    return true;
//		}
//        
//        response.getWriter().write("false");
//        return false;
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
