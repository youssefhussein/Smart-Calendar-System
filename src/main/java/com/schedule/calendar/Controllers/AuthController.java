package com.schedule.calendar.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @GetMapping("/login")
    public ModelAndView loginIndex() {
        return new ModelAndView("/auth/login.html");
    }
    
    @GetMapping("/signup")
    public ModelAndView signupIndex() {
        return new ModelAndView("/auth/signup.html");
    }
    
    @GetMapping("")
    public ModelAndView fallback() {
        return new ModelAndView("/auth/login.html");
    }
}
