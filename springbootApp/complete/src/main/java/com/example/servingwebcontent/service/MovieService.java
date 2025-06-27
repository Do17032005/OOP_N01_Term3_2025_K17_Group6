package com.example.servingwebcontent.service;

import com.example.servingwebcontent.database.MovieDao;
import com.example.servingwebcontent.model.Movie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieService {
    
    private final MovieDao movieDao;
    
    public MovieService(MovieDao movieDao) {
        this.movieDao = movieDao;
    }
    
    /**
     * Get all movies
     */
    public List<Movie> getAllMovies() {
        return movieDao.getAllMovies();
    }
    
    /**
     * Get movie by ID
     */
    public Optional<Movie> getMovieById(Long id) {
        return movieDao.getMovieById(id);
    }
    
    /**
     * Create a new movie
     */
    public Movie createMovie(Movie movie) {
        validateMovie(movie);
        
        // Check if movie title already exists
        if (movieDao.findByTitleIgnoreCase(movie.getTitle()).isPresent()) {
            throw new IllegalArgumentException("Movie title already exists");
        }
        
        return movieDao.createMovie(movie);
    }
    
    /**
     * Update an existing movie
     */
    public Movie updateMovie(Long id, Movie movieDetails) {
        Movie movie = movieDao.getMovieById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + id));
        
        // Check if new title conflicts with existing movie
        Optional<Movie> existingMovie = movieDao.findByTitleIgnoreCase(movieDetails.getTitle());
        if (existingMovie.isPresent() && !existingMovie.get().getId().equals(id)) {
            throw new IllegalArgumentException("Movie title already exists");
        }
        
        movie.setTitle(movieDetails.getTitle());
        movie.setGenre(movieDetails.getGenre());
        movie.setDuration(movieDetails.getDuration());
        movie.setReleaseDate(movieDetails.getReleaseDate());
        movie.setAgeRating(movieDetails.getAgeRating());
        movie.setDescription(movieDetails.getDescription());
        
        movieDao.updateMovie(movie);
        return movie;
    }
    
    /**
     * Delete a movie
     */
    public void deleteMovie(Long id) {
        Movie movie = movieDao.getMovieById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + id));
        
        // Check if movie has showtimes
        // Note: This would require a join query or separate check
        // For now, we'll just delete the movie
        
        movieDao.deleteMovie(id);
    }
    
    /**
     * Search movies by title
     */
    public List<Movie> searchMoviesByTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllMovies();
        }
        return movieDao.findByTitleContaining(keyword.trim());
    }
    
    /**
     * Get movies by genre
     */
    public List<Movie> getMoviesByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be empty");
        }
        return movieDao.findByGenre(genre.trim());
    }
    
    /**
     * Get movies released after a specific date
     */
    public List<Movie> getMoviesReleasedAfter(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return movieDao.findByReleaseDateAfter(date);
    }
    
    /**
     * Get movies suitable for a specific age
     */
    public List<Movie> getMoviesSuitableForAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        return movieDao.findByAgeRating(age);
    }
    
    /**
     * Get movies by duration range
     */
    public List<Movie> getMoviesByDurationRange(int minDuration, int maxDuration) {
        if (minDuration < 0 || maxDuration < 0) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }
        if (minDuration > maxDuration) {
            throw new IllegalArgumentException("Minimum duration cannot be greater than maximum duration");
        }
        return movieDao.findByDurationRange(minDuration, maxDuration);
    }
    
    /**
     * Get long movies (duration > 150 minutes)
     */
    public List<Movie> getLongMovies() {
        return movieDao.findLongMovies();
    }
    
    /**
     * Get movies with available showtimes
     */
    public List<Movie> getMoviesWithAvailableShowtimes() {
        return movieDao.findMoviesWithAvailableShowtimes();
    }
    
    /**
     * Validate movie data
     */
    private void validateMovie(Movie movie) {
        if (movie.getTitle() == null || movie.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title is required");
        }
        
        if (movie.getGenre() == null || movie.getGenre().trim().isEmpty()) {
            throw new IllegalArgumentException("Movie genre is required");
        }
        
        if (movie.getDuration() <= 0) {
            throw new IllegalArgumentException("Movie duration must be positive");
        }
        
        if (movie.getReleaseDate() == null) {
            throw new IllegalArgumentException("Movie release date is required");
        }
        
        if (movie.getAgeRating() < 0) {
            throw new IllegalArgumentException("Movie age rating cannot be negative");
        }
    }
} 