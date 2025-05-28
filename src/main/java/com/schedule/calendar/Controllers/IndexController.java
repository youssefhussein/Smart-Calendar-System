package com.schedule.calendar.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
   
     @GetMapping("/events/all")
    public String allEventsPage() {
        return "all-events";
    }
    
}