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
import java.util.Objects;

@Service
public class TaskService {
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8070/service/tasks"; // Base URL for the REST API
    
    public TaskService() {
        this.restTemplate = new RestTemplate(); // Initialize a new RestTemplate instance
    }
    
    public Task save(Task newTask , Integer userId) {
        String url = baseUrl + "/store?userId=" + userId;
        return restTemplate.exchange(RequestEntity.post(url).body(newTask), Task.class).getBody();
    }
    public List<Task> findByUserAndDueDateBetween(Integer userId, LocalDate startDate, LocalDate endDate) {
        
        String url = baseUrl + "/findByUserAndDueDateBetween?userId=" + userId +
                "&startDate=" + startDate + "&endDate=" + endDate;
        return restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Task>>() {
                }).getBody();
    }

    public List<Task> findAll() {
        String url = baseUrl + "/all";
        return restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Task>>() {
                }).getBody();
    }
    public List<Task> findByUserOrderByDueDateAsc(Integer userId) {
        String url = baseUrl + "/findByUserOrderByDueDateAsc?userId=" + userId;
        return restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Task>>() {
                }).getBody();
    }
}
