package com.example.servingwebcontent;

import java.util.List;
import java.util.UUID;
import com.example.servingwebcontent.database.CustomerDAO;
import com.example.servingwebcontent.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    
    private final CustomerDAO customerDAO;

    @Autowired
    public CustomerController(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    // === LIST CUSTOMERS ===
    @GetMapping("/list")
    public String listCustomer(Model model) {
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            model.addAttribute("customers", customers);
            return "customer/list";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải danh sách khách hàng: " + e.getMessage());
            return "customer/list";
        }
    }

    // === SHOW ADD FORM ===
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer/add";
    }

    // === ADD CUSTOMER ===
    @PostMapping("/add")
    public String addCustomer(@ModelAttribute Customer customer, 
                             Model model, 
                             RedirectAttributes redirectAttributes) {
        try {
            // Validation using ValidationUtils
            ValidationUtils.validateCustomer(customer.getName(), customer.getEmail(), customer.getPhoneNumber());
            
            // Generate ID if not provided
            if (ValidationUtils.isEmpty(customer.getId())) {
                customer.setId(UUID.randomUUID().toString());
            }
            
            customerDAO.insertCustomer(customer);
            redirectAttributes.addFlashAttribute("success", "Thêm khách hàng thành công!");
            return "redirect:/customer/list";
            
        } catch (IllegalArgumentException e) {
            // Validation error
            model.addAttribute("error", e.getMessage());
            model.addAttribute("customer", customer);
            return "customer/add";
        } catch (Exception e) {
            // System error
            model.addAttribute("error", "Lỗi hệ thống khi thêm khách hàng: " + e.getMessage());
            model.addAttribute("customer", customer);
            return "customer/add";
        }
    }

    // === SHOW EDIT FORM ===
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                redirectAttributes.addFlashAttribute("error", "ID khách hàng không hợp lệ!");
                return "redirect:/customer/list";
            }
            
            // Find customer by ID
            Customer customer = findCustomerById(id);
            if (customer == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy khách hàng với ID: " + id);
                return "redirect:/customer/list";
            }
            
            model.addAttribute("customer", customer);
            return "customer/edit";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            return "redirect:/customer/list";
        }
    }

    // === UPDATE CUSTOMER ===
    @PostMapping("/edit/{id}")
    public String updateCustomer(@PathVariable String id, 
                                @ModelAttribute Customer customer, 
                                Model model, 
                                RedirectAttributes redirectAttributes) {
        try {
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                redirectAttributes.addFlashAttribute("error", "ID khách hàng không hợp lệ!");
                return "redirect:/customer/list";
            }
            
            // Validate customer data
            ValidationUtils.validateCustomer(customer.getName(), customer.getEmail(), customer.getPhoneNumber());
            
            // Set ID from path
            customer.setId(id);
            
            // Check if customer exists
            Customer existingCustomer = findCustomerById(id);
            if (existingCustomer == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy khách hàng để cập nhật!");
                return "redirect:/customer/list";
            }
            
            customerDAO.updateCustomer(customer);
            redirectAttributes.addFlashAttribute("success", "Cập nhật khách hàng thành công!");
            return "redirect:/customer/list";
            
        } catch (IllegalArgumentException e) {
            // Validation error
            model.addAttribute("error", e.getMessage());
            model.addAttribute("customer", customer);
            return "customer/edit";
        } catch (Exception e) {
            // System error
            model.addAttribute("error", "Lỗi hệ thống khi cập nhật khách hàng: " + e.getMessage());
            model.addAttribute("customer", customer);
            return "customer/edit";
        }
    }

    // === DELETE CUSTOMER ===
    @PostMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                redirectAttributes.addFlashAttribute("error", "ID khách hàng không hợp lệ!");
                return "redirect:/customer/list";
            }
            
            // Check if customer exists
            Customer existingCustomer = findCustomerById(id);
            if (existingCustomer == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy khách hàng để xóa!");
                return "redirect:/customer/list";
            }
            
            customerDAO.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("success", "Xóa khách hàng thành công!");
            return "redirect:/customer/list";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống khi xóa khách hàng: " + e.getMessage());
            return "redirect:/customer/list";
        }
    }

    // === PRIVATE HELPER METHODS ===
    
    /**
     * Tìm customer theo ID
     * Note: CustomerDAO chưa có method getCustomerById, nên phải filter từ getAllCustomers
     */
    private Customer findCustomerById(String id) {
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm kiếm khách hàng: " + e.getMessage(), e);
        }
    }
}