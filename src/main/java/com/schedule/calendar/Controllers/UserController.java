package com.schedule.calendar.Controllers;

import com.schedule.calendar.Models.Organization;
import com.schedule.calendar.Models.User;
import com.schedule.calendar.Models.UserType;
import com.schedule.calendar.Models.StudentType; // Add this import
import com.schedule.calendar.Repositories.OrganizationRepository;
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
    private OrganizationRepository organizationRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView addUser() {
        ModelAndView mav = new ModelAndView("auth/signup");
        User newUser = new User();
        mav.addObject("user", newUser);
        // Add list of organizations for student registration
        mav.addObject("organizations", organizationRepository.findAll());
        return mav;
    }
    
    @PostMapping("/signup")
    public String saveUser(@ModelAttribute @Valid User user, 
                      @RequestParam(required = false) String organizationName,
                      @RequestParam(required = false) String organizationDescription,
                      @RequestParam(required = false) Integer organizationId) {

        // Set basic user properties
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole("USER");
        
        // Handle different user types
        if (user.getUserType() == UserType.ORGANIZATION) {
            // Organization user setup
            if (organizationName == null || organizationName.trim().isEmpty()) {
                return "redirect:/auth/signup?error=Organization name is required";
            }
            
            if (organizationRepository.existsByName(organizationName)) {
                return "redirect:/auth/signup?error=Organization name already exists";
            }
            
            Organization org = new Organization();
            org.setName(organizationName.trim());
            org.setDescription(organizationDescription != null ? organizationDescription.trim() : "");
            org.setOrganizationType("ACADEMIC"); 
            org = organizationRepository.save(org);
            
            user.setOrganization(org);
            user.setIsOrganizationAdmin(true);
            user.setStudentType(StudentType.NON_STUDENT);
            
        } else if (user.getUserType() == UserType.STUDENT && organizationId != null) {
            // Student user setup
            Organization organization = organizationRepository.findById(organizationId)
                .orElse(null);
            if (organization != null) {
                user.setOrganization(organization);
                user.setStudentType(StudentType.STUDENT);
                user.setIsOrganizationAdmin(false);
            } else {
                return "redirect:/auth/signup?error=Invalid organization selected";
            }
            
        } else {
            
            user.setOrganization(null);
            user.setIsOrganizationAdmin(false);
            user.setStudentType(StudentType.NON_STUDENT);
            user.setStudentId(null);
            user.setUserType(UserType.DEFAULT);
            
        }

        try {
            userRepository.save(user);
            return "redirect:/auth/login?registered=true";
        } catch (Exception e) {
            return "redirect:/auth/signup?error=Failed to create user: " + e.getMessage();
        }
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
