package com.example.servingwebcontent.database;

import com.example.servingwebcontent.model.Movie;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MovieDao {
    
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movie";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                movies.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public Optional<Movie> getMovieById(Long id) {
        String sql = "SELECT * FROM movie WHERE id = ?";
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

    public Movie createMovie(Movie movie) {
        String sql = "INSERT INTO movie (title, genre, duration, release_date, age_rating, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getGenre());
            ps.setInt(3, movie.getDuration());
            ps.setDate(4, Date.valueOf(movie.getReleaseDate()));
            ps.setInt(5, movie.getAgeRating());
            ps.setString(6, movie.getDescription());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    movie.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movie;
    }

    public boolean updateMovie(Movie movie) {
        String sql = "UPDATE movie SET title=?, genre=?, duration=?, release_date=?, age_rating=?, description=? WHERE id=?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getGenre());
            ps.setInt(3, movie.getDuration());
            ps.setDate(4, Date.valueOf(movie.getReleaseDate()));
            ps.setInt(5, movie.getAgeRating());
            ps.setString(6, movie.getDescription());
            ps.setLong(7, movie.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMovie(Long id) {
        String sql = "DELETE FROM movie WHERE id=?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<Movie> findByTitleIgnoreCase(String title) {
        String sql = "SELECT * FROM movie WHERE LOWER(title) = LOWER(?)";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
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

    public List<Movie> findByGenre(String genre) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movie WHERE LOWER(genre) = LOWER(?)";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, genre);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movies.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public List<Movie> findByReleaseDateAfter(LocalDate date) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movie WHERE release_date > ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movies.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public List<Movie> findByAgeRating(int ageRating) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movie WHERE age_rating <= ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ageRating);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movies.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public List<Movie> findByDurationRange(int minDuration, int maxDuration) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movie WHERE duration BETWEEN ? AND ?";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, minDuration);
            ps.setInt(2, maxDuration);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movies.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public List<Movie> findByTitleContaining(String keyword) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movie WHERE LOWER(title) LIKE LOWER(?)";
        try (Connection conn = Aivenconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movies.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public List<Movie> findLongMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movie WHERE duration > 150";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                movies.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public List<Movie> findMoviesWithAvailableShowtimes() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT DISTINCT m.* FROM movie m JOIN showtime s ON m.id = s.movie_id WHERE s.start_time > NOW()";
        try (Connection conn = Aivenconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                movies.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    private Movie mapRow(ResultSet rs) throws SQLException {
        Movie movie = new Movie();
        movie.setId(rs.getLong("id"));
        movie.setTitle(rs.getString("title"));
        movie.setGenre(rs.getString("genre"));
        movie.setDuration(rs.getInt("duration"));
        movie.setReleaseDate(rs.getDate("release_date").toLocalDate());
        movie.setAgeRating(rs.getInt("age_rating"));
        movie.setDescription(rs.getString("description"));
        return movie;
    }
} 