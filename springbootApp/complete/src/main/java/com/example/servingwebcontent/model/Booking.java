package com.example.servingwebcontent.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;
    
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets;
    
    @Column(name = "booking_number", unique = true, nullable = false)
    private String bookingNumber;
    
    @Column(name = "total_amount", nullable = false)
    private double totalAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;
    
    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (bookingDate == null) {
            bookingDate = LocalDateTime.now();
        }
        if (status == null) {
            status = BookingStatus.PENDING;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Default constructor
    public Booking() {}
    
    // Constructor with required fields
    public Booking(User user, Showtime showtime, String bookingNumber, double totalAmount) {
        this.user = user;
        this.showtime = showtime;
        this.bookingNumber = bookingNumber;
        this.totalAmount = totalAmount;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Showtime getShowtime() {
        return showtime;
    }
    
    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }
    
    public List<Ticket> getTickets() {
        return tickets;
    }
    
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
    
    public String getBookingNumber() {
        return bookingNumber;
    }
    
    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public BookingStatus getStatus() {
        return status;
    }
    
    public void setStatus(BookingStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getBookingDate() {
        return bookingDate;
    }
    
    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Business logic methods
    public boolean isConfirmed() {
        return BookingStatus.CONFIRMED.equals(status);
    }
    
    public boolean isCancelled() {
        return BookingStatus.CANCELLED.equals(status);
    }
    
    public boolean isPending() {
        return BookingStatus.PENDING.equals(status);
    }
    
    public boolean canBeCancelled() {
        return isConfirmed() && showtime != null && showtime.isAvailable();
    }
    
    public int getTicketCount() {
        return tickets != null ? tickets.size() : 0;
    }
    
    public String getFormattedTotalAmount() {
        return String.format("%,.0f VND", totalAmount);
    }
    
    public String getFormattedBookingDate() {
        return bookingDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    
    public void addTicket(Ticket ticket) {
        if (tickets != null) {
            tickets.add(ticket);
            ticket.setBooking(this);
        }
    }
    
    public void removeTicket(Ticket ticket) {
        if (tickets != null) {
            tickets.remove(ticket);
            ticket.setBooking(null);
        }
    }
    
    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", bookingNumber='" + bookingNumber + '\'' +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", showtime=" + (showtime != null ? showtime.getId() : "null") +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", ticketCount=" + getTicketCount() +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Booking booking = (Booking) obj;
        return id != null && id.equals(booking.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    public enum BookingStatus {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }
} 