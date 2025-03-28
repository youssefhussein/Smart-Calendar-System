package com.schedule.calendar.Models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "assignments")

public class Assignment {
    
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String assignmentName;
    private String assignmentDescription;
    private LocalDate AdueDate;
    private boolean isCompleted;
    

    public Assignment() {
    }

    public Assignment(Long id, String assignmentName, String assignmentDescription, LocalDate AdueDate, boolean isCompleted) {
        this.id = id;
        this.assignmentName = assignmentName;
        this.assignmentDescription = assignmentDescription;
        this.AdueDate = AdueDate;
        this.isCompleted = isCompleted;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssignmentName() {
        return this.assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getAssignmentDescription() {
        return this.assignmentDescription;
    }

    public void setAssignmentDescription(String assignmentDescription) {
        this.assignmentDescription = assignmentDescription;
    }

    public LocalDate getAdueDate() {
        return this.AdueDate;
    }

    public void setAdueDate(LocalDate AdueDate) {
        this.AdueDate = AdueDate;
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

    public Assignment id(Long id) {
        setId(id);
        return this;
    }

    public Assignment assignmentName(String assignmentName) {
        setAssignmentName(assignmentName);
        return this;
    }

    public Assignment assignmentDescription(String assignmentDescription) {
        setAssignmentDescription(assignmentDescription);
        return this;
    }

    public Assignment AdueDate(LocalDate AdueDate) {
        setAdueDate(AdueDate);
        return this;
    }

    public Assignment isCompleted(boolean isCompleted) {
        setIsCompleted(isCompleted);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Assignment)) {
            return false;
        }
        Assignment assignment = (Assignment) o;
        return Objects.equals(id, assignment.id) && Objects.equals(assignmentName, assignment.assignmentName) && Objects.equals(assignmentDescription, assignment.assignmentDescription) && Objects.equals(AdueDate, assignment.AdueDate) && isCompleted == assignment.isCompleted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignmentName, assignmentDescription, AdueDate, isCompleted);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", assignmentName='" + getAssignmentName() + "'" +
            ", assignmentDescription='" + getAssignmentDescription() + "'" +
            ", AdueDate='" + getAdueDate() + "'" +
            ", isCompleted='" + isIsCompleted() + "'" +
            "}";
    }
    
}
