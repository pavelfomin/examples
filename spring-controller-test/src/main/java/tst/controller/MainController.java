package tst.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/*")
public class MainController {
	
    @RequestMapping
    public String index(HttpServletRequest request, Model model) {
    	model.addAttribute("date", System.currentTimeMillis());
    	return "index";
    }

    @RequestMapping("test")
    public String test(Model model) {
    	
    	model.addAttribute("date", System.currentTimeMillis());

    	return "test";
    }

    @RequestMapping("test1")
    @ResponseBody
    public String test1(Model model) {
    	
    	String trace = "response from "+ this +" at "+ new Date();
    	
    	return trace;
    }
    
}
