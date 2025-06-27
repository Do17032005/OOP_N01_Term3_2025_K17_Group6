package com.example.servingwebcontent;

import com.example.servingwebcontent.model.User;
import com.example.servingwebcontent.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private MovieService movieService;
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private ShowtimeService showtimeService;

    // Admin dashboard
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.isAdmin()) {
            return "redirect:/login";
        }
        
        // Get statistics
        long totalUsers = userService.getAllUsers().size();
        long totalMovies = movieService.getAllMovies().size();
        long totalBookings = bookingService.getAllBookings().size();
        long totalShowtimes = showtimeService.getAllShowtimes().size();
        
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalMovies", totalMovies);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("totalShowtimes", totalShowtimes);
        model.addAttribute("user", user);
        
        return "admin/dashboard";
    }

    // User management
    @GetMapping("/users")
    public String users(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.isAdmin()) {
            return "redirect:/login";
        }
        
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", user);
        return "admin/users";
    }

    // Create user form
    @GetMapping("/users/create")
    public String createUserForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.isAdmin()) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        return "admin/user-form";
    }

    // Create user
    @PostMapping("/users/create")
    public String createUser(@RequestParam String username,
                            @RequestParam String email,
                            @RequestParam String password,
                            @RequestParam String fullName,
                            @RequestParam String phone,
                            @RequestParam String role,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        User adminUser = (User) session.getAttribute("user");
        if (adminUser == null || !adminUser.isAdmin()) {
            return "redirect:/login";
        }

        try {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setFullName(fullName);
            newUser.setPhoneNumber(phone);
            newUser.setRole(User.UserRole.valueOf(role.toUpperCase()));

            userService.createUser(newUser);
            redirectAttributes.addFlashAttribute("success", "User created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create user: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }

    // Edit user form
    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, HttpSession session, Model model) {
        User adminUser = (User) session.getAttribute("user");
        if (adminUser == null || !adminUser.isAdmin()) {
            return "redirect:/login";
        }

        User userToEdit = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        model.addAttribute("userToEdit", userToEdit);
        model.addAttribute("user", adminUser);
        return "admin/user-form";
    }

    // Update user
    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id,
                            @RequestParam String username,
                            @RequestParam String email,
                            @RequestParam String fullName,
                            @RequestParam String phone,
                            @RequestParam String role,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        User adminUser = (User) session.getAttribute("user");
        if (adminUser == null || !adminUser.isAdmin()) {
            return "redirect:/login";
        }

        try {
            User userToUpdate = userService.getUserById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            
            userToUpdate.setUsername(username);
            userToUpdate.setEmail(email);
            userToUpdate.setFullName(fullName);
            userToUpdate.setPhoneNumber(phone);
            userToUpdate.setRole(User.UserRole.valueOf(role.toUpperCase()));

            userService.updateUser(id, userToUpdate);
            redirectAttributes.addFlashAttribute("success", "User updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update user: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }

    // Delete user
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User adminUser = (User) session.getAttribute("user");
        if (adminUser == null || !adminUser.isAdmin()) {
            return "redirect:/login";
        }

        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }

    // Change user role
    @PostMapping("/users/{id}/role")
    public String changeUserRole(@PathVariable Long id,
                                @RequestParam String newRole,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User adminUser = (User) session.getAttribute("user");
        if (adminUser == null || !adminUser.isAdmin()) {
            return "redirect:/login";
        }

        try {
            userService.changeUserRole(id, User.UserRole.valueOf(newRole.toUpperCase()));
            redirectAttributes.addFlashAttribute("success", "User role changed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to change user role: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }

    // System settings
    @GetMapping("/settings")
    public String settings(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.isAdmin()) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        return "admin/settings";
    }
} 