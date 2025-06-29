package com.example.servingwebcontent;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Global Exception Handler để xử lý lỗi tập trung
 * Xử lý các exception chung cho toàn bộ ứng dụng
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Xử lý lỗi database
     */
    @ExceptionHandler(Exception.class)
    public String handleDatabaseException(Exception e, Model model, RedirectAttributes redirectAttributes) {
        String errorMessage = "Lỗi hệ thống: " + e.getMessage();
        
        // Log error (in production, use proper logging framework)
        System.err.println("Error occurred: " + e.getMessage());
        e.printStackTrace();
        
        // Add error message to model or redirect attributes
        if (model != null) {
            model.addAttribute("error", errorMessage);
        }
        if (redirectAttributes != null) {
            redirectAttributes.addFlashAttribute("error", errorMessage);
        }
        
        return "error";
    }

    /**
     * Xử lý lỗi validation
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleValidationException(IllegalArgumentException e, Model model, RedirectAttributes redirectAttributes) {
        String errorMessage = "Dữ liệu không hợp lệ: " + e.getMessage();
        
        if (model != null) {
            model.addAttribute("error", errorMessage);
        }
        if (redirectAttributes != null) {
            redirectAttributes.addFlashAttribute("error", errorMessage);
        }
        
        return "error";
    }

    /**
     * Xử lý lỗi không tìm thấy resource
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException e, Model model, RedirectAttributes redirectAttributes) {
        String errorMessage = "Không tìm thấy: " + e.getMessage();
        
        if (model != null) {
            model.addAttribute("error", errorMessage);
        }
        if (redirectAttributes != null) {
            redirectAttributes.addFlashAttribute("error", errorMessage);
        }
        
        return "error";
    }
}

/**
 * Custom exception cho resource không tìm thấy
 */
class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
} 