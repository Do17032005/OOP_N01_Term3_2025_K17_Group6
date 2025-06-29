package com.example.servingwebcontent.database;

import com.example.servingwebcontent.model.Seat;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SeatDAO {
    public List<Seat> getAllSeats() {
        List<Seat> seats = new ArrayList<>();
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "SELECT * FROM Seat";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Seat seat = new Seat(
                        rs.getString("id"),
                        rs.getString("roomId"),
                        rs.getString("seatNumber")
                    );
                    seats.add(seat);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
        return seats;
    }

    public void insertSeat(Seat seat) {
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "INSERT INTO Seat (id, roomId, seatNumber) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, seat.getId());
                ps.setString(2, seat.getRoomId());
                ps.setString(3, seat.getSeatNumber());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thêm ghế vào database: " + e.getMessage(), e);
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
    }

    public Seat getSeatById(String id) {
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "SELECT * FROM Seat WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Seat(
                            rs.getString("id"),
                            rs.getString("roomId"),
                            rs.getString("seatNumber")
                        );
                    }
                }
            }
        } catch (Exception e) { 
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
        return null;
    }
    
    public void updateSeat(Seat seat) {
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "UPDATE Seat SET roomId=?, seatNumber=? WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, seat.getRoomId());
                ps.setString(2, seat.getSeatNumber());
                ps.setString(3, seat.getId());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
    }

    public void deleteSeat(String id) {
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "DELETE FROM Seat WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, id);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
    }
} 