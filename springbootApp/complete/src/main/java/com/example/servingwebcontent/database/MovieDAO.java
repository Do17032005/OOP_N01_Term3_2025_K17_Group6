package com.example.servingwebcontent.database;

import com.example.servingwebcontent.model.Movie;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MovieDAO {
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "SELECT * FROM Movie";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Movie m = new Movie(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("title"),
                        rs.getString("showTime"),
                        rs.getString("dateTime"),
                        rs.getInt("duration"),
                        rs.getString("genre"),
                        rs.getInt("age")
                    );
                    movies.add(m);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
        return movies;
    }

    public void insertMovie(Movie m) {
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "INSERT INTO Movie (id, name, title, showTime, dateTime, duration, genre, age) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, m.getId());
                ps.setString(2, m.getName());
                ps.setString(3, m.getTitle());
                ps.setString(4, m.getShowTime());
                ps.setString(5, m.getDateTime());
                ps.setInt(6, m.getDuration());
                ps.setString(7, m.getGenre());
                ps.setInt(8, m.getAge());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thêm phim vào database: " + e.getMessage(), e);
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
    }

    public Movie getMovieById(String id) {
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "SELECT * FROM Movie WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Movie(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("title"),
                            rs.getString("showTime"),
                            rs.getString("dateTime"),
                            rs.getInt("duration"),
                            rs.getString("genre"),
                            rs.getInt("age")
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

    public void updateMovie(Movie m) {
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "UPDATE Movie SET name=?, title=?, showTime=?, dateTime=?, duration=?, genre=?, age=? WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, m.getName());
                ps.setString(2, m.getTitle());
                ps.setString(3, m.getShowTime());
                ps.setString(4, m.getDateTime());
                ps.setInt(5, m.getDuration());
                ps.setString(6, m.getGenre());
                ps.setInt(7, m.getAge());
                ps.setString(8, m.getId());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
    }

    public void deleteMovie(String id) {
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "DELETE FROM Movie WHERE id=?";
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