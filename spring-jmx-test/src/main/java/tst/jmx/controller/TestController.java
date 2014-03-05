package tst.jmx.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test/*")
public class TestController {
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TestController.class);
	
    @RequestMapping
    @ResponseBody
    public String testing() {
    	
    	String trace = "response from "+ this +" at "+ new Date();

    	if (logger.isDebugEnabled()) {
			logger.debug("test: "+ trace);
		}
		
    	return trace;
    }

}
