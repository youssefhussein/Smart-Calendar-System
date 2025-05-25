package com.schedule.calendar.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class IndexController {
    @GetMapping("")
    public ModelAndView index() {
        return new ModelAndView("index.html");
    }
     @GetMapping("/events/all")
    public ModelAndView allEventsPage() {
        return new ModelAndView("all-events.html");
    }
    
}