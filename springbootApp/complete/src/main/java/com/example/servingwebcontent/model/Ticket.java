package com.example.servingwebcontent.model;

public class Ticket {
    private String id;
    private String showtimeId;
    private String seatId;
    private String customerId;
    private double price;

    public Ticket() {}

    public Ticket(String id, String showtimeId, String seatId, String customerId, double price) {
        this.id = id;
        this.showtimeId = showtimeId;
        this.seatId = seatId;
        this.customerId = customerId;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(String showtimeId) {
        this.showtimeId = showtimeId;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", showtimeId='" + showtimeId + '\'' +
                ", seatId='" + seatId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", price=" + price +
                '}';
    }
}
