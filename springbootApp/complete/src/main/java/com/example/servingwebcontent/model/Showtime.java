package com.example.servingwebcontent.model;

import java.time.LocalDateTime;

public class Showtime {
    private String id;
    private String movieId;
    private String roomId;
    private LocalDateTime startTime;

    public Showtime() {}

    public Showtime(String id, String movieId, String roomId, LocalDateTime startTime) {
        this.id = id;
        this.movieId = movieId;
        this.roomId = roomId;
        this.startTime = startTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "Showtime{" +
                "id='" + id + '\'' +
                ", movieId='" + movieId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", startTime=" + startTime +
                '}';
    }
} 