package com.example.servingwebcontent.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;
    
    @Column(name = "ticket_number", unique = true, nullable = false)
    private String ticketNumber;
    
    @Column(name = "price", nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status;
    
    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (bookingDate == null) {
            bookingDate = LocalDateTime.now();
        }
        if (status == null) {
            status = TicketStatus.CONFIRMED;
        }
    }
    
    // Default constructor
    public Ticket() {}

    // Constructor with required fields
    public Ticket(Showtime showtime, User user, Seat seat, String ticketNumber, double price) {
        this.showtime = showtime;
        this.user = user;
        this.seat = seat;
        this.ticketNumber = ticketNumber;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }

    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
    
    public Booking getBooking() {
        return booking;
    }
    
    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public TicketStatus getStatus() {
        return status;
    }
    
    public void setStatus(TicketStatus status) {
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
    
    // Business logic methods
    public boolean isConfirmed() {
        return TicketStatus.CONFIRMED.equals(status);
    }
    
    public boolean isCancelled() {
        return TicketStatus.CANCELLED.equals(status);
    }
    
    public boolean isRefunded() {
        return TicketStatus.REFUNDED.equals(status);
    }
    
    public boolean isPending() {
        return TicketStatus.PENDING.equals(status);
    }
    
    public boolean canBeCancelled() {
        return (isConfirmed() || isPending()) && showtime != null && showtime.isAvailable();
    }
    
    public String getFormattedPrice() {
        return String.format("%,.0f VND", price);
    }
    
    public String getFormattedBookingDate() {
        return bookingDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", ticketNumber='" + ticketNumber + '\'' +
                ", showtime=" + (showtime != null ? showtime.getId() : "null") +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", seat=" + (seat != null ? seat.getSeatNumber() : "null") +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ticket ticket = (Ticket) obj;
        return id != null && id.equals(ticket.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    public enum TicketStatus {
        PENDING, CONFIRMED, CANCELLED, REFUNDED
    }
}
