package com.example.servingwebcontent.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeatList {
    private List<Seat> seats = new ArrayList<>();

    public List<Seat> getAll() {
        return new ArrayList<>(seats);
    }

    public Optional<Seat> findById(Long id) {
        return seats.stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    public void add(Seat seat) {
        seats.add(seat);
    }

    public boolean remove(Long id) {
        return seats.removeIf(s -> s.getId().equals(id));
    }

    public boolean update(Seat seat) {
        for (int i = 0; i < seats.size(); i++) {
            if (seats.get(i).getId().equals(seat.getId())) {
                seats.set(i, seat);
                return true;
            }
        }
        return false;
    }
} 