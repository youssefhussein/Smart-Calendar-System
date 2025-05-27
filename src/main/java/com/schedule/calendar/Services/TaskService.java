package com.schedule.calendar.Services;

import com.schedule.calendar.Models.Task;
import com.schedule.calendar.Models.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8070/service/tasks"; // Base URL for the REST API
    
    public TaskService() {
        this.restTemplate = new RestTemplate(); // Initialize a new RestTemplate instance
    }
    
    public Task save(Task newTask) {
        String url = baseUrl + "/store"; // The endpoint URL for saving a task
        return restTemplate.exchange(RequestEntity.post(url).body(newTask), Task.class).getBody();
    }
    public List<Task> findByUserAndDueDateBetween(User currentUser, LocalDate startDate, LocalDate endDate) {
        String url = baseUrl + "/findByUserAndDueDateBetween?userId=" + currentUser.getId() +
                "&startDate=" + startDate + "&endDate=" + endDate;
        return restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Task>>() {
                }).getBody();
    }
    public List<Task> findByUserOrderByDueDateAsc(User currentUser) {
        String url = baseUrl + "/findByUserOrderByDueDateAsc";
        Map<String, User> requestBody = new HashMap<>();
        requestBody.put("user", currentUser);
        RequestEntity<Map<String, User>> requestEntity = RequestEntity.post(url).body(requestBody);
        return restTemplate.exchange(requestEntity, new ParameterizedTypeReference<List<Task>>() {}).getBody();
    }
}
