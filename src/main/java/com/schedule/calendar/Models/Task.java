package com.schedule.calendar.Models;

import java.time.LocalDate;
import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@SuppressWarnings("unused")
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taskName;
    private String taskDescription;
    private LocalDate TdueDate;
    private boolean isCompleted;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId") 
    private User user;

    public Task() {
    }

    public Task(Long id, String taskName, String taskDescription, LocalDate TdueDate, boolean isCompleted,  User user) {
        this.id = id;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.TdueDate = TdueDate;
        this.isCompleted = isCompleted;
        this.user= user;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return this.taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public LocalDate getTdueDate() {
        return this.TdueDate;
    }

    public void setTdueDate(LocalDate TdueDate) {
        this.TdueDate = TdueDate;
    }

    public boolean isIsCompleted() {
        return this.isCompleted;
    }

    public boolean getIsCompleted() {
        return this.isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Task id(Long id) {
        setId(id);
        return this;
    }

    public Task taskName(String taskName) {
        setTaskName(taskName);
        return this;
    }

    public Task taskDescription(String taskDescription) {
        setTaskDescription(taskDescription);
        return this;
    }

    public Task TdueDate(LocalDate TdueDate) {
        setTdueDate(TdueDate);
        return this;
    }

    public Task isCompleted(boolean isCompleted) {
        setIsCompleted(isCompleted);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Task)) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(taskName, task.taskName)
                && Objects.equals(taskDescription, task.taskDescription) && Objects.equals(TdueDate, task.TdueDate)
                && isCompleted == task.isCompleted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskName, taskDescription, TdueDate, isCompleted);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", taskName='" + getTaskName() + "'" +
                ", taskDescription='" + getTaskDescription() + "'" +
                ", TdueDate='" + getTdueDate() + "'" +
                ", isCompleted='" + isIsCompleted() + "'" +
                "}";
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

}
