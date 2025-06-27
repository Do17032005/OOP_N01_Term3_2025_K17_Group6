package com.example.servingwebcontent.database;

import com.example.servingwebcontent.model.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {
    
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM room";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public Optional<Room> getRoomById(Long id) {
        String sql = "SELECT * FROM room WHERE id = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Room createRoom(Room room) {
        String sql = "INSERT INTO room (name, total_seats, capacity) VALUES (?, ?, ?)";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, room.getName());
            ps.setInt(2, room.getTotalSeats());
            ps.setInt(3, room.getCapacity());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    room.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return room;
    }

    public boolean updateRoom(Room room) {
        String sql = "UPDATE room SET name=?, total_seats=?, capacity=? WHERE id=?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room.getName());
            ps.setInt(2, room.getTotalSeats());
            ps.setInt(3, room.getCapacity());
            ps.setLong(4, room.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteRoom(Long id) {
        String sql = "DELETE FROM room WHERE id=?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<Room> findByName(String name) {
        String sql = "SELECT * FROM room WHERE name = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM room WHERE name = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Room> findByNameContaining(String keyword) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM room WHERE LOWER(name) LIKE LOWER(?)";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public List<Room> findByCapacityRange(int minCapacity, int maxCapacity) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM room WHERE capacity BETWEEN ? AND ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, minCapacity);
            ps.setInt(2, maxCapacity);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public List<Room> findByTotalSeats(int totalSeats) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM room WHERE total_seats = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, totalSeats);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public List<Room> findRoomsWithAvailableCapacity() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM room WHERE capacity < total_seats";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public List<Room> findRoomsWithShowtimes() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT DISTINCT r.* FROM room r JOIN showtime s ON r.id = s.room_id";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public List<Room> findRoomsWithoutShowtimes() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.* FROM room r LEFT JOIN showtime s ON r.id = s.room_id WHERE s.id IS NULL";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public List<Room> findLargeRooms(int threshold) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM room WHERE capacity > ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public List<Room> findByCapacity(int capacity) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM room WHERE capacity = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, capacity);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    private Room mapRow(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setId(rs.getLong("id"));
        room.setName(rs.getString("name"));
        room.setTotalSeats(rs.getInt("total_seats"));
        room.setCapacity(rs.getInt("capacity"));
        return room;
    }
} 