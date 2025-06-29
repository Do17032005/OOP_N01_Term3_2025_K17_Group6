package com.example.servingwebcontent.database;

import com.example.servingwebcontent.model.Ticket;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TicketDAO {
    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "SELECT * FROM Ticket";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Ticket t = new Ticket(
                        rs.getString("id"),
                        rs.getString("showtimeId"),
                        rs.getString("seatId"),
                        rs.getString("customerId"),
                        rs.getDouble("price")
                    );
                    tickets.add(t);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
        return tickets;
    }

    public Ticket findTicketById(String id) {
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "SELECT * FROM Ticket WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Ticket(
                            rs.getString("id"),
                            rs.getString("showtimeId"),
                            rs.getString("seatId"),
                            rs.getString("customerId"),
                            rs.getDouble("price")
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

    public void insertTicket(Ticket t) {
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "INSERT INTO Ticket (id, showtimeId, seatId, customerId, price) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, t.getId());
                ps.setString(2, t.getShowtimeId());
                ps.setString(3, t.getSeatId());
                ps.setString(4, t.getCustomerId());
                ps.setDouble(5, t.getPrice());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thêm vé vào database: " + e.getMessage(), e);
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
    }

    public void updateTicket(Ticket t) {
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "UPDATE Ticket SET showtimeId=?, seatId=?, customerId=?, price=? WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, t.getShowtimeId());
                ps.setString(2, t.getSeatId());
                ps.setString(3, t.getCustomerId());
                ps.setDouble(4, t.getPrice());
                ps.setString(5, t.getId());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
    }

    public Ticket getTicketById(String id) {
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "SELECT * FROM Ticket WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Ticket(
                            rs.getString("id"),
                            rs.getString("showtimeId"),
                            rs.getString("seatId"),
                            rs.getString("customerId"),
                            rs.getDouble("price")
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

    public void deleteTicket(String id) {
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "DELETE FROM Ticket WHERE id=?";
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