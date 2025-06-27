package com.example.servingwebcontent.service;

import com.example.servingwebcontent.database.SeatDao;
import com.example.servingwebcontent.model.Seat;
import com.example.servingwebcontent.model.Seat.SeatType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SeatService {
    
    private final SeatDao seatDao;
    
    @Autowired
    public SeatService(SeatDao seatDao) {
        this.seatDao = seatDao;
    }
    
    /**
     * Get all seats
     */
    public List<Seat> getAllSeats() {
        return seatDao.getAllSeats();
    }
    
    /**
     * Get seat by ID
     */
    public Optional<Seat> getSeatById(Long id) {
        return seatDao.getSeatById(id);
    }
    
    /**
     * Create a new seat
     */
    public Seat createSeat(Seat seat) {
        validateSeat(seat);
        
        // Check if seat number already exists in the same room
        if (seatDao.findBySeatNumberAndRoomId(seat.getSeatNumber(), seat.getRoom().getId()).isPresent()) {
            throw new IllegalArgumentException("Seat number already exists in this room");
        }
        
        return seatDao.createSeat(seat);
    }
    
    /**
     * Update an existing seat
     */
    public Seat updateSeat(Long id, Seat seatDetails) {
        Seat seat = seatDao.getSeatById(id)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found with id: " + id));
        
        // Check if new seat number conflicts with existing seat in the same room
        Optional<Seat> existingSeat = seatDao.findBySeatNumberAndRoomId(
            seatDetails.getSeatNumber(), seatDetails.getRoom().getId());
        if (existingSeat.isPresent() && !existingSeat.get().getId().equals(id)) {
            throw new IllegalArgumentException("Seat number already exists in this room");
        }
        
        seat.setRoom(seatDetails.getRoom());
        seat.setSeatNumber(seatDetails.getSeatNumber());
        seat.setRowNumber(seatDetails.getRowNumber());
        seat.setColumnNumber(seatDetails.getColumnNumber());
        seat.setSeatType(seatDetails.getSeatType());
        
        seatDao.updateSeat(seat);
        return seat;
    }
    
    /**
     * Delete a seat
     */
    public void deleteSeat(Long id) {
        Seat seat = seatDao.getSeatById(id)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found with id: " + id));
        
        seatDao.deleteSeat(id);
    }
    
    /**
     * Get seats by room
     */
    public List<Seat> getSeatsByRoom(Long roomId) {
        if (roomId == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }
        return seatDao.findByRoomId(roomId);
    }
    
    /**
     * Get seats by type
     */
    public List<Seat> getSeatsByType(SeatType type) {
        if (type == null) {
            throw new IllegalArgumentException("Seat type cannot be null");
        }
        return seatDao.findBySeatType(type.name());
    }
    
    /**
     * Get seats by row
     */
    public List<Seat> getSeatsByRow(Long roomId, String rowNumber) {
        if (roomId == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }
        if (rowNumber == null || rowNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Row number cannot be empty");
        }
        return seatDao.findByRowNumberAndRoomId(rowNumber.trim(), roomId);
    }
    
    /**
     * Get available seats in a room
     */
    public List<Seat> getAvailableSeatsInRoom(Long roomId) {
        if (roomId == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }
        return seatDao.findAvailableSeatsByRoom(roomId);
    }
    
    /**
     * Get premium seats
     */
    public List<Seat> getPremiumSeats() {
        return seatDao.findBySeatType(SeatType.PREMIUM.name());
    }
    
    /**
     * Get standard seats
     */
    public List<Seat> getStandardSeats() {
        return seatDao.findBySeatType(SeatType.STANDARD.name());
    }
    
    /**
     * Get VIP seats
     */
    public List<Seat> getVipSeats() {
        return seatDao.findBySeatType(SeatType.VIP.name());
    }
    
    /**
     * Get seats by price range
     */
    public List<Seat> getSeatsByPriceRange(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        // Note: Seat model doesn't have price field, so this method is not applicable
        throw new UnsupportedOperationException("Seat model doesn't have price field");
    }
    
    /**
     * Get seats by room and type
     */
    public List<Seat> getSeatsByRoomAndType(Long roomId, SeatType type) {
        if (roomId == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Seat type cannot be null");
        }
        return seatDao.findByRoomIdAndSeatType(roomId, type.name());
    }
    
    /**
     * Count seats by room
     */
    public long countSeatsByRoom(Long roomId) {
        if (roomId == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }
        return seatDao.findByRoomId(roomId).size();
    }
    
    /**
     * Count seats by type
     */
    public long countSeatsByType(SeatType type) {
        if (type == null) {
            throw new IllegalArgumentException("Seat type cannot be null");
        }
        return seatDao.findBySeatType(type.name()).size();
    }
    
    /**
     * Validate seat data
     */
    private void validateSeat(Seat seat) {
        if (seat.getRoom() == null) {
            throw new IllegalArgumentException("Room is required");
        }
        
        if (seat.getSeatNumber() == null || seat.getSeatNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Seat number is required");
        }
        
        if (seat.getRowNumber() == null || seat.getRowNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Row number is required");
        }
        
        if (seat.getColumnNumber() <= 0) {
            throw new IllegalArgumentException("Column number must be positive");
        }
        
        if (seat.getSeatType() == null) {
            throw new IllegalArgumentException("Seat type is required");
        }
    }
} 