package com.example.servingwebcontent;

import com.example.servingwebcontent.database.CustomerDAO;
import com.example.servingwebcontent.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;
import java.util.Arrays;

@Controller
public class CustomerController {
    
    private final CustomerDAO customerDAO;
    
    @Autowired
    public CustomerController(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    // === CUSTOMER MANAGEMENT ===
    @GetMapping("/")
    public String index(Model model, jakarta.servlet.http.HttpSession session) {
        // Get logged in customer from session
        Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
        if (loggedInCustomer != null) {
            model.addAttribute("loggedInCustomer", loggedInCustomer);
        }
        return "index";
    }
    
    @GetMapping("/customers")
    public String getAllCustomers(Model model) {
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            model.addAttribute("customers", customers);
            return "customer/list";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải danh sách khách hàng: " + e.getMessage());
            return "customer/list";
        }
    }

    @GetMapping("/customers/add")
    public String showAddForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer/add";
    }

    @PostMapping("/customers/add")
    public String addCustomer(@ModelAttribute Customer customer, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (customer.getId() == null || customer.getId().trim().isEmpty()) {
                customer.setId(UUID.randomUUID().toString());
            }
            
            // Validate customer data
            if (!isValidCustomer(customer)) {
                model.addAttribute("error", "Thông tin khách hàng không hợp lệ!");
                model.addAttribute("customer", customer);
                return "customer/add";
            }
            
            customerDAO.insertCustomer(customer);
            redirectAttributes.addFlashAttribute("success", "Thêm khách hàng thành công!");
            return "redirect:/customers";
        } catch (Exception e) {
            model.addAttribute("error", "Thêm khách hàng thất bại: " + e.getMessage());
            model.addAttribute("customer", customer);
            return "customer/add";
        }
    }

    @GetMapping("/customers/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Customer customer = findCustomerById(id);
            if (customer == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy khách hàng với ID: " + id);
                return "redirect:/customers";
            }
            model.addAttribute("customer", customer);
            return "customer/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể tải thông tin khách hàng: " + e.getMessage());
            return "redirect:/customers";
        }
    }

    @PostMapping("/customers/edit")
    public String editCustomer(@ModelAttribute Customer customer, RedirectAttributes redirectAttributes) {
        try {
            if (!isValidCustomer(customer)) {
                redirectAttributes.addFlashAttribute("error", "Thông tin khách hàng không hợp lệ!");
                return "redirect:/customers/edit/" + customer.getId();
            }
            
            customerDAO.updateCustomer(customer);
            redirectAttributes.addFlashAttribute("success", "Cập nhật khách hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật khách hàng thất bại: " + e.getMessage());
        }
        return "redirect:/customers";
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            Customer customer = findCustomerById(id);
            if (customer == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy khách hàng để xóa!");
                return "redirect:/customers";
            }
            
            customerDAO.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("success", "Xóa khách hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Xóa khách hàng thất bại: " + e.getMessage());
        }
        return "redirect:/customers";
    }

    // === AUTHENTICATION ===
    
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new Customer());
        return "customer/login";
    }

    @GetMapping("/customer/login")
    public String showLoginFormCustomer(Model model) {
        model.addAttribute("loginForm", new Customer());
        return "customer/login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("loginForm") Customer loginForm, Model model, jakarta.servlet.http.HttpSession session) {
        return authenticateUser(loginForm, model, session);
    }

    @PostMapping("/customer/login")
    public String processLoginCustomer(@ModelAttribute("loginForm") Customer loginForm, Model model, jakarta.servlet.http.HttpSession session) {
        return authenticateUser(loginForm, model, session);
    }

    @GetMapping("/logout")
    public String logout(jakarta.servlet.http.HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer/register";
    }

    @GetMapping("/customer/register")
    public String showRegisterFormCustomer(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer/register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("customer") Customer customer, Model model) {
        return registerCustomer(customer, model);
    }

    @PostMapping("/customer/register")
    public String processRegisterCustomer(@ModelAttribute("customer") Customer customer, Model model) {
        return registerCustomer(customer, model);
    }

    // === PRIVATE HELPER METHODS ===
    
    private String authenticateUser(Customer loginForm, Model model, jakarta.servlet.http.HttpSession session) {
        try {
            if (loginForm.getEmail() == null || loginForm.getPhoneNumber() == null) {
                model.addAttribute("loginError", "Vui lòng nhập đầy đủ thông tin!");
                return "customer/login";
            }
            
            Customer authenticatedCustomer = customerDAO.getAllCustomers().stream()
                .filter(c -> c.getEmail().equals(loginForm.getEmail()) && 
                           c.getPhoneNumber().equals(loginForm.getPhoneNumber()))
                .findFirst()
                .orElse(null);
                
            if (authenticatedCustomer != null) {
                session.setAttribute("loggedInCustomer", authenticatedCustomer);
                return "redirect:/";
            } else {
                model.addAttribute("loginError", "Email hoặc số điện thoại không đúng!");
                return "customer/login";
            }
        } catch (Exception e) {
            model.addAttribute("loginError", "Đăng nhập thất bại: " + e.getMessage());
            return "customer/login";
        }
    }
    
    private String registerCustomer(Customer customer, Model model) {
        try {
            if (!isValidCustomer(customer)) {
                model.addAttribute("error", "Thông tin đăng ký không hợp lệ!");
                return "customer/register";
            }
            
            // Check for duplicate email/phone
            if (isEmailExists(customer.getEmail())) {
                model.addAttribute("error", "Email đã được sử dụng!");
                return "customer/register";
            }
            
            if (isPhoneExists(customer.getPhoneNumber())) {
                model.addAttribute("error", "Số điện thoại đã được sử dụng!");
                return "customer/register";
            }
            
            // Generate ID if not provided
            if (customer.getId() == null || customer.getId().isEmpty()) {
                customer.setId(UUID.randomUUID().toString());
            }
            
            customerDAO.insertCustomer(customer);
            model.addAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            model.addAttribute("customer", new Customer());
        } catch (Exception e) {
            model.addAttribute("error", "Đăng ký thất bại: " + e.getMessage());
        }
        return "customer/register";
    }
    
    private Customer findCustomerById(String id) {
        return customerDAO.getAllCustomers().stream()
            .filter(c -> c.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    private boolean isValidCustomer(Customer customer) {
        return customer != null && 
               customer.getName() != null && !customer.getName().trim().isEmpty() &&
               customer.getEmail() != null && !customer.getEmail().trim().isEmpty() &&
               customer.getPhoneNumber() != null && !customer.getPhoneNumber().trim().isEmpty();
    }
    
    private boolean isEmailExists(String email) {
        return customerDAO.getAllCustomers().stream()
            .anyMatch(c -> c.getEmail().equals(email));
    }
    
    private boolean isPhoneExists(String phone) {
        return customerDAO.getAllCustomers().stream()
            .anyMatch(c -> c.getPhoneNumber().equals(phone));
    }
}

@Configuration
class LoginInterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
            .excludePathPatterns(
                "/", "/login", "/logout", "/register", 
                "/customer/login", "/customer/register",
                "/static/**", "/css/**", "/js/**", "/images/**"
            );
    }
}

class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object loggedInCustomer = request.getSession().getAttribute("loggedInCustomer");
        String uri = request.getRequestURI();
        
        // List of paths that don't require authentication
        List<String> publicPaths = Arrays.asList(
            "/", "/login", "/logout", "/register", 
            "/customer/login", "/customer/register",
            "/static", "/css", "/js", "/images"
        );
        
        // Check if current URI is a public path
        boolean isPublicPath = publicPaths.stream()
            .anyMatch(path -> uri.equals(path) || uri.startsWith(path + "/"));
        
        if (loggedInCustomer == null && !isPublicPath) {
            response.sendRedirect("/customer/login");
            return false;
        }
        return true;
    }
}