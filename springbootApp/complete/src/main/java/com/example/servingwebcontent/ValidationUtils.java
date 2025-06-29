package com.example.servingwebcontent;

import java.util.regex.Pattern;

/**
 * Utility class cho validation
 * Chứa các method validation chung cho toàn bộ ứng dụng
 */
public class ValidationUtils {
    
    // Email pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    // Phone pattern (Vietnamese)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^(0|\\+84)(\\d{9,10})$"
    );
    
    /**
     * Kiểm tra string có null hoặc empty không
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    /**
     * Kiểm tra email có hợp lệ không
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Kiểm tra số điện thoại có hợp lệ không
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Kiểm tra độ dài string
     */
    public static boolean isValidLength(String value, int minLength, int maxLength) {
        if (isEmpty(value)) {
            return false;
        }
        int length = value.trim().length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * Kiểm tra ID có hợp lệ không
     */
    public static boolean isValidId(String id) {
        return !isEmpty(id) && id.trim().length() > 0;
    }
    
    /**
     * Kiểm tra giá có hợp lệ không
     */
    public static boolean isValidPrice(double price) {
        return price >= 0;
    }
    
    /**
     * Validate customer data
     */
    public static void validateCustomer(String name, String email, String phone) {
        if (isEmpty(name)) {
            throw new IllegalArgumentException("Tên khách hàng không được để trống");
        }
        if (!isValidLength(name, 2, 100)) {
            throw new IllegalArgumentException("Tên khách hàng phải từ 2-100 ký tự");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }
        if (!isValidPhone(phone)) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
    }
    
    /**
     * Validate movie data
     */
    public static void validateMovie(String title, String description, int duration) {
        if (isEmpty(title)) {
            throw new IllegalArgumentException("Tên phim không được để trống");
        }
        if (!isValidLength(title, 1, 200)) {
            throw new IllegalArgumentException("Tên phim phải từ 1-200 ký tự");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Thời lượng phim phải lớn hơn 0");
        }
        // Description is optional, so we don't validate it
    }
    
    /**
     * Validate room data
     */
    public static void validateRoom(String name, int capacity) {
        if (isEmpty(name)) {
            throw new IllegalArgumentException("Tên phòng không được để trống");
        }
        if (!isValidLength(name, 1, 50)) {
            throw new IllegalArgumentException("Tên phòng phải từ 1-50 ký tự");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Sức chứa phòng phải lớn hơn 0");
        }
    }
    
    /**
     * Validate showtime data
     */
    public static void validateShowtime(String movieId, String roomId, String time) {
        if (!isValidId(movieId)) {
            throw new IllegalArgumentException("ID phim không hợp lệ");
        }
        if (!isValidId(roomId)) {
            throw new IllegalArgumentException("ID phòng không hợp lệ");
        }
        if (isEmpty(time)) {
            throw new IllegalArgumentException("Thời gian chiếu không được để trống");
        }
    }
    
    /**
     * Validate ticket data
     */
    public static void validateTicket(String showtimeId, String seatId, String customerId, double price) {
        if (!isValidId(showtimeId)) {
            throw new IllegalArgumentException("ID suất chiếu không hợp lệ");
        }
        if (!isValidId(seatId)) {
            throw new IllegalArgumentException("ID ghế không hợp lệ");
        }
        if (!isValidId(customerId)) {
            throw new IllegalArgumentException("ID khách hàng không hợp lệ");
        }
        if (!isValidPrice(price)) {
            throw new IllegalArgumentException("Giá vé không hợp lệ");
        }
    }
} 