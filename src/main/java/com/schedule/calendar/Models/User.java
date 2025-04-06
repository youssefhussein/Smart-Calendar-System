package com.schedule.calendar.Models;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id" , unique = true , nullable = false)
    private int id;
    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.STUDENT;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    public User() {
    }
    
    public User(String username, String email, String password, UserRole role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public UserRole getRole() {
        return this.role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    public void addTask(Task task) {
        this.tasks.add(task);
        task.setUser(this); 
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
        task.setUser(null); 
    }
    public User username(String username) {
        setUsername(username);
        return this;
    }
    
    public User email(String email) {
        setEmail(email);
        return this;
    }
    
    public User password(String password) {
        setPassword(password);
        return this;
    }
    
    public User role(UserRole role) {
        setRole(role);
        return this;
    }
  public int getId() {
        return id;
    }

    public void setId(int id) {
     this.id = id;
    }    
    
   
    
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(role, user.role);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(username, email, password, role);
    }
    
    @Override
    public String toString() {
        return "{" +
                " username='" + getUsername() + "'" +
                ", email='" + getEmail() + "'" +
                ", password='" + getPassword() + "'" +
                ", role='" + getRole() + "'" +
                "}";
    }
    
}
