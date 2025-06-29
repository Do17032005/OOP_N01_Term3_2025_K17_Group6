package com.example.servingwebcontent.model;
import java.util.ArrayList;

public class SeatList {
    public ArrayList<Seat> seats = new ArrayList<>();

    public ArrayList<Seat> addSeat(Seat seat) {
        seats.add(seat);
        return seats;
    }

    public ArrayList<Seat> getEditSeatById(String id, String seatNumber) {
        for (int i = 0; i < seats.size(); i++) {
            if (seats.get(i).getId().equals(id)) {
                seats.get(i).setSeatNumber(seatNumber);
                break;
            }
        }
        return seats;
    }

    public ArrayList<Seat> getDeleteSeat(String id) {
        for (int i = 0; i < seats.size(); i++) {
            if (seats.get(i).getId().equals(id)) {
                seats.remove(i);
                break;
            }
        }
        return seats;
    }

    public void printSeatList() {
        for (int i = 0; i < seats.size(); i++) {
            System.out.println(seats.get(i));
        }
    }
}
