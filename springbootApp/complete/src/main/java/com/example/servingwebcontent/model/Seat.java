package com.example.servingwebcontent.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    
    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Column(name = "rows_number", nullable = false)
    private String rowNumber;
    
    @Column(name = "columns_number", nullable = false)
    private int columnNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false)
    private SeatType seatType;
    
    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets;
    
    // Default constructor
    public Seat() {}

    // Constructor with required fields
    public Seat(Room room, String seatNumber, String rowNumber, int columnNumber, SeatType seatType) {
        this.room = room;
        this.seatNumber = seatNumber;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.seatType = seatType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    
    public String getRowNumber() {
        return rowNumber;
    }
    
    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }
    
    public int getColumnNumber() {
        return columnNumber;
    }
    
    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }
    
    public SeatType getSeatType() {
        return seatType;
    }
    
    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }
    
    public List<Ticket> getTickets() {
        return tickets;
    }
    
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
    
    // Business logic methods
    public boolean isAvailable() {
        return tickets == null || tickets.isEmpty() || 
               tickets.stream().allMatch(ticket -> ticket.isCancelled() || ticket.isRefunded());
    }
    
    public boolean isOccupied() {
        return !isAvailable();
    }
    
    public double getPriceMultiplier() {
        switch (seatType) {
            case VIP:
                return 1.5;
            case PREMIUM:
                return 1.2;
            case STANDARD:
            default:
                return 1.0;
        }
    }
    
    public String getDisplayName() {
        return rowNumber + columnNumber;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", seatNumber='" + seatNumber + '\'' +
                ", rowNumber='" + rowNumber + '\'' +
                ", columnNumber=" + columnNumber +
                ", seatType=" + seatType +
                ", room=" + (room != null ? room.getName() : "null") +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Seat seat = (Seat) obj;
        return id != null && id.equals(seat.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    public enum SeatType {
        STANDARD, PREMIUM, VIP
    }
} 