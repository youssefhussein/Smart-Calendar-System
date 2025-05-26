package com.schedule.calendar.Models;

import jakarta.persistence.*;
import lombok.Data; // Make sure this import is present
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Data // This is crucial
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // For getId()

    @Column(nullable = false)
    private String taskName; // For getTaskName(), setTaskName()

    @Lob
    private String taskDescription; // For getTaskDescription(), setTaskDescription()

    private LocalDate dueDate; // For getDueDate(), setDueDate()

    private boolean completed = false; // For isCompleted(), setCompleted()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // For setUser()
}