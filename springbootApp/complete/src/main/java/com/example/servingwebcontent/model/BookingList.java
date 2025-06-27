package com.example.servingwebcontent.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingList {
    private List<Booking> bookings = new ArrayList<>();

    public List<Booking> getAll() {
        return new ArrayList<>(bookings);
    }

    public Optional<Booking> findById(Long id) {
        return bookings.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    public void add(Booking booking) {
        bookings.add(booking);
    }

    public boolean remove(Long id) {
        return bookings.removeIf(b -> b.getId().equals(id));
    }

    public boolean update(Booking booking) {
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getId().equals(booking.getId())) {
                bookings.set(i, booking);
                return true;
            }
        }
        return false;
    }
} 