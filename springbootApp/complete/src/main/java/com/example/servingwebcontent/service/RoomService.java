package com.example.servingwebcontent.service;

import com.example.servingwebcontent.database.RoomDao;
import com.example.servingwebcontent.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomService {
    
    private final RoomDao roomDao;
    
    @Autowired
    public RoomService(RoomDao roomDao) {
        this.roomDao = roomDao;
    }
    
    /**
     * Get all rooms
     */
    public List<Room> getAllRooms() {
        return roomDao.getAllRooms();
    }
    
    /**
     * Get room by ID
     */
    public Optional<Room> getRoomById(Long id) {
        return roomDao.getRoomById(id);
    }
    
    /**
     * Create a new room
     */
    public Room createRoom(Room room) {
        validateRoom(room);
        
        // Check if room name already exists
        if (roomDao.findByName(room.getName()).isPresent()) {
            throw new IllegalArgumentException("Room name already exists");
        }
        
        return roomDao.createRoom(room);
    }
    
    /**
     * Update an existing room
     */
    public Room updateRoom(Long id, Room roomDetails) {
        Room room = roomDao.getRoomById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + id));
        
        // Check if new name conflicts with existing room
        Optional<Room> existingRoom = roomDao.findByName(roomDetails.getName());
        if (existingRoom.isPresent() && !existingRoom.get().getId().equals(id)) {
            throw new IllegalArgumentException("Room name already exists");
        }
        
        room.setName(roomDetails.getName());
        room.setTotalSeats(roomDetails.getTotalSeats());
        room.setCapacity(roomDetails.getCapacity());
        
        roomDao.updateRoom(room);
        return room;
    }
    
    /**
     * Delete a room
     */
    public void deleteRoom(Long id) {
        Room room = roomDao.getRoomById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + id));
        
        // Check if room has showtimes
        // Note: This would require a join query or separate check
        // For now, we'll just delete the room
        
        roomDao.deleteRoom(id);
    }
    
    /**
     * Get room by name
     */
    public Optional<Room> getRoomByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Room name cannot be empty");
        }
        return roomDao.findByName(name.trim());
    }
    
    /**
     * Get rooms by capacity range
     */
    public List<Room> getRoomsByCapacityRange(int minCapacity, int maxCapacity) {
        if (minCapacity < 0 || maxCapacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        if (minCapacity > maxCapacity) {
            throw new IllegalArgumentException("Minimum capacity cannot be greater than maximum capacity");
        }
        return roomDao.findByCapacityRange(minCapacity, maxCapacity);
    }
    
    /**
     * Get large rooms (capacity > 100)
     */
    public List<Room> getLargeRooms() {
        return roomDao.findLargeRooms(100);
    }
    
    /**
     * Get small rooms (capacity < 50)
     */
    public List<Room> getSmallRooms() {
        return roomDao.findByCapacityRange(1, 49);
    }
    
    /**
     * Search rooms by name
     */
    public List<Room> searchRoomsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllRooms();
        }
        return roomDao.findByNameContaining(name.trim());
    }
    
    /**
     * Get available rooms (no showtimes scheduled)
     */
    public List<Room> getAvailableRooms() {
        return roomDao.findRoomsWithoutShowtimes();
    }
    
    /**
     * Get rooms with showtimes
     */
    public List<Room> getRoomsWithShowtimes() {
        return roomDao.findRoomsWithShowtimes();
    }
    
    /**
     * Validate room data
     */
    private void validateRoom(Room room) {
        if (room.getName() == null || room.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Room name is required");
        }
        
        if (room.getTotalSeats() <= 0) {
            throw new IllegalArgumentException("Total seats must be positive");
        }
        
        if (room.getCapacity() <= 0) {
            throw new IllegalArgumentException("Room capacity must be positive");
        }
        
        if (room.getCapacity() > room.getTotalSeats()) {
            throw new IllegalArgumentException("Capacity cannot be greater than total seats");
        }
    }
} 