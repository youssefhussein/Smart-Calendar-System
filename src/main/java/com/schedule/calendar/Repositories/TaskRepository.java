package com.schedule.calendar.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schedule.calendar.Models.Task;
import com.schedule.calendar.Models.User;
import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserAndDueDateBetween(User user, LocalDate startDate, LocalDate endDate);  // Changed from tdueDate to dueDate
    List<Task> findByUserOrderByDueDateAsc(User user);
}