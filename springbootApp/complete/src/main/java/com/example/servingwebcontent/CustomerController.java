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
}

@Configuration
class LoginInterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
            .excludePathPatterns("/login", "/logout", "/static/**", "/css/**", "/js/**", "/images/**");
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