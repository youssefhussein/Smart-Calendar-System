package com.schedule.calendar.Controllers;

import com.schedule.calendar.Models.User;
import com.schedule.calendar.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public ModelAndView listUsers() {
        List<User> users = userRepository.findAll();
        ModelAndView mav = new ModelAndView("admin/users");
        mav.addObject("users", users);
        return mav;
    }

    @GetMapping("/users/new")
    public ModelAndView showNewUserForm() {
        ModelAndView mav = new ModelAndView("admin/user-form");
        User user = new User();
        mav.addObject("user", user);
        mav.addObject("action", "create");
        return mav;
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute User user) {

        if(user.getPassword() != null && !user.getPassword().isEmpty()){
            String encodedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
            user.setPassword(encodedPassword);
        }
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public ModelAndView showEditUserForm(@PathVariable("id") int id) {
        Optional<User> userOpt = userRepository.findById(id);
        ModelAndView mav = new ModelAndView("admin/user-form");
        if (userOpt.isPresent()) {
            mav.addObject("user", userOpt.get());
            mav.addObject("action", "edit");
            return mav;
        }
        return new ModelAndView("redirect:/admin/users");
    }

    @PostMapping("/users/edit")
    public String updateUser(@ModelAttribute User user) {

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
            user.setPassword(encodedPassword);
        } else {
            User existingUser = userRepository.findById(user.getId()).orElse(null);
            if (existingUser != null) {
                user.setPassword(existingUser.getPassword());
            }
        }
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }
}
