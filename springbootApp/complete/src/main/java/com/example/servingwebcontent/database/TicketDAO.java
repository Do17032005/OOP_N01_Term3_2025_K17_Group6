package com.example.servingwebcontent.database;

import com.example.servingwebcontent.model.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://avnadmin:AVNS_OY6UdTSUCEJY08Wic_V@mysql-1bf49a9c-nghiengame005.c.aivencloud.com:27021/defaultdb?ssl-mode=REQUIRED",
                "avnadmin",
                "AVNS_OY6UdTSUCEJY08Wic_V"
            );
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
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://avnadmin:AVNS_OY6UdTSUCEJY08Wic_V@mysql-1bf49a9c-nghiengame005.c.aivencloud.com:27021/defaultdb?ssl-mode=REQUIRED",
                "avnadmin",
                "AVNS_OY6UdTSUCEJY08Wic_V"
            );
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
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://avnadmin:AVNS_OY6UdTSUCEJY08Wic_V@mysql-1bf49a9c-nghiengame005.c.aivencloud.com:27021/defaultdb?ssl-mode=REQUIRED",
                "avnadmin",
                "AVNS_OY6UdTSUCEJY08Wic_V"
            );
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
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
    }

    public void updateTicket(Ticket t) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://avnadmin:AVNS_OY6UdTSUCEJY08Wic_V@mysql-1bf49a9c-nghiengame005.c.aivencloud.com:27021/defaultdb?ssl-mode=REQUIRED",
                "avnadmin",
                "AVNS_OY6UdTSUCEJY08Wic_V"
            );
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

    public void deleteTicket(String id) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://avnadmin:AVNS_OY6UdTSUCEJY08Wic_V@mysql-1bf49a9c-nghiengame005.c.aivencloud.com:27021/defaultdb?ssl-mode=REQUIRED",
                "avnadmin",
                "AVNS_OY6UdTSUCEJY08Wic_V"
            );
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