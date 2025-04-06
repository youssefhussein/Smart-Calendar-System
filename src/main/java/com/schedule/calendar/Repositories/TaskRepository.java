package com.schedule.calendar.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schedule.calendar.Models.Task;

public interface TaskRepository extends JpaRepository<Task,Long> {

}
