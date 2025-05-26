package com.schedule.calendar.Controllers;

import com.schedule.calendar.Models.User;
import com.schedule.calendar.Models.UserType;
import com.schedule.calendar.Repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/auth/signup")
    // @ModelAttribute("error_signup_flash") String errorSignupFlash will make it available in the model if it's a flash attribute.
    // No need to explicitly add it again via model.addAttribute if it's already a @ModelAttribute on the method.
    // However, to use th:if="${error_signup_flash}" directly, it needs to be in the model.
    // ModelAndView handles this slightly differently.
    public ModelAndView showSignupForm(Model model) { // Simpler to just use Model if also using @ModelAttribute this way
        ModelAndView mav = new ModelAndView("auth/signup");
        if (!model.containsAttribute("user")) { // Add user object if not already present (e.g. from a failed POST redirect with errors)
            mav.addObject("user", new User());
        }
        // If "error_signup_flash" is a flash attribute, it will be in the model automatically.
        // You can directly access it in Thymeleaf as ${error_signup_flash}
        return mav;
    }

    @PostMapping("/auth/signup")
    public String processSignup(@Valid @ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirectAttributes) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            redirectAttributes.addFlashAttribute("error_signup_flash", "Username already exists.");
            return "redirect:/auth/signup";
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            redirectAttributes.addFlashAttribute("error_signup_flash", "Email already registered.");
            return "redirect:/auth/signup";
        }
        // If you want to show field-specific errors on the signup page after a failed POST:
        if (result.hasErrors()) {
            // Add the user object back to carry over entered data
            redirectAttributes.addFlashAttribute("user", user);
            // Add binding result to display errors
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttributes.addFlashAttribute("error_signup_flash", "Please correct the errors below.");
            return "redirect:/auth/signup"; // This will re-run the GET /auth/signup
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole("USER");
        user.setUserType(UserType.DEFAULT);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success_login_flash", "Registration successful! Please login.");
        return "redirect:/auth/login";
    }

    @GetMapping("/auth/login")
    public String showLoginForm(Model model,
                                @RequestParam(value = "error", required = false) String springSecurityError, // From Spring Security
                                @RequestParam(value = "logout", required = false) String springSecurityLogout
                                // Flash attributes are automatically added to the Model
                                // @ModelAttribute("success_login_flash") String successLoginFlash,
                                // @ModelAttribute("error_signup_flash") String errorSignupFlash
                                ) {

        if (springSecurityError != null) {
            model.addAttribute("error_login_model", "Invalid username or password.");
        }
        if (springSecurityLogout != null) {
            model.addAttribute("message_login_model", "You have been logged out successfully.");
        }
        // Flash attributes like "success_login_flash" or "error_signup_flash" (if redirected from signup with error to login)
        // will be in the model if they were set via redirectAttributes.addFlashAttribute().
        // No need to explicitly add them again here if they are already flash attributes.
        // Thymeleaf can access them directly: ${success_login_flash}, ${error_signup_flash}

        return "auth/login";
    }

    @GetMapping("/profile")
    public String userProfile(Model model, Principal principal) {
        if (principal == null) {
            // This should ideally be handled by Spring Security's .authenticated() rule
            return "redirect:/auth/login";
        }
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        model.addAttribute("user", user);
        return "user/profile";
    }
}