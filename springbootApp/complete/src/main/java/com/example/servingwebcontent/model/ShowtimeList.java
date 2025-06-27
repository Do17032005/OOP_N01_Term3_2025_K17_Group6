package com.example.servingwebcontent.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowtimeList {
    private List<Showtime> showtimes = new ArrayList<>();

    public List<Showtime> getAll() {
        return new ArrayList<>(showtimes);
    }

    public Optional<Showtime> findById(Long id) {
        return showtimes.stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    public void add(Showtime showtime) {
        showtimes.add(showtime);
    }

    public boolean remove(Long id) {
        return showtimes.removeIf(s -> s.getId().equals(id));
    }

    public boolean update(Showtime showtime) {
        for (int i = 0; i < showtimes.size(); i++) {
            if (showtimes.get(i).getId().equals(showtime.getId())) {
                showtimes.set(i, showtime);
                return true;
            }
        }
        return false;
    }
} 