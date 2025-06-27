package com.group6.cinema.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.group6.cinema.model.Movie;
import com.group6.cinema.repository.MovieRepository;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }
}
