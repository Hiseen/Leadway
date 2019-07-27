package com.leadway.leadway_server.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FrontendController {
	
    // Match everything without a suffix (so not a static resource)
	// all paths that do not contain a period (and are not explicitly mapped already) are Angular routes, 
	//	and should forward to the home page.
	
	// https://stackoverflow.com/questions/43913753/spring-boot-with-redirecting-with-single-page-angular2
    @RequestMapping(value = "/**/{path:[^.]*}")       
    public String redirect() {
        // Forward to home page so that route is preserved.
        return "forward:/leadway-gui/index.html";
    }
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public void index(final HttpServletResponse response) throws IOException {
	    response.setStatus(HttpStatus.OK.value());
	    response.sendRedirect( "/leadway-gui/index.html");
	}
}
