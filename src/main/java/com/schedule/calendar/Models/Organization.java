package com.schedule.calendar.Models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizations")
@Data
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column
    private String description;
    
    @Column(nullable = false)
    private String organizationType = "ACADEMIC"; // Adding default value
    
    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
    private List<User> students = new ArrayList<>();
    
    @Column(nullable = true)
    private Integer maxStudents;
    
    public void addStudent(User student) {
        students.add(student);
        student.setOrganization(this);
        student.setStudentType(StudentType.STUDENT);
    }
}