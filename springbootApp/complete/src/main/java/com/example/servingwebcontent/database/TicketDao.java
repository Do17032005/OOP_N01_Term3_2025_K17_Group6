package com.example.servingwebcontent.database;

import com.example.servingwebcontent.model.Ticket;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TicketDao {
    
    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tickets.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public Optional<Ticket> getTicketById(Long id) {
        String sql = "SELECT * FROM ticket WHERE id = ?";
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

    public Ticket createTicket(Ticket ticket) {
        String sql = "INSERT INTO ticket (ticket_number, user_id, showtime_id, seat_id, price, status, booking_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ticket.getTicketNumber());
            ps.setLong(2, ticket.getUser().getId());
            ps.setLong(3, ticket.getShowtime().getId());
            ps.setLong(4, ticket.getSeat().getId());
            ps.setDouble(5, ticket.getPrice());
            ps.setString(6, ticket.getStatus().name());
            ps.setTimestamp(7, Timestamp.valueOf(ticket.getBookingDate()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    ticket.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ticket;
    }

    public boolean updateTicket(Ticket ticket) {
        String sql = "UPDATE ticket SET ticket_number=?, user_id=?, showtime_id=?, seat_id=?, price=?, status=?, booking_date=? WHERE id=?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticket.getTicketNumber());
            ps.setLong(2, ticket.getUser().getId());
            ps.setLong(3, ticket.getShowtime().getId());
            ps.setLong(4, ticket.getSeat().getId());
            ps.setDouble(5, ticket.getPrice());
            ps.setString(6, ticket.getStatus().name());
            ps.setTimestamp(7, Timestamp.valueOf(ticket.getBookingDate()));
            ps.setLong(8, ticket.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteTicket(Long id) {
        String sql = "DELETE FROM ticket WHERE id=?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<Ticket> findByTicketNumber(String ticketNumber) {
        String sql = "SELECT * FROM ticket WHERE ticket_number = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticketNumber);
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

    public List<Ticket> findByTicketNumberContaining(String keyword) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE LOWER(ticket_number) LIKE LOWER(?)";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public List<Ticket> findByUserId(Long userId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE user_id = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public List<Ticket> findByShowtimeId(Long showtimeId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE showtime_id = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, showtimeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public List<Ticket> findBySeatId(Long seatId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE seat_id = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, seatId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public List<Ticket> findByStatus(String status) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE status = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public List<Ticket> findByBookingDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE booking_date BETWEEN ? AND ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public List<Ticket> findByPriceRange(double minPrice, double maxPrice) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE price BETWEEN ? AND ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, minPrice);
            ps.setDouble(2, maxPrice);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public List<Ticket> findConfirmedTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE status = 'CONFIRMED'";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tickets.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public List<Ticket> findCancelledTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE status = 'CANCELLED'";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tickets.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public List<Ticket> findTodayTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE DATE(booking_date) = CURRENT_DATE";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tickets.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public List<Ticket> findByUserIdAndStatus(Long userId, String status) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE user_id = ? AND status = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setString(2, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public List<Ticket> findByShowtimeIdAndStatus(Long showtimeId, String status) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE showtime_id = ? AND status = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, showtimeId);
            ps.setString(2, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public Double calculateTotalRevenue() {
        String sql = "SELECT SUM(price) FROM ticket WHERE status = 'CONFIRMED'";
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

    public Double calculateRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT SUM(price) FROM ticket WHERE status = 'CONFIRMED' AND booking_date BETWEEN ? AND ?";
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

    private Ticket mapRow(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getLong("id"));
        ticket.setTicketNumber(rs.getString("ticket_number"));
        // Note: User, Showtime, Seat objects would need to be loaded separately
        // ticket.setUser(userDao.getUserById(rs.getLong("user_id")));
        // ticket.setShowtime(showtimeDao.getShowtimeById(rs.getLong("showtime_id")));
        // ticket.setSeat(seatDao.getSeatById(rs.getLong("seat_id")));
        ticket.setPrice(rs.getDouble("price"));
        ticket.setStatus(Ticket.TicketStatus.valueOf(rs.getString("status")));
        ticket.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
        return ticket;
    }
} 