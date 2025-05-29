package com.schedule.calendar.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(unique = true)
    private String username;
    @Column(nullable = false)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType = UserType.DEFAULT;
    @Column(nullable = false)
    private String userRole;
    @Column(nullable = true)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Task> tasks;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private StudentType studentType = StudentType.NON_STUDENT;
    
    @Column(nullable = true)
    private String studentId;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "organization_id", nullable = true)
    private Organization organization;
    
    @Column(nullable = true)
    private Boolean isOrganizationAdmin = false;
    
}
