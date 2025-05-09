package com.schedule.calendar.Controllers;

import com.schedule.calendar.Models.User;
import com.schedule.calendar.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;


@Controller
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/signup")
    public ModelAndView addUser() {
        ModelAndView mav = new ModelAndView("auth/signup");
        User newUser = new User();
        mav.addObject("user", newUser);
        return mav;
    }

    @PostMapping("/signup")
    public String saveUser(@RequestBody User user) {
//        String encodedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
//        user.setPassword(encodedPassword);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
        return "calendar";
    }

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView("auth/login");
        User newUser = new User();
        mav.addObject("user", newUser);
        return mav;
    }

    @PostMapping("/login")
    public ModelAndView loginCheck(@RequestParam("username") String username,
                             @RequestParam("password") String password) {
        System.out.println("Custom login controller reached ✅");

        Optional<User>  dbUser = this.userRepository.findByUsername(username);
        if (dbUser.isEmpty()) {
            System.out.println("User not found ❌");
            return new ModelAndView("auth/login");
        }

        boolean isPasswordMatched = BCrypt.checkpw(password, dbUser.get().getPassword());
        if (isPasswordMatched) {
            System.out.println("Login successful 🚀");
            return new ModelAndView("calendar");
        } else {
            System.out.println("Password mismatch ❌");
            return new ModelAndView("auth/login");
        }
    }
    
}
