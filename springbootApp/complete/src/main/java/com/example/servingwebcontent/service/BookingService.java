package com.example.servingwebcontent.service;

import com.example.servingwebcontent.database.BookingDao;
import com.example.servingwebcontent.model.Booking;
import com.example.servingwebcontent.model.Booking.BookingStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class BookingService {
    
    private final BookingDao bookingDao;
    
    public BookingService(BookingDao bookingDao) {
        this.bookingDao = bookingDao;
    }
    
    /**
     * Get all bookings
     */
    public List<Booking> getAllBookings() {
        return bookingDao.getAllBookings();
    }
    
    /**
     * Get booking by ID
     */
    public Optional<Booking> getBookingById(Long id) {
        return bookingDao.getBookingById(id);
    }
    
    /**
     * Get booking by booking number
     */
    public Optional<Booking> getBookingByNumber(String bookingNumber) {
        return bookingDao.findByBookingNumber(bookingNumber);
    }
    
    /**
     * Create a new booking
     */
    public Booking createBooking(Booking booking) {
        validateBooking(booking);
        
        // Generate booking number if not provided
        if (booking.getBookingNumber() == null || booking.getBookingNumber().trim().isEmpty()) {
            booking.setBookingNumber(generateBookingNumber());
        }
        
        // Set default status if not provided
        if (booking.getStatus() == null) {
            booking.setStatus(BookingStatus.PENDING);
        }
        
        // Set booking date if not provided
        if (booking.getBookingDate() == null) {
            booking.setBookingDate(LocalDateTime.now());
        }
        
        return bookingDao.createBooking(booking);
    }
    
    /**
     * Update an existing booking
     */
    public Booking updateBooking(Long id, Booking bookingDetails) {
        Booking booking = bookingDao.getBookingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        
        booking.setUser(bookingDetails.getUser());
        booking.setShowtime(bookingDetails.getShowtime());
        booking.setTickets(bookingDetails.getTickets());
        booking.setTotalAmount(bookingDetails.getTotalAmount());
        booking.setStatus(bookingDetails.getStatus());
        booking.setBookingDate(bookingDetails.getBookingDate());
        
        bookingDao.updateBooking(booking);
        return booking;
    }
    
    /**
     * Delete a booking
     */
    public void deleteBooking(Long id) {
        Booking booking = bookingDao.getBookingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        
        bookingDao.deleteBooking(id);
    }
    
    /**
     * Get bookings by user
     */
    public List<Booking> getBookingsByUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return bookingDao.findByUserId(userId);
    }
    
    /**
     * Get bookings by showtime
     */
    public List<Booking> getBookingsByShowtime(Long showtimeId) {
        if (showtimeId == null) {
            throw new IllegalArgumentException("Showtime ID cannot be null");
        }
        return bookingDao.findByShowtimeId(showtimeId);
    }
    
    /**
     * Get bookings by status
     */
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        return bookingDao.findByStatus(status.name());
    }
    
    /**
     * Get bookings by date range
     */
    public List<Booking> getBookingsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        return bookingDao.findByDateRange(startDate, endDate);
    }
    
    /**
     * Get bookings by amount range
     */
    public List<Booking> getBookingsByAmountRange(double minAmount, double maxAmount) {
        if (minAmount < 0 || maxAmount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (minAmount > maxAmount) {
            throw new IllegalArgumentException("Minimum amount cannot be greater than maximum amount");
        }
        return bookingDao.findByAmountRange(minAmount, maxAmount);
    }
    
    /**
     * Get confirmed bookings
     */
    public List<Booking> getConfirmedBookings() {
        return bookingDao.findConfirmedBookings();
    }
    
    /**
     * Get cancelled bookings
     */
    public List<Booking> getCancelledBookings() {
        return bookingDao.findCancelledBookings();
    }
    
    /**
     * Get today's bookings
     */
    public List<Booking> getTodayBookings() {
        return bookingDao.findTodayBookings();
    }
    
    /**
     * Cancel a booking
     */
    public Booking cancelBooking(Long id) {
        Booking booking = bookingDao.getBookingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled");
        }
        
        booking.setStatus(BookingStatus.CANCELLED);
        bookingDao.updateBooking(booking);
        return booking;
    }
    
    /**
     * Confirm a booking
     */
    public Booking confirmBooking(Long id) {
        Booking booking = bookingDao.getBookingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        
        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Booking is already confirmed");
        }
        
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingDao.updateBooking(booking);
        return booking;
    }
    
    /**
     * Calculate total revenue
     */
    public Double calculateTotalRevenue() {
        return bookingDao.calculateTotalRevenue();
    }
    
    /**
     * Calculate revenue by date range
     */
    public Double calculateRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        return bookingDao.calculateRevenueByDateRange(startDate, endDate);
    }
    
    /**
     * Get high value bookings
     */
    public List<Booking> getHighValueBookings(double threshold) {
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold cannot be negative");
        }
        return bookingDao.findHighValueBookings(threshold);
    }
    
    /**
     * Get recent bookings (last 7 days)
     */
    public List<Booking> getRecentBookings() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return bookingDao.findRecentBookings(sevenDaysAgo);
    }
    
    /**
     * Validate booking data
     */
    private void validateBooking(Booking booking) {
        if (booking.getUser() == null) {
            throw new IllegalArgumentException("User is required");
        }
        
        if (booking.getShowtime() == null) {
            throw new IllegalArgumentException("Showtime is required");
        }
        
        if (booking.getTickets() == null || booking.getTickets().isEmpty()) {
            throw new IllegalArgumentException("At least one ticket is required");
        }
        
        if (booking.getTotalAmount() <= 0) {
            throw new IllegalArgumentException("Total amount must be positive");
        }
    }
    
    /**
     * Generate unique booking number
     */
    private String generateBookingNumber() {
        return "BK" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
} 