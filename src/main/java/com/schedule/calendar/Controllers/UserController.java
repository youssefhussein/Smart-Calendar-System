package com.schedule.calendar.Controllers;

import com.schedule.calendar.Models.User;
import com.schedule.calendar.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public ModelAndView getUsers() {
        ModelAndView mav = new ModelAndView("list-users.html");
        List<User> users = this.userRepository.findAll();
        mav.addObject("users", users);
        return mav;
    }

    @GetMapping("/signup")
    public ModelAndView addUser() {
        ModelAndView mav = new ModelAndView("auth/signup");
        User newUser = new User();
        mav.addObject("user", newUser);
        return mav;
    }

    @PostMapping("/signup")
    public String saveUser(@ModelAttribute User user) {
        String encodedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
        user.setPassword(encodedPassword);
        this.userRepository.save(user);
        return "Added";
    }

    // 🔐 Login form
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView("auth/login");
        User newUser = new User();
        mav.addObject("user", newUser);
        return mav;
    }

    // 🔐 Check login credentials
    @PostMapping("/login")
    public ModelAndView loginCheck(@RequestParam("username") String username,
                             @RequestParam("password") String password) {
        System.out.println("Custom login controller reached ✅");
    
        User dbUser = this.userRepository.findByUsername(username);
        if (dbUser == null) {
            System.out.println("User not found ❌");
            return new ModelAndView("auth/login");
        }
    
        Boolean isPasswordMatched = BCrypt.checkpw(password, dbUser.getPassword());
        if (isPasswordMatched) {
            System.out.println("Login successful 🚀");
            return new ModelAndView("calendar");
        } else {
            System.out.println("Password mismatch ❌");
            return new ModelAndView("auth/login");
        }
    }
    
}
