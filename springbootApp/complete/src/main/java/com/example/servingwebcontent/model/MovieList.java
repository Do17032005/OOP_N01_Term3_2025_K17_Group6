package com.example.servingwebcontent.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieList {
    private List<Movie> movies = new ArrayList<>();

    public List<Movie> getAll() {
        return new ArrayList<>(movies);
    }

    public Optional<Movie> findById(Long id) {
        return movies.stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    public void add(Movie movie) {
        movies.add(movie);
    }

    public boolean remove(Long id) {
        return movies.removeIf(m -> m.getId().equals(id));
    }

    public boolean update(Movie movie) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId().equals(movie.getId())) {
                movies.set(i, movie);
                return true;
            }
        }
        return false;
    }
} 