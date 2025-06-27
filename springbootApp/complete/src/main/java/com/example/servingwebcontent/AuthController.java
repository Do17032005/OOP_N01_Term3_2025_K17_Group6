package com.example.servingwebcontent;

import com.example.servingwebcontent.model.User;
import com.example.servingwebcontent.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Login process
    @PostMapping("/login")
    public String login(@RequestParam String email, 
                       @RequestParam String password,
                       HttpSession session,
                       Model model) {
        try {
            User user = userService.authenticateUser(email, password);
            if (user != null) {
                // Store user info in session
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userRole", user.getRole().toString());
                session.setAttribute("userName", user.getFullName());
                
                // Redirect based on role
                if (user.isAdmin()) {
                    return "redirect:/admin/dashboard";
                } else {
                    return "redirect:/";
                }
            } else {
                model.addAttribute("error", "Invalid email or password");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return "login";
        }
    }

    // Register page
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // Register process
    @PostMapping("/register")
    public String register(@RequestParam String firstName,
                          @RequestParam String lastName,
                          @RequestParam String email,
                          @RequestParam String phone,
                          @RequestParam String password,
                          @RequestParam String confirmPassword,
                          Model model) {
        try {
            // Validate password confirmation
            if (!password.equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match");
                return "register";
            }

            // Check if email already exists
            if (userService.findByEmail(email) != null) {
                model.addAttribute("error", "Email already registered");
                return "register";
            }

            // Create new user
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(password)); // Encode password
            newUser.setFullName(firstName + " " + lastName);
            newUser.setPhoneNumber(phone);
            newUser.setUsername(email); // Using email as username
            newUser.setRole(User.UserRole.CUSTOMER);

            userService.saveUser(newUser);
            model.addAttribute("success", "Registration successful! Please login.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // Profile page
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "profile";
    }

    // Update profile
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String fullName,
                               @RequestParam String phone,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            user.setFullName(fullName);
            user.setPhoneNumber(phone);
            userService.saveUser(user);
            
            // Update session
            session.setAttribute("user", user);
            session.setAttribute("userName", user.getFullName());
            
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Update failed: " + e.getMessage());
        }
        
        return "redirect:/profile";
    }

    // Change password
    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            // Validate current password
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                redirectAttributes.addFlashAttribute("error", "Current password is incorrect");
                return "redirect:/profile";
            }

            // Validate new password confirmation
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "New passwords do not match");
                return "redirect:/profile";
            }

            // Update password with encoding
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.saveUser(user);
            
            redirectAttributes.addFlashAttribute("success", "Password changed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Password change failed: " + e.getMessage());
        }
        
        return "redirect:/profile";
    }
} 