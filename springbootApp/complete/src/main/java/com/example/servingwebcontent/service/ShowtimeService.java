package com.example.servingwebcontent.service;

import com.example.servingwebcontent.database.MovieDao;
import com.example.servingwebcontent.database.RoomDao;
import com.example.servingwebcontent.database.ShowtimeDao;

import com.example.servingwebcontent.model.Showtime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShowtimeService {
    
    private final ShowtimeDao showtimeDao;
    private final MovieDao movieDao;
    private final RoomDao roomDao;
    

    public ShowtimeService(ShowtimeDao showtimeDao, 
                          MovieDao movieDao, 
                          RoomDao roomDao) {
        this.showtimeDao = showtimeDao;
        this.movieDao = movieDao;
        this.roomDao = roomDao;
    }
    
    /**
     * Get all showtimes
     */
    public List<Showtime> getAllShowtimes() {
        return showtimeDao.getAllShowtimes();
    }
    
    /**
     * Get showtime by ID
     */
    public Optional<Showtime> getShowtimeById(Long id) {
        return showtimeDao.getShowtimeById(id);
    }
    
    /**
     * Create a new showtime
     */
    public Showtime createShowtime(Showtime showtime) {
        validateShowtime(showtime);
        
        // Check if showtime conflicts with existing showtime in the same room
        if (showtimeDao.findByMovieIdAndRoomId(showtime.getMovie().getId(), showtime.getRoom().getId()).isPresent()) {
            throw new IllegalArgumentException("Showtime already exists for this movie and room");
        }
        
        return showtimeDao.createShowtime(showtime);
    }
    
    /**
     * Update an existing showtime
     */
    public Showtime updateShowtime(Long id, Showtime showtimeDetails) {
        Showtime showtime = showtimeDao.getShowtimeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Showtime not found with id: " + id));
        
        // Check if new showtime conflicts with existing showtime in the same room
        Optional<Showtime> existingShowtime = showtimeDao.findByMovieIdAndRoomId(
            showtimeDetails.getMovie().getId(), showtimeDetails.getRoom().getId());
        if (existingShowtime.isPresent() && !existingShowtime.get().getId().equals(id)) {
            throw new IllegalArgumentException("Showtime already exists for this movie and room");
        }
        
        showtime.setMovie(showtimeDetails.getMovie());
        showtime.setRoom(showtimeDetails.getRoom());
        showtime.setStartTime(showtimeDetails.getStartTime());
        showtime.setEndTime(showtimeDetails.getEndTime());
        showtime.setPrice(showtimeDetails.getPrice());
        
        showtimeDao.updateShowtime(showtime);
        return showtime;
    }
    
    /**
     * Delete a showtime
     */
    public void deleteShowtime(Long id) {
        Showtime showtime = showtimeDao.getShowtimeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Showtime not found with id: " + id));
        
        // Check if showtime has tickets
        // Note: This would require a join query or separate check
        // For now, we'll just delete the showtime
        
        showtimeDao.deleteShowtime(id);
    }
    
    /**
     * Get showtimes by movie
     */
    public List<Showtime> getShowtimesByMovie(Long movieId) {
        if (movieId == null) {
            throw new IllegalArgumentException("Movie ID cannot be null");
        }
        return showtimeDao.findByMovieId(movieId);
    }
    
    /**
     * Get showtimes by room
     */
    public List<Showtime> getShowtimesByRoom(Long roomId) {
        if (roomId == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }
        return showtimeDao.findByRoomId(roomId);
    }
    
    /**
     * Get available showtimes (start time in the future)
     */
    public List<Showtime> getAvailableShowtimes() {
        return showtimeDao.findByStartTimeAfter(LocalDateTime.now());
    }
    
    /**
     * Get showtimes by movie and date range
     */
    public List<Showtime> getShowtimesByMovieAndDateRange(Long movieId, LocalDateTime startDate, LocalDateTime endDate) {
        if (movieId == null) {
            throw new IllegalArgumentException("Movie ID cannot be null");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        return showtimeDao.findByMovieAndDateRange(movieId, startDate, endDate);
    }
    
    /**
     * Get showtimes by price range
     */
    public List<Showtime> getShowtimesByPriceRange(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        return showtimeDao.findByPriceRange(minPrice, maxPrice);
    }
    
    /**
     * Get showtimes with available seats
     */
    public List<Showtime> getShowtimesWithAvailableSeats() {
        return showtimeDao.findAvailableShowtimes(LocalDateTime.now());
    }
    
    /**
     * Get today's showtimes
     */
    public List<Showtime> getTodayShowtimes() {
        return showtimeDao.findTodayShowtimes();
    }
    
    /**
     * Get showtimes by movie title
     */
    public List<Showtime> getShowtimesByMovieTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be empty");
        }
        return showtimeDao.findByMovieTitleContaining(title.trim());
    }
    
    /**
     * Count showtimes by movie
     */
    public long countShowtimesByMovie(Long movieId) {
        if (movieId == null) {
            throw new IllegalArgumentException("Movie ID cannot be null");
        }
        return showtimeDao.countByMovieId(movieId);
    }
    
    /**
     * Validate showtime data
     */
    private void validateShowtime(Showtime showtime) {
        if (showtime.getMovie() == null) {
            throw new IllegalArgumentException("Movie is required");
        }
        
        if (showtime.getRoom() == null) {
            throw new IllegalArgumentException("Room is required");
        }
        
        if (showtime.getStartTime() == null) {
            throw new IllegalArgumentException("Start time is required");
        }
        
        if (showtime.getEndTime() == null) {
            throw new IllegalArgumentException("End time is required");
        }
        
        if (showtime.getStartTime().isAfter(showtime.getEndTime())) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
        
        if (showtime.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        
        // Check if start time is in the future
        if (showtime.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Start time must be in the future");
        }
        
        // Verify movie and room exist
        if (!movieDao.getMovieById(showtime.getMovie().getId()).isPresent()) {
            throw new IllegalArgumentException("Movie not found");
        }
        
        if (!roomDao.getRoomById(showtime.getRoom().getId()).isPresent()) {
            throw new IllegalArgumentException("Room not found");
        }
    }
    
    /**
     * Get available seats for a showtime
     */
    public int getAvailableSeats(Long showtimeId) {
        Showtime showtime = showtimeDao.getShowtimeById(showtimeId)
                .orElseThrow(() -> new IllegalArgumentException("Showtime not found"));
        
        // This would require a more complex query to count available seats
        // For now, return a placeholder
        return 50; // Placeholder
    }
    
    /**
     * Check if showtime is fully booked
     */
    public boolean isShowtimeFullyBooked(Long showtimeId) {
        Showtime showtime = showtimeDao.getShowtimeById(showtimeId)
                .orElseThrow(() -> new IllegalArgumentException("Showtime not found"));
        
        // This would require a more complex query to check availability
        // For now, return false as placeholder
        return false; // Placeholder
    }
} 