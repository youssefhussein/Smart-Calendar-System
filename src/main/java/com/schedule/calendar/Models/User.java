package com.schedule.calendar.Models;

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

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false, unique = true) // Added unique=true for email as it's common
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType = UserType.DEFAULT;

    @Column(nullable = false)
    private String userRole; // e.g., "USER", "ADMIN", "USER,ADMIN"

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Task> tasks;
}