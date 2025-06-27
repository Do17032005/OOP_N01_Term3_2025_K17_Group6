package com.example.servingwebcontent.database;

import com.example.servingwebcontent.model.Seat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SeatDao {
    
    public List<Seat> getAllSeats() {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seat";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                seats.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public Optional<Seat> getSeatById(Long id) {
        String sql = "SELECT * FROM seat WHERE id = ?";
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

    public Seat createSeat(Seat seat) {
        String sql = "INSERT INTO seat (seat_number, row_number, column_number, seat_type, room_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, seat.getSeatNumber());
            ps.setString(2, seat.getRowNumber());
            ps.setInt(3, seat.getColumnNumber());
            ps.setString(4, seat.getSeatType().name());
            ps.setLong(5, seat.getRoom().getId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    seat.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seat;
    }

    public boolean updateSeat(Seat seat) {
        String sql = "UPDATE seat SET seat_number=?, row_number=?, column_number=?, seat_type=?, room_id=? WHERE id=?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, seat.getSeatNumber());
            ps.setString(2, seat.getRowNumber());
            ps.setInt(3, seat.getColumnNumber());
            ps.setString(4, seat.getSeatType().name());
            ps.setLong(5, seat.getRoom().getId());
            ps.setLong(6, seat.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteSeat(Long id) {
        String sql = "DELETE FROM seat WHERE id=?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<Seat> findBySeatNumberAndRoomId(String seatNumber, Long roomId) {
        String sql = "SELECT * FROM seat WHERE seat_number = ? AND room_id = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, seatNumber);
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

    public List<Seat> findBySeatNumberContaining(String keyword) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seat WHERE LOWER(seat_number) LIKE LOWER(?)";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public List<Seat> findBySeatType(String seatType) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seat WHERE seat_type = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, seatType);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public List<Seat> findByRoomId(Long roomId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seat WHERE room_id = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public List<Seat> findByRoomIdAndSeatType(Long roomId, String seatType) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seat WHERE room_id = ? AND seat_type = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, roomId);
            ps.setString(2, seatType);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public List<Seat> findAvailableSeatsByRoom(Long roomId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT s.* FROM seat s LEFT JOIN ticket t ON s.id = t.seat_id WHERE s.room_id = ? AND t.id IS NULL";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public List<Seat> findOccupiedSeatsByRoom(Long roomId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT s.* FROM seat s JOIN ticket t ON s.id = t.seat_id WHERE s.room_id = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public List<Seat> findByRowNumber(String rowNumber) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seat WHERE row_number = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rowNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public List<Seat> findByRowNumberAndRoomId(String rowNumber, Long roomId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seat WHERE row_number = ? AND room_id = ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rowNumber);
            ps.setLong(2, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public List<Seat> findVIPSeats() {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seat WHERE seat_type = 'VIP'";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                seats.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public List<Seat> findPremiumSeats() {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seat WHERE seat_type = 'PREMIUM'";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                seats.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public List<Seat> findStandardSeats() {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seat WHERE seat_type = 'STANDARD'";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                seats.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    private Seat mapRow(ResultSet rs) throws SQLException {
        Seat seat = new Seat();
        seat.setId(rs.getLong("id"));
        seat.setSeatNumber(rs.getString("seat_number"));
        seat.setRowNumber(rs.getString("row_number"));
        seat.setColumnNumber(rs.getInt("column_number"));
        seat.setSeatType(Seat.SeatType.valueOf(rs.getString("seat_type")));
        // Note: Room object would need to be loaded separately
        // seat.setRoom(roomDao.getRoomById(rs.getLong("room_id")));
        return seat;
    }
} 