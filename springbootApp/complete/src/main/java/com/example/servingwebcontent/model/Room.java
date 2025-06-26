package com.example.servingwebcontent.model;

public class Room {
    private String id;
    private String name;
    private int totalSeats;

    public Room() {}

    public Room(String id, String name, int totalSeats) {
        this.id = id;
        this.name = name;
        this.totalSeats = totalSeats;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", totalSeats=" + totalSeats +
                '}';
    }
} 