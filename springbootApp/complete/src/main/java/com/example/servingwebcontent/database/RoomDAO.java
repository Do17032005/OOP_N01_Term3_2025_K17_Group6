package com.example.servingwebcontent.database;

import com.example.servingwebcontent.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://avnadmin:AVNS_OY6UdTSUCEJY08Wic_V@mysql-1bf49a9c-nghiengame005.c.aivencloud.com:27021/defaultdb?ssl-mode=REQUIRED",
                "avnadmin",
                "AVNS_OY6UdTSUCEJY08Wic_V"
            );
            String sql = "SELECT * FROM Room";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Room room = new Room(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("totalSeats")
                    );
                    rooms.add(room);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
        return rooms;
    }

    public void insertRoom(Room room) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://avnadmin:AVNS_OY6UdTSUCEJY08Wic_V@mysql-1bf49a9c-nghiengame005.c.aivencloud.com:27021/defaultdb?ssl-mode=REQUIRED",
                "avnadmin",
                "AVNS_OY6UdTSUCEJY08Wic_V"
            );
            String sql = "INSERT INTO Room (id, name, totalSeats) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, room.getId());
                ps.setString(2, room.getName());
                ps.setInt(3, room.getTotalSeats());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
    }

    public void updateRoom(Room room) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://avnadmin:AVNS_OY6UdTSUCEJY08Wic_V@mysql-1bf49a9c-nghiengame005.c.aivencloud.com:27021/defaultdb?ssl-mode=REQUIRED",
                "avnadmin",
                "AVNS_OY6UdTSUCEJY08Wic_V"
            );
            String sql = "UPDATE Room SET name=?, totalSeats=? WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, room.getName());
                ps.setInt(2, room.getTotalSeats());
                ps.setString(3, room.getId());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
    }

    public void deleteRoom(String id) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://avnadmin:AVNS_OY6UdTSUCEJY08Wic_V@mysql-1bf49a9c-nghiengame005.c.aivencloud.com:27021/defaultdb?ssl-mode=REQUIRED",
                "avnadmin",
                "AVNS_OY6UdTSUCEJY08Wic_V"
            );
            String sql = "DELETE FROM Room WHERE id=?";
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