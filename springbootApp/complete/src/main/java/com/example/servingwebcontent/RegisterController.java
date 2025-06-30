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
import java.util.UUID;

@Controller
public class RegisterController {
    
    private final CustomerDAO customerDAO;
    
    @Autowired
    public RegisterController(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

        @GetMapping("/index")
    public String showHomePage(Model model, HttpSession session) {
        return "index";
    }

    // === SHOW REGISTER PAGE ===
    @GetMapping("/register")
    public String showRegisterPage(Model model, HttpSession session) {
        // Check if already logged in
        if (session.getAttribute("loggedInCustomer") != null) {
            return "redirect:/";
        }
        
        model.addAttribute("customer", new Customer());
        return "register";
    }

    // === PROCESS REGISTRATION ===
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute Customer customer, 
                                     Model model, 
                                     RedirectAttributes redirectAttributes) {
        try {
            // Validate input
            ValidationUtils.validateCustomer(customer.getName(), customer.getEmail(), customer.getPhoneNumber());
            
            // Check if email already exists
            List<Customer> existingCustomers = customerDAO.getAllCustomers();
            boolean emailExists = existingCustomers.stream()
                .anyMatch(c -> c.getEmail().equalsIgnoreCase(customer.getEmail()));
            
            if (emailExists) {
                model.addAttribute("errorMessage", "Email này đã được đăng ký! Vui lòng sử dụng email khác.");
                model.addAttribute("customer", customer);
                return "register";
            }
            
            // Check if phone already exists
            boolean phoneExists = existingCustomers.stream()
                .anyMatch(c -> c.getPhoneNumber().equals(customer.getPhoneNumber()));
            
            if (phoneExists) {
                model.addAttribute("errorMessage", "Số điện thoại này đã được đăng ký! Vui lòng sử dụng số khác.");
                model.addAttribute("customer", customer);
                return "register";
            }
            
            // Generate ID if not provided
            if (ValidationUtils.isEmpty(customer.getId())) {
                customer.setId(UUID.randomUUID().toString());
            }
            
            // Save customer
            customerDAO.insertCustomer(customer);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Đăng ký thành công! Bạn có thể đăng nhập với email và số điện thoại vừa đăng ký.");
            
            return "redirect:/login";
            
        } catch (IllegalArgumentException e) {
            // Validation error
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("customer", customer);
            return "register";
        } catch (Exception e) {
            // System error
            model.addAttribute("errorMessage", "Lỗi hệ thống khi đăng ký: " + e.getMessage());
            model.addAttribute("customer", customer);
            return "register";
        }
    }
} 