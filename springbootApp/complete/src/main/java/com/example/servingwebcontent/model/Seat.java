package com.example.servingwebcontent.model;

public class Seat {
    private String id;
    private String roomId;
    private String seatNumber;

    public Seat() {}

    public Seat(String id, String roomId, String seatNumber) {
        this.id = id;
        this.roomId = roomId;
        this.seatNumber = seatNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id='" + id + '\'' +
                ", roomId='" + roomId + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                '}';
    }
} 