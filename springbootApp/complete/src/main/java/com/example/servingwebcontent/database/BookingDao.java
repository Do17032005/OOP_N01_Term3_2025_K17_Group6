package com.example.servingwebcontent.database;

import com.example.servingwebcontent.model.Booking;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class BookingDao {
    
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bookings.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public Optional<Booking> getBookingById(Long id) {
        String sql = "SELECT * FROM booking WHERE id = ?";
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

    public Booking createBooking(Booking booking) {
        String sql = "INSERT INTO booking (booking_number, user_id, showtime_id, booking_date, total_amount, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, booking.getBookingNumber());
            ps.setLong(2, booking.getUser().getId());
            ps.setLong(3, booking.getShowtime().getId());
            ps.setTimestamp(4, Timestamp.valueOf(booking.getBookingDate()));
            ps.setDouble(5, booking.getTotalAmount());
            ps.setString(6, booking.getStatus().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    booking.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booking;
    }

    public boolean updateBooking(Booking booking) {
        String sql = "UPDATE booking SET booking_number=?, user_id=?, showtime_id=?, booking_date=?, total_amount=?, status=? WHERE id=?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, booking.getBookingNumber());
            ps.setLong(2, booking.getUser().getId());
            ps.setLong(3, booking.getShowtime().getId());
            ps.setTimestamp(4, Timestamp.valueOf(booking.getBookingDate()));
            ps.setDouble(5, booking.getTotalAmount());
            ps.setString(6, booking.getStatus().name());
            ps.setLong(7, booking.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteBooking(Long id) {
        String sql = "DELETE FROM booking WHERE id=?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Booking> findByUserId(Long userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE user_id = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Booking> findByShowtimeId(Long showtimeId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE showtime_id = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, showtimeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Booking> findByStatus(String status) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE status = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public Optional<Booking> findByBookingNumber(String bookingNumber) {
        String sql = "SELECT * FROM booking WHERE booking_number = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bookingNumber);
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

    public List<Booking> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE booking_date BETWEEN ? AND ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Booking> findByAmountRange(double minAmount, double maxAmount) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE total_amount BETWEEN ? AND ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, minAmount);
            ps.setDouble(2, maxAmount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Booking> findConfirmedBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE status = 'CONFIRMED'";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bookings.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Booking> findTodayBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE DATE(booking_date) = CURRENT_DATE";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bookings.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public Double calculateTotalRevenue() {
        String sql = "SELECT SUM(total_amount) FROM booking WHERE status = 'CONFIRMED'";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public List<Booking> findCancelledBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE status = 'CANCELLED'";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bookings.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public Double calculateRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT SUM(total_amount) FROM booking WHERE status = 'CONFIRMED' AND booking_date BETWEEN ? AND ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public List<Booking> findHighValueBookings(double threshold) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE total_amount >= ? ORDER BY total_amount DESC";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Booking> findRecentBookings(LocalDateTime since) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE booking_date >= ? ORDER BY booking_date DESC";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(since));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    private Booking mapRow(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getLong("id"));
        booking.setBookingNumber(rs.getString("booking_number"));
        // Note: User and Showtime objects would need to be loaded separately
        // booking.setUser(userDao.getUserById(rs.getLong("user_id")));
        // booking.setShowtime(showtimeDao.getShowtimeById(rs.getLong("showtime_id")));
        booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
        booking.setTotalAmount(rs.getDouble("total_amount"));
        booking.setStatus(Booking.BookingStatus.valueOf(rs.getString("status")));
        return booking;
    }
} 