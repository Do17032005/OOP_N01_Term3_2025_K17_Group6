package com.example.servingwebcontent.database;

import com.example.servingwebcontent.model.Showtime;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShowtimeDAO {
    public List<Showtime> getAllShowtimes() {
        List<Showtime> showtimes = new ArrayList<>();
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://avnadmin:AVNS_OY6UdTSUCEJY08Wic_V@mysql-1bf49a9c-nghiengame005.c.aivencloud.com:27021/defaultdb?ssl-mode=REQUIRED",
                "avnadmin",
                "AVNS_OY6UdTSUCEJY08Wic_V"
            );
            String sql = "SELECT * FROM Showtime";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Showtime st = new Showtime(
                        rs.getString("id"),
                        rs.getString("movieId"),
                        rs.getString("roomId"),
                        rs.getTimestamp("startTime").toLocalDateTime()
                    );
                    showtimes.add(st);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
        return showtimes;
    }

    public void insertShowtime(Showtime st) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://avnadmin:AVNS_OY6UdTSUCEJY08Wic_V@mysql-1bf49a9c-nghiengame005.c.aivencloud.com:27021/defaultdb?ssl-mode=REQUIRED",
                "avnadmin",
                "AVNS_OY6UdTSUCEJY08Wic_V"
            );
            String sql = "INSERT INTO Showtime (id, movieId, roomId, startTime) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, st.getId());
                ps.setString(2, st.getMovieId());
                ps.setString(3, st.getRoomId());
                ps.setTimestamp(4, Timestamp.valueOf(st.getStartTime()));
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
    }

    public void updateShowtime(Showtime st) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://avnadmin:AVNS_OY6UdTSUCEJY08Wic_V@mysql-1bf49a9c-nghiengame005.c.aivencloud.com:27021/defaultdb?ssl-mode=REQUIRED",
                "avnadmin",
                "AVNS_OY6UdTSUCEJY08Wic_V"
            );
            String sql = "UPDATE Showtime SET movieId=?, roomId=?, startTime=? WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, st.getMovieId());
                ps.setString(2, st.getRoomId());
                ps.setTimestamp(3, Timestamp.valueOf(st.getStartTime()));
                ps.setString(4, st.getId());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
    }

    public void deleteShowtime(String id) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://avnadmin:AVNS_OY6UdTSUCEJY08Wic_V@mysql-1bf49a9c-nghiengame005.c.aivencloud.com:27021/defaultdb?ssl-mode=REQUIRED",
                "avnadmin",
                "AVNS_OY6UdTSUCEJY08Wic_V"
            );
            String sql = "DELETE FROM Showtime WHERE id=?";
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