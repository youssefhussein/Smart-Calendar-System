package com.schedule.calendar.Models;

import java.time.LocalDate;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
@SuppressWarnings("unused")
@Entity
@Table(name = "projects")

public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String projectName;
    private String projectDescription;
    private boolean isCompleted;
    private LocalDate PdueDate;


    public Project() {
    }

    public Project(Long id, String projectName, String projectDescription, boolean isCompleted, LocalDate PdueDate) {
        this.id = id;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.isCompleted = isCompleted;
        this.PdueDate = PdueDate;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return this.projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
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

    public LocalDate getPdueDate() {
        return this.PdueDate;
    }

    public void setPdueDate(LocalDate PdueDate) {
        this.PdueDate = PdueDate;
    }

    public Project id(Long id) {
        setId(id);
        return this;
    }

    public Project projectName(String projectName) {
        setProjectName(projectName);
        return this;
    }

    public Project projectDescription(String projectDescription) {
        setProjectDescription(projectDescription);
        return this;
    }

    public Project isCompleted(boolean isCompleted) {
        setIsCompleted(isCompleted);
        return this;
    }

    public Project PdueDate(LocalDate PdueDate) {
        setPdueDate(PdueDate);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Project)) {
            return false;
        }
        Project project = (Project) o;
        return Objects.equals(id, project.id) && Objects.equals(projectName, project.projectName) && Objects.equals(projectDescription, project.projectDescription) && isCompleted == project.isCompleted && Objects.equals(PdueDate, project.PdueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectName, projectDescription, isCompleted, PdueDate);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", projectName='" + getProjectName() + "'" +
            ", projectDescription='" + getProjectDescription() + "'" +
            ", isCompleted='" + isIsCompleted() + "'" +
            ", PdueDate='" + getPdueDate() + "'" +
            "}";
    }
    
}
