package com.schedule.calendar.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.schedule.calendar.Models.Task;
import com.schedule.calendar.Repositories.TaskRepository;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/calendar")
public class CalendarController {
    private final TaskRepository TaskRepository;

    public CalendarController(TaskRepository TaskRepository) {
        this.TaskRepository = TaskRepository;
    }

    @GetMapping("")
    public ModelAndView calendar() {
        ModelAndView mav = new ModelAndView("calendar.html");

        List<Task> tasks = TaskRepository.findAll();
        if (tasks.isEmpty()) {
            mav.addObject("message", "No tasks found.");
        } else {
            mav.addObject("tasks", tasks);
        }
        return mav;
    }
    @PostMapping("/create")
    public String createTask(@RequestBody @Valid Task task) {
        TaskRepository.save(task);
        return "redirect:/calendar";
    }
    @PostMapping("/${id}/update")
    public String updateTask(@RequestParam Integer taskid , @RequestBody @Valid Task task, Model model) {
        Optional<Task> existingTask = TaskRepository.findById(task.getId());
        if (existingTask.isEmpty()) {
            model.addAttribute("message", "Something went wrong");
            return "redirect:/calendar";
        }
        Task updatedTask = existingTask.get();
        updatedTask.setTaskName(task.getTaskName());
        updatedTask.setTaskDescription(task.getTaskDescription());
        updatedTask.setTdueDate(task.getTdueDate());
        updatedTask.setCompleted(task.getIsCompleted());
        TaskRepository.save(updatedTask);
        return "redirect:/calendar";
    }
    @PostMapping("/delete")
    public String deleteTask(@RequestBody Long id) {
        TaskRepository.deleteById(id);
        return "redirect:/calendar";
    }

}
