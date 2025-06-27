package com.example.servingwebcontent.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(name = "total_seats", nullable = false)
    private int totalSeats;

    @Column(name = "capacity", nullable = false)
    private int capacity;
    
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Showtime> showtimes;
    
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seats;

    // Default constructor
    public Room() {}

    // Constructor with required fields
    public Room(String name, int totalSeats, int capacity) {
        this.name = name;
        this.totalSeats = totalSeats;
        this.capacity = capacity;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    public List<Showtime> getShowtimes() {
        return showtimes;
    }
    
    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }
    
    public List<Seat> getSeats() {
        return seats;
    }
    
    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
    
    // Business logic methods
    public boolean isAvailable() {
        return showtimes != null && showtimes.stream().anyMatch(Showtime::isAvailable);
    }
    
    public int getAvailableSeats() {
        return capacity - (seats != null ? seats.size() : 0);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", totalSeats=" + totalSeats +
                ", capacity=" + capacity +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Room room = (Room) obj;
        return id != null && id.equals(room.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
} 