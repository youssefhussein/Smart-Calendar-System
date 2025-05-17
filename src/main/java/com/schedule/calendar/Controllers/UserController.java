package com.schedule.calendar.Controllers;

import com.schedule.calendar.Models.User;
import com.schedule.calendar.Models.UserType;
import com.schedule.calendar.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/auth")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView addUser() {
        ModelAndView mav = new ModelAndView("auth/signup");
        User newUser = new User();
        mav.addObject("user", newUser);
        return mav;
    }
    
    @PostMapping("/signup")
    public String saveUser(@ModelAttribute @Valid  User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole("USER");
        user.setUserType(UserType.DEFAULT);
        userRepository.save(user);
        return "redirect:/auth/login?registered=true";
    }
    
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView("auth/login");
        User newUser = new User();
        mav.addObject("user", newUser);
        return mav;
    }
    
    
}
