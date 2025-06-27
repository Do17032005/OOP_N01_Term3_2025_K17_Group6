package com.example.servingwebcontent;

import com.example.servingwebcontent.database.CustomerDAO;
import com.example.servingwebcontent.model.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

@Controller
public class CustomerController {
    private final CustomerDAO customerDAO = new CustomerDAO();

    @GetMapping("/customers")
    public String getAllCustomers(Model model) {
        List<Customer> customers = customerDAO.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customers";
    }

    @GetMapping("/customers/add")
    public String showAddForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "add-customer";
    }

    @PostMapping("/customers/add")
    public String addCustomer(@ModelAttribute Customer customer, Model model) {
        customerDAO.insertCustomer(customer);
        model.addAttribute("message", "Thêm khách hàng thành công!");
        model.addAttribute("customer", new Customer());
        return "add-customer";
    }

    @GetMapping("/customers/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        List<Customer> customers = customerDAO.getAllCustomers();
        Customer customer = customers.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
        model.addAttribute("customer", customer);
        return "edit-customer";
    }

    @PostMapping("/customers/edit")
    public String editCustomer(@ModelAttribute Customer customer) {
        customerDAO.updateCustomer(customer);
        return "redirect:/customers";
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable String id) {
        customerDAO.deleteCustomer(id);
        return "redirect:/customers";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new Customer());
        return "customer-login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("loginForm") Customer loginForm, Model model, jakarta.servlet.http.HttpSession session) {
        for (Customer c : customerDAO.getAllCustomers()) {
            if (c.getEmail().equals(loginForm.getEmail()) && c.getPhoneNumber().equals(loginForm.getPhoneNumber())) {
                session.setAttribute("loggedInCustomer", c);
                return "redirect:/";
            }
        }
        model.addAttribute("loginError", "Email hoặc số điện thoại không đúng!");
        return "customer-login";
    }

    @GetMapping("/logout")
    public String logout(jakarta.servlet.http.HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "sign-up";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("customer") Customer customer, Model model) {
        // Kiểm tra trùng email/số điện thoại
        for (Customer c : customerDAO.getAllCustomers()) {
            if (c.getEmail().equals(customer.getEmail())) {
                model.addAttribute("message", "Email đã được sử dụng!");
                return "sign-up";
            }
            if (c.getPhoneNumber().equals(customer.getPhoneNumber())) {
                model.addAttribute("message", "Số điện thoại đã được sử dụng!");
                return "sign-up";
            }
        }
        // Sinh id tự động nếu chưa có
        if (customer.getId() == null || customer.getId().isEmpty()) {
            customer.setId(UUID.randomUUID().toString());
        }
        // Lưu customer vào DB
        customerDAO.insertCustomer(customer);
        model.addAttribute("message", "Đăng ký thành công!");
        model.addAttribute("customer", new Customer());
        return "sign-up";
    }
}

@Configuration
class LoginInterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
            .excludePathPatterns(
                "/login", "/logout", "/register", "/static/**", "/css/**", "/js/**", "/images/**"
            );
    }
}

class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object loggedInCustomer = request.getSession().getAttribute("loggedInCustomer");
        String uri = request.getRequestURI();
        if (loggedInCustomer == null && !uri.equals("/login") && !uri.equals("/logout")) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}