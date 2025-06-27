package com.example.servingwebcontent.service;

import com.example.servingwebcontent.database.TicketDao;
import com.example.servingwebcontent.model.Ticket;
import com.example.servingwebcontent.model.Ticket.TicketStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketService {
    
    private final TicketDao ticketDao;
    
    public TicketService(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }
    
    /**
     * Get all tickets
     */
    public List<Ticket> getAllTickets() {
        return ticketDao.getAllTickets();
    }
    
    /**
     * Get ticket by ID
     */
    public Optional<Ticket> getTicketById(Long id) {
        return ticketDao.getTicketById(id);
    }
    
    /**
     * Create a new ticket
     */
    public Ticket createTicket(Ticket ticket) {
        validateTicket(ticket);
        
        // Set default status if not provided
        if (ticket.getStatus() == null) {
            ticket.setStatus(TicketStatus.CONFIRMED);
        }
        
        // Set booking date if not provided
        if (ticket.getBookingDate() == null) {
            ticket.setBookingDate(LocalDateTime.now());
        }
        
        return ticketDao.createTicket(ticket);
    }
    
    /**
     * Update an existing ticket
     */
    public Ticket updateTicket(Long id, Ticket ticketDetails) {
        Ticket ticket = ticketDao.getTicketById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + id));
        
        ticket.setUser(ticketDetails.getUser());
        ticket.setShowtime(ticketDetails.getShowtime());
        ticket.setSeat(ticketDetails.getSeat());
        ticket.setBooking(ticketDetails.getBooking());
        ticket.setPrice(ticketDetails.getPrice());
        ticket.setStatus(ticketDetails.getStatus());
        ticket.setBookingDate(ticketDetails.getBookingDate());
        
        ticketDao.updateTicket(ticket);
        return ticket;
    }
    
    /**
     * Delete a ticket
     */
    public void deleteTicket(Long id) {
        Ticket ticket = ticketDao.getTicketById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + id));
        
        ticketDao.deleteTicket(id);
    }
    
    /**
     * Get tickets by user
     */
    public List<Ticket> getTicketsByUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return ticketDao.findByUserId(userId);
    }
    
    /**
     * Get tickets by showtime
     */
    public List<Ticket> getTicketsByShowtime(Long showtimeId) {
        if (showtimeId == null) {
            throw new IllegalArgumentException("Showtime ID cannot be null");
        }
        return ticketDao.findByShowtimeId(showtimeId);
    }
    
    /**
     * Get tickets by seat
     */
    public List<Ticket> getTicketsBySeat(Long seatId) {
        if (seatId == null) {
            throw new IllegalArgumentException("Seat ID cannot be null");
        }
        return ticketDao.findBySeatId(seatId);
    }
    
    /**
     * Get tickets by status
     */
    public List<Ticket> getTicketsByStatus(TicketStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        return ticketDao.findByStatus(status.name());
    }
    
    /**
     * Get tickets by date range
     */
    public List<Ticket> getTicketsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        return ticketDao.findByBookingDateRange(startDate, endDate);
    }
    
    /**
     * Get tickets by price range
     */
    public List<Ticket> getTicketsByPriceRange(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        return ticketDao.findByPriceRange(minPrice, maxPrice);
    }
    
    /**
     * Get confirmed tickets
     */
    public List<Ticket> getConfirmedTickets() {
        return ticketDao.findByStatus(TicketStatus.CONFIRMED.name());
    }
    
    /**
     * Get cancelled tickets
     */
    public List<Ticket> getCancelledTickets() {
        return ticketDao.findByStatus(TicketStatus.CANCELLED.name());
    }
    
    /**
     * Get refunded tickets
     */
    public List<Ticket> getRefundedTickets() {
        return ticketDao.findByStatus(TicketStatus.REFUNDED.name());
    }
    
    /**
     * Cancel a ticket
     */
    public Ticket cancelTicket(Long id) {
        Ticket ticket = ticketDao.getTicketById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + id));
        
        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new IllegalStateException("Ticket is already cancelled");
        }
        
        ticket.setStatus(TicketStatus.CANCELLED);
        ticketDao.updateTicket(ticket);
        return ticket;
    }
    
    /**
     * Refund a ticket
     */
    public Ticket refundTicket(Long id) {
        Ticket ticket = ticketDao.getTicketById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + id));
        
        if (ticket.getStatus() == TicketStatus.REFUNDED) {
            throw new IllegalStateException("Ticket is already refunded");
        }
        
        ticket.setStatus(TicketStatus.REFUNDED);
        ticketDao.updateTicket(ticket);
        return ticket;
    }
    
    /**
     * Get today's tickets
     */
    public List<Ticket> getTodayTickets() {
        return ticketDao.findTodayTickets();
    }
    
    /**
     * Calculate total revenue from tickets
     */
    public double calculateTotalRevenue() {
        return ticketDao.calculateTotalRevenue();
    }
    
    /**
     * Calculate revenue by date range
     */
    public double calculateRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        return ticketDao.calculateRevenueByDateRange(startDate, endDate);
    }
    
    /**
     * Count tickets by status
     */
    public long countTicketsByStatus(TicketStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        return ticketDao.findByStatus(status.name()).size();
    }
    
    /**
     * Get recent tickets
     */
    public List<Ticket> getRecentTickets(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be positive");
        }
        // Note: This would require a more complex query
        // For now, return all tickets and limit in memory
        List<Ticket> allTickets = ticketDao.getAllTickets();
        return allTickets.stream()
                .sorted((t1, t2) -> t2.getBookingDate().compareTo(t1.getBookingDate()))
                .limit(limit)
                .toList();
    }
    
    /**
     * Validate ticket data
     */
    private void validateTicket(Ticket ticket) {
        if (ticket.getUser() == null) {
            throw new IllegalArgumentException("User is required");
        }
        
        if (ticket.getShowtime() == null) {
            throw new IllegalArgumentException("Showtime is required");
        }
        
        if (ticket.getSeat() == null) {
            throw new IllegalArgumentException("Seat is required");
        }
        
        if (ticket.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }
} 