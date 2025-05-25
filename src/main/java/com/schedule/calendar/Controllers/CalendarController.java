package com.schedule.calendar.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.schedule.calendar.Models.Task;
import com.schedule.calendar.Repositories.TaskRepository;

import jakarta.validation.constraints.Null;

@RestController
@RequestMapping("/calendar")
public class CalendarController {
    private final TaskRepository TaskRepository;

    public CalendarController(TaskRepository TaskRepository) {
        this.TaskRepository = TaskRepository;
    }

    @GetMapping("")
    public ModelAndView calendar(Model model) {
        List<Task> tasks = TaskRepository.findAll();
        ModelAndView mav = new ModelAndView("/calendar");

        if (tasks.isEmpty()) {
            mav.addObject("tasks", null);
        } else {
            mav.addObject("tasks", tasks);
        }
        return mav;

    }
    

    
}
