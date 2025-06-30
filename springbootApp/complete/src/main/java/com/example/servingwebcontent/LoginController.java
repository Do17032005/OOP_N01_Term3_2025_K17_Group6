package com.example.servingwebcontent;

import com.example.servingwebcontent.database.CustomerDAO;
import com.example.servingwebcontent.model.Customer;
import com.example.servingwebcontent.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
public class LoginController {
    
    private final CustomerDAO customerDAO;
    
    @Autowired
    public LoginController(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    // === LOGIN PAGE ===
    @GetMapping("/login")
    public String showLoginPage(Model model, HttpSession session) {
        // Check if already logged in
        if (session.getAttribute("loggedInCustomer") != null) {
            return "redirect:/";
        }
        
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    // === LOGIN PROCESS ===
    @PostMapping("/login")
    public String processLogin(@ModelAttribute LoginForm loginForm, 
                              Model model, 
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        try {
            // Validate input
            if (!ValidationUtils.isValidEmail(loginForm.getEmail())) {
                model.addAttribute("errorMessage", "Email không hợp lệ!");
                return "login";
            }
            
            if (!ValidationUtils.isValidPhone(loginForm.getPhoneNumber())) {
                model.addAttribute("errorMessage", "Số điện thoại không hợp lệ!");
                return "login";
            }

            // Find customer by email and phone
            List<Customer> customers = customerDAO.getAllCustomers();
            Customer customer = customers.stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(loginForm.getEmail()) && 
                           c.getPhoneNumber().equals(loginForm.getPhoneNumber()))
                .findFirst()
                .orElse(null);

            if (customer == null) {
                model.addAttribute("errorMessage", "Email hoặc số điện thoại không đúng!");
                return "login";
            }

            // Login successful
            session.setAttribute("loggedInCustomer", customer);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Đăng nhập thành công! Xin chào " + customer.getName());
            
            return "redirect:/index";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            return "login";
        }
    }

    // === LOGOUT ===
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "Đăng xuất thành công!");
        return "redirect:/";
    }

    // === LOGIN FORM INNER CLASS ===
    public static class LoginForm {
        private String email;
        private String phoneNumber;

        // Constructors
        public LoginForm() {}

        public LoginForm(String email, String phoneNumber) {
            this.email = email;
            this.phoneNumber = phoneNumber;
        }

        // Getters and Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
} 