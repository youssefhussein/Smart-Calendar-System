package com.schedule.calendar.Controllers;

import com.schedule.calendar.Models.Task;
import com.schedule.calendar.Models.User;
import com.schedule.calendar.Repositories.TaskRepository;
import com.schedule.calendar.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller // Changed from @RestController
@RequestMapping("/calendar")
public class CalendarController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository; // Added UserRepository

    @Autowired // Good practice to use constructor injection
    public CalendarController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public String calendarPage(Model model, Principal principal) { // Using Principal for current user
        if (principal == null) {
            // This should be handled by Spring Security's .authenticated() rule
            // but as a fallback:
            return "redirect:/auth/login";
        }

        User currentUser = userRepository.findByUsername(principal.getName()).orElse(null);
        List<Task> tasks = Collections.emptyList();

        if (currentUser != null) {
            // Assuming Task has a 'user' field mapped to the User entity
            // And TaskRepository has a method like: List<Task> findByUser(User user);
            // OR if User entity has @OneToMany List<Task> tasks;
            tasks = taskRepository.findByUser(currentUser); // You'll need to add this method to TaskRepository
            // If using user.getTasks() directly, ensure tasks are eagerly fetched or session is open.
            // tasks = currentUser.getTasks();
        }

        model.addAttribute("tasks", tasks.isEmpty() ? null : tasks);
        model.addAttribute("viewType", "main"); // Example attribute
        return "calendar"; // Renders src/main/resources/templates/calendar.html
    }

    @GetMapping("/view/monthly")
    public String monthlyView(Model model, Principal principal) {
        // Fetch tasks for the current user, potentially filtered for monthly view
        User currentUser = userRepository.findByUsername(principal.getName()).orElse(null);
        List<Task> tasks = Collections.emptyList();
        if (currentUser != null) {
            tasks = taskRepository.findByUser(currentUser); // Or more specific query for monthly
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
            tasks = taskRepository.findByUser(currentUser); // Or more specific query for weekly
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
            tasks = taskRepository.findByUser(currentUser); // Or more specific query for daily
        }
        model.addAttribute("tasks", tasks.isEmpty() ? null : tasks);
        model.addAttribute("viewType", "Daily");
        return "calendar"; // Or "calendar_daily"
    }
}