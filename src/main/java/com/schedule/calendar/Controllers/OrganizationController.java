package com.schedule.calendar.Controllers;

import com.schedule.calendar.Models.Task;
import com.schedule.calendar.Models.User;
import com.schedule.calendar.Models.UserType;
import com.schedule.calendar.Repositories.TaskRepository;
import com.schedule.calendar.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/organization")
public class OrganizationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    // Create Task
    @PostMapping("/tasks")
    public String createTask(@ModelAttribute Task task, @RequestParam List<Integer> studentIds) {
        User currentUser = getCurrentUser();
        if (currentUser == null || !currentUser.getIsOrganizationAdmin()) {
            return "redirect:/calendar";
        }

        for (Integer studentId : studentIds) {
            User student = userRepository.findById(studentId).orElse(null);
            if (student != null && student.getOrganization().equals(currentUser.getOrganization())) {
                Task newTask = new Task();
                newTask.setTaskName(task.getTaskName());
                newTask.setTaskDescription(task.getTaskDescription());
                newTask.setDueDate(task.getDueDate());
                newTask.setUser(student);
                newTask.setCompleted(false);
                taskRepository.save(newTask);
            }
        }
        return "redirect:/organization/tasks";
    }

    // Read Tasks
    @GetMapping("/tasks")
    public ModelAndView listTasks() {
        ModelAndView mav = new ModelAndView("organization/tasks");
        User currentUser = getCurrentUser();
        
        if (currentUser != null && currentUser.getOrganization() != null) {
            List<User> students = userRepository.findByOrganizationAndUserType(
                currentUser.getOrganization(), 
                UserType.STUDENT
            );
            List<Task> tasks = taskRepository.findByUsersInOrganization(currentUser.getOrganization());
            mav.addObject("students", students);
            mav.addObject("tasks", tasks);
        }
        
        return mav;
    }

    // Update Task
    @PostMapping("/tasks/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute Task task) {
        User currentUser = getCurrentUser();
        if (currentUser == null || !currentUser.getIsOrganizationAdmin()) {
            return "redirect:/calendar";
        }

        Task existingTask = taskRepository.findById(id).orElse(null);
        if (existingTask != null && existingTask.getUser().getOrganization().equals(currentUser.getOrganization())) {
            existingTask.setTaskName(task.getTaskName());
            existingTask.setTaskDescription(task.getTaskDescription());
            existingTask.setDueDate(task.getDueDate());
            taskRepository.save(existingTask);
        }
        
        return "redirect:/organization/tasks";
    }

    // Delete Task
    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        if (currentUser == null || !currentUser.getIsOrganizationAdmin()) {
            return "redirect:/calendar";
        }

        Task task = taskRepository.findById(id).orElse(null);
        if (task != null && task.getUser().getOrganization().equals(currentUser.getOrganization())) {
            taskRepository.delete(task);
        }
        
        return "redirect:/organization/tasks";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName()).orElse(null);
    }
}