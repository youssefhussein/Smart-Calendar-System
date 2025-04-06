package com.schedule.calendar.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.schedule.calendar.Models.Task;
import com.schedule.calendar.Repositories.TaskRepository;

@RestController
@RequestMapping("/calendar")
public class CalendarController {
    private final TaskRepository TaskRepository;

    public CalendarController(TaskRepository TaskRepository) {
        this.TaskRepository = TaskRepository;
    }

    @GetMapping("")
    public String calendar(Model model) {
        List<Task> tasks = TaskRepository.findAll();
        if (tasks.isEmpty()) {
            model.addAttribute("message", "No tasks found.");
        } else {
            model.addAttribute("tasks", tasks);
        }
        return "calendar";

    }

    public String createTask(@RequestBody Task task) {
        TaskRepository.save(task);
        return "redirect:/calendar";
    }

    public String updateTask(@RequestBody Task task, Model model) {
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

    public String deleteTask(@RequestBody Long id) {
        TaskRepository.deleteById(id);
        return "redirect:/calendar";
    }

}
