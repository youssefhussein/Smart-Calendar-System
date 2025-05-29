package com.schedule.calendar.Repositories;

import com.schedule.calendar.Models.Organization;
import com.schedule.calendar.Models.Task;
import com.schedule.calendar.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserAndDueDateBetween(User user, LocalDate startDate, LocalDate endDate);  // Changed from tdueDate to dueDate
    List<Task> findByUserOrderByDueDateAsc(User user);

    @Query("SELECT t FROM Task t WHERE t.user.organization = :organization")
    List<Task> findByUsersInOrganization(Organization organization);
}