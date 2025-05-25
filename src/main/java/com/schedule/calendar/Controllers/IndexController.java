package com.schedule.calendar.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
    @GetMapping("")
    public String index() {
        return "index";
    }
     @GetMapping("/events/all")
    public String allEventsPage() {
        return "all-events";
    }
    
}