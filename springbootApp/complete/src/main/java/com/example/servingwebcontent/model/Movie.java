package com.example.servingwebcontent.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String genre;
    
    @Column(nullable = false)
    private int duration; // Thời lượng của phim (phút)
    
    @Column(name = "age_rating", nullable = false)
    private int ageRating; // Độ tuổi tối thiểu
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "release_date")
    private LocalDate releaseDate; // Ngày phát hành
    
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Showtime> showtimes;
    
    @PrePersist
    protected void onCreate() {
        if (releaseDate == null) {
            releaseDate = LocalDate.now();
        }
    }

    // Default constructor
    public Movie() {}

    // Constructor with required fields
    public Movie(String name, String title, String genre, int duration, int ageRating, String description) {
        this.name = name;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.ageRating = ageRating;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(int ageRating) {
        this.ageRating = ageRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Showtime> getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }

    // Business logic methods
    public boolean isSuitableForAge(int viewerAge) {
        return viewerAge >= ageRating;
    }
    
    public boolean isLongMovie() {
        return duration > 150; // Phim dài hơn 2.5 giờ
    }
    
    public String getDurationFormatted() {
        int hours = duration / 60;
        int minutes = duration % 60;
        return hours + "h " + minutes + "m";
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", duration=" + duration +
                ", ageRating=" + ageRating +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Movie movie = (Movie) obj;
        return id != null && id.equals(movie.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}