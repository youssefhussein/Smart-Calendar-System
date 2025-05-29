package com.schedule.calendar.Controllers;

import com.schedule.calendar.Models.Task;
import com.schedule.calendar.Models.User;
import com.schedule.calendar.Repositories.TaskRepository;
import com.schedule.calendar.Repositories.UserRepository;
import com.schedule.calendar.Services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks") 
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername()).orElse(null);
    }

    public static class TaskDTO {
        public Long id;
        public String taskName;
        public String taskDescription; 
        public String tdueDate;
        public String color;
        public boolean isCompleted;

        public TaskDTO(Task task) {
            this.id = task.getId() != null ? task.getId().longValue() : null;
            this.taskName = task.getTaskName();
            this.taskDescription = task.getTaskDescription(); // Map description
            this.tdueDate = task.getDueDate().toString();
            this.isCompleted = task.isCompleted();
            this.color = "event-grey";

            if (task.getTaskName() != null) {
                String lowerTaskName = task.getTaskName().toLowerCase();
                if (lowerTaskName.contains("meeting")) this.color = "event-blue";
                else if (lowerTaskName.contains("project")) this.color = "event-purple";
                else if (lowerTaskName.contains("gym")) this.color = "event-green";
                else if (lowerTaskName.contains("client")) this.color = "event-orange";
                else if (lowerTaskName.contains("important")) this.color = "event-red";
                else this.color = "event-grey"; // A default
            } else {
                this.color = "event-grey"; // Default if taskName is null
            }
        }
    }
    
    // DTO for receiving task data from frontend
    public static class TaskCreateRequest {
        public String eventTitle; // Matches JS: title
        public String eventDate;  // Matches JS: date (YYYY-MM-DD)
        public String eventTime;  // Matches JS: time
        public String eventColor; // Matches JS: color (CSS class like "event-blue")
        public String eventDescription; // Matches JS: description
    }


    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasksForMonth(@RequestParam int year, @RequestParam int month) {
        User currentUser = getCurrentAuthenticatedUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        Integer userId = currentUser.getId();
        List<Task> tasks = taskService.findByUserAndDueDateBetween(userId, startDate, endDate);
        List<TaskDTO> taskDTOs = tasks.stream().map(TaskDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(taskDTOs);
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskCreateRequest requestDTO) {
        User currentUser = getCurrentAuthenticatedUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Task newTask = new Task();
        String title = requestDTO.eventTime != null && !requestDTO.eventTime.isEmpty() ?
                       requestDTO.eventTitle + " (" + requestDTO.eventTime + ")" :
                       requestDTO.eventTitle;
        newTask.setTaskName(title);
        newTask.setTaskDescription(requestDTO.eventDescription != null ? requestDTO.eventDescription : "");
        try {
            newTask.setDueDate(LocalDate.parse(requestDTO.eventDate)); // Expects YYYY-MM-DD
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format for eventDate. Expected YYYY-MM-DD.");
        }
        newTask.setCompleted(false);
        newTask.setUser(currentUser);
       
        Task savedTask = taskService.save(newTask , currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new TaskDTO(savedTask));
    }
     @GetMapping("/all")
    public ResponseEntity<List<TaskDTO>> getAllTasksForUser() {
        User currentUser = getCurrentAuthenticatedUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Fetch all tasks for the user, ordered by due date ascending
         Integer userId = currentUser.getId();
        List<Task> tasks = taskService.findByUserOrderByDueDateAsc(userId);
        List<TaskDTO> taskDTOs = tasks.stream().map(TaskDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(taskDTOs);
    }
}