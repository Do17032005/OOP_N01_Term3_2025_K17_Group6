package com.example.servingwebcontent.service;

import com.example.servingwebcontent.database.UserDao;
import com.example.servingwebcontent.model.User;
import com.example.servingwebcontent.model.User.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
    
    /**
     * Get user by ID
     */
    public Optional<User> getUserById(Long id) {
        return userDao.getUserById(id);
    }
    
    /**
     * Create a new user
     */
    public User createUser(User user) {
        validateUser(user);
        
        // Check if username already exists
        if (userDao.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Check if email already exists
        if (userDao.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Set default role if not provided
        if (user.getRole() == null) {
            user.setRole(UserRole.CUSTOMER);
        }
        
        return userDao.createUser(user);
    }
    
    /**
     * Update an existing user
     */
    public User updateUser(Long id, User userDetails) {
        User user = userDao.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        
        // Check if new username conflicts with existing user
        Optional<User> existingUser = userDao.findByUsername(userDetails.getUsername());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Check if new email conflicts with existing user
        existingUser = userDao.findByEmail(userDetails.getEmail());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setFullName(userDetails.getFullName());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setRole(userDetails.getRole());
        
        userDao.updateUser(user);
        return user;
    }
    
    /**
     * Delete a user
     */
    public void deleteUser(Long id) {
        User user = userDao.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        
        userDao.deleteUser(id);
    }
    
    /**
     * Get user by username
     */
    public Optional<User> getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        return userDao.findByUsername(username.trim());
    }
    
    /**
     * Get user by email
     */
    public Optional<User> getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        return userDao.findByEmail(email.trim());
    }
    
    /**
     * Get users by role
     */
    public List<User> getUsersByRole(UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        return userDao.findByRole(role.name());
    }
    
    /**
     * Search users by name
     */
    public List<User> searchUsersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllUsers();
        }
        return userDao.findByFullNameContaining(name.trim());
    }
    
    /**
     * Get users by phone number
     */
    public List<User> getUsersByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
        return userDao.findByPhoneNumber(phone.trim());
    }
    
    /**
     * Change user role
     */
    public User changeUserRole(Long id, UserRole newRole) {
        User user = userDao.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        
        if (newRole == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        
        user.setRole(newRole);
        userDao.updateUser(user);
        return user;
    }
    
    /**
     * Get admin users
     */
    public List<User> getAdminUsers() {
        return userDao.findByRole(UserRole.ADMIN.name());
    }
    
    /**
     * Get customer users
     */
    public List<User> getCustomerUsers() {
        return userDao.findByRole(UserRole.CUSTOMER.name());
    }
    
    /**
     * Validate user data
     */
    private void validateUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        
        // Validate email format
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        // Validate username format (alphanumeric and underscore only)
        if (!user.getUsername().matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Username can only contain letters, numbers, and underscores");
        }
    }

    /**
     * Authenticate user with email and password
     */
    public User authenticateUser(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        Optional<User> userOpt = userDao.findByEmail(email.trim());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Use BCrypt password verification
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    /**
     * Find user by email
     */
    public User findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        
        Optional<User> userOpt = userDao.findByEmail(email.trim());
        return userOpt.orElse(null);
    }

    /**
     * Save user (create or update)
     */
    public User saveUser(User user) {
        if (user.getId() == null) {
            // New user - create
            return createUser(user);
        } else {
            // Existing user - update
            return updateUser(user.getId(), user);
        }
    }
} 