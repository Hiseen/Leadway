package com.leadway.leadway_server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {
	
    // Match everything without a suffix (so not a static resource)
	// all paths that do not contain a period (and are not explicitly mapped already) are Angular routes, 
	//	and should forward to the home page.
	// https://stackoverflow.com/questions/43913753/spring-boot-with-redirecting-with-single-page-angular2
//    @RequestMapping(value = "/**/{path:[^.]*}")       
//    public String redirect() {
//        // Forward to home page so that route is preserved.
//        return "forward:/leadway-gui/index.html";
//    }
}
