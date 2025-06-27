package com.example.servingwebcontent.database;

import com.example.servingwebcontent.model.Showtime;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ShowtimeDao {
    
    public List<Showtime> getAllShowtimes() {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT * FROM showtime";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                showtimes.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    public Optional<Showtime> getShowtimeById(Long id) {
        String sql = "SELECT * FROM showtime WHERE id = ?";
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

    public Showtime createShowtime(Showtime showtime) {
        String sql = "INSERT INTO showtime (movie_id, room_id, start_time, end_time, price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, showtime.getMovie().getId());
            ps.setLong(2, showtime.getRoom().getId());
            ps.setTimestamp(3, Timestamp.valueOf(showtime.getStartTime()));
            ps.setTimestamp(4, Timestamp.valueOf(showtime.getEndTime()));
            ps.setDouble(5, showtime.getPrice());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    showtime.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtime;
    }

    public boolean updateShowtime(Showtime showtime) {
        String sql = "UPDATE showtime SET movie_id=?, room_id=?, start_time=?, end_time=?, price=? WHERE id=?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, showtime.getMovie().getId());
            ps.setLong(2, showtime.getRoom().getId());
            ps.setTimestamp(3, Timestamp.valueOf(showtime.getStartTime()));
            ps.setTimestamp(4, Timestamp.valueOf(showtime.getEndTime()));
            ps.setDouble(5, showtime.getPrice());
            ps.setLong(6, showtime.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteShowtime(Long id) {
        String sql = "DELETE FROM showtime WHERE id=?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Showtime> findByMovieId(Long movieId) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT * FROM showtime WHERE movie_id = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    showtimes.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    public List<Showtime> findByRoomId(Long roomId) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT * FROM showtime WHERE room_id = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    showtimes.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    public List<Showtime> findByStartTimeAfter(LocalDateTime now) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT * FROM showtime WHERE start_time > ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(now));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    showtimes.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    public List<Showtime> findByMovieAndDateRange(Long movieId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT * FROM showtime WHERE movie_id = ? AND start_time BETWEEN ? AND ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, movieId);
            ps.setTimestamp(2, Timestamp.valueOf(startDate));
            ps.setTimestamp(3, Timestamp.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    showtimes.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    public List<Showtime> findByPriceRange(double minPrice, double maxPrice) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT * FROM showtime WHERE price BETWEEN ? AND ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, minPrice);
            ps.setDouble(2, maxPrice);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    showtimes.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    public List<Showtime> findAvailableShowtimes(LocalDateTime now) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT s.* FROM showtime s WHERE s.start_time > ? AND (SELECT COUNT(*) FROM ticket t WHERE t.showtime_id = s.id) < (SELECT capacity FROM room WHERE id = s.room_id)";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(now));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    showtimes.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    public Optional<Showtime> findByMovieIdAndRoomId(Long movieId, Long roomId) {
        String sql = "SELECT * FROM showtime WHERE movie_id = ? AND room_id = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, movieId);
            ps.setLong(2, roomId);
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

    public List<Showtime> findTodayShowtimes() {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT * FROM showtime WHERE DATE(start_time) = CURRENT_DATE";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                showtimes.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    public List<Showtime> findByMovieTitleContaining(String title) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT s.* FROM showtime s JOIN movie m ON s.movie_id = m.id WHERE LOWER(m.title) LIKE LOWER(?)";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + title + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    showtimes.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    public long countByMovieId(Long movieId) {
        String sql = "SELECT COUNT(*) FROM showtime WHERE movie_id = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Showtime mapRow(ResultSet rs) throws SQLException {
        Showtime showtime = new Showtime();
        showtime.setId(rs.getLong("id"));
        // Note: Movie and Room objects would need to be loaded separately
        // showtime.setMovie(movieDao.getMovieById(rs.getLong("movie_id")));
        // showtime.setRoom(roomDao.getRoomById(rs.getLong("room_id")));
        showtime.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
        showtime.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
        showtime.setPrice(rs.getDouble("price"));
        return showtime;
    }
} 