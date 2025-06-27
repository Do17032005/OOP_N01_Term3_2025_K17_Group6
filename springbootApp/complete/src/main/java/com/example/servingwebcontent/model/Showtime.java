package com.example.servingwebcontent.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "showtimes")
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    
    @Column(name = "price", nullable = false)
    private double price;
    
    @OneToMany(mappedBy = "showtime", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets;
    
    @PrePersist
    @PreUpdate
    protected void calculateEndTime() {
        if (movie != null && startTime != null) {
            this.endTime = startTime.plusMinutes(movie.getDuration());
        }
    }
    
    // Default constructor
    public Showtime() {}

    // Constructor with required fields
    public Showtime(Movie movie, Room room, LocalDateTime startTime, double price) {
        this.movie = movie;
        this.room = room;
        this.startTime = startTime;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public List<Ticket> getTickets() {
        return tickets;
    }
    
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
    
    // Business logic methods
    public boolean isAvailable() {
        return startTime.isAfter(LocalDateTime.now());
    }
    
    public boolean isFullyBooked() {
        if (room == null || tickets == null) {
            return false;
        }
        return tickets.size() >= room.getCapacity();
    }
    
    public int getAvailableSeats() {
        if (room == null || tickets == null) {
            return room != null ? room.getCapacity() : 0;
        }
        return room.getCapacity() - tickets.size();
    }
    
    public String getFormattedStartTime() {
        return startTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
    }
    
    public String getFormattedDate() {
        return startTime.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Override
    public String toString() {
        return "Showtime{" +
                "id=" + id +
                ", movie=" + (movie != null ? movie.getTitle() : "null") +
                ", room=" + (room != null ? room.getName() : "null") +
                ", startTime=" + startTime +
                ", price=" + price +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Showtime showtime = (Showtime) obj;
        return id != null && id.equals(showtime.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
} 