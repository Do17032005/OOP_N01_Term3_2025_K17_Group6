package com.example.servingwebcontent;

import com.example.servingwebcontent.model.User;
import com.example.servingwebcontent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class UserCrudController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listUsers(Model model, 
                          @RequestParam(required = false) String keyword,
                          @RequestParam(required = false) String role,
                          @RequestParam(defaultValue = "0") int page) {
        try {
            List<User> users = userService.getAllUsers();
            
            // Filter by keyword
            if (keyword != null && !keyword.trim().isEmpty()) {
                users = users.stream()
                    .filter(user -> user.getFullName().toLowerCase().contains(keyword.toLowerCase()) ||
                                   user.getEmail().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
            }
            
            // Filter by role
            if (role != null && !role.trim().isEmpty()) {
                users = users.stream()
                    .filter(user -> user.getRole().toString().equalsIgnoreCase(role))
                    .toList();
            }

            // Pagination
            int pageSize = 10;
            int totalPages = (int) Math.ceil((double) users.size() / pageSize);
            int start = page * pageSize;
            int end = Math.min(start + pageSize, users.size());
            
            List<User> pagedUsers = users.subList(start, end);

            // Prepare model data
            model.addAttribute("items", pagedUsers);
            model.addAttribute("title", "Users");
            model.addAttribute("icon", "bi bi-people");
            model.addAttribute("addUrl", "/users/add");
            model.addAttribute("listUrl", "/users");
            model.addAttribute("searchUrl", "/users");
            model.addAttribute("viewUrl", "/users/view");
            model.addAttribute("editUrl", "/users/edit");
            model.addAttribute("deleteUrl", "/users/delete");
            model.addAttribute("keyword", keyword);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);

            // Define columns
            List<Map<String, Object>> columns = Arrays.asList(
                Map.of("field", "username", "label", "Username", "type", ""),
                Map.of("field", "fullName", "label", "Full Name", "type", ""),
                Map.of("field", "email", "label", "Email", "type", ""),
                Map.of("field", "phoneNumber", "label", "Phone", "type", ""),
                Map.of("field", "role", "label", "Role", "type", "status")
            );
            model.addAttribute("columns", columns);

            // Filter options
            List<Map<String, String>> filterOptions = Arrays.asList(
                Map.of("value", "CUSTOMER", "label", "Customer"),
                Map.of("value", "ADMIN", "label", "Admin")
            );
            model.addAttribute("filterOptions", filterOptions);
            model.addAttribute("selectedFilter", role);

        } catch (Exception e) {
            model.addAttribute("error", "Error loading users: " + e.getMessage());
        }

        return "crud";
    }
} 