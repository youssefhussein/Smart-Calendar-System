package com.schedule.calendar.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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