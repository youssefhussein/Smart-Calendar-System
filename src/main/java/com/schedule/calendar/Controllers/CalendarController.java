package com.schedule.calendar.Controllers;

import com.schedule.calendar.Models.Task;
import com.schedule.calendar.Models.User;
import com.schedule.calendar.Repositories.UserRepository;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import com.schedule.calendar.Services.TaskService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/calendar")
public class CalendarController {
    private final TaskService taskService;
    private final UserRepository userRepository;
    
    public CalendarController(TaskService TaskRepository , UserRepository userRepository) {
        this.taskService = TaskRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public ModelAndView calendar(Model model) {
        List<Task> tasks = taskService.findAll();
        ModelAndView mav = new ModelAndView("/calendar");

        if (tasks.isEmpty()) {
            mav.addObject("tasks", null);
        } else {
            mav.addObject("tasks", tasks);
        }
        model.addAttribute("viewType", "main");
        
        return mav;

    }
    
    @GetMapping("/view/monthly")
    public String monthlyView(Model model, Principal principal) {
        // Fetch tasks for the current user, potentially filtered for monthly view
        User currentUser = userRepository.findByUsername(principal.getName()).orElse(null);
        List<Task> tasks = Collections.emptyList();
        if (currentUser != null) {
            tasks =currentUser.getTasks(); // Or more specific query for monthly
        }
        model.addAttribute("tasks", tasks.isEmpty() ? null : tasks);
        model.addAttribute("viewType", "Monthly");
        // You might use the same "calendar.html" template and just pass different data
        // or have "calendar_monthly.html"
        return "calendar"; // Or "calendar_monthly"
    }

    @GetMapping("/view/weekly")
    public String weeklyView(Model model, Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName()).orElse(null);
        List<Task> tasks = Collections.emptyList();
        if (currentUser != null) {
            tasks =currentUser.getTasks(); // Or more specific query for weekly
        }
        model.addAttribute("tasks", tasks.isEmpty() ? null : tasks);
        model.addAttribute("viewType", "Weekly");
        return "calendar"; // Or "calendar_weekly"
    }

    @GetMapping("/view/daily")
    public String dailyView(Model model, Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName()).orElse(null);
        List<Task> tasks = Collections.emptyList();
        if (currentUser != null) {
            tasks = currentUser.getTasks(); // Or more specific query for daily
        }
        model.addAttribute("tasks", tasks.isEmpty() ? null : tasks);
        model.addAttribute("viewType", "Daily");
        return "calendar"; // Or "calendar_daily"
    }
}