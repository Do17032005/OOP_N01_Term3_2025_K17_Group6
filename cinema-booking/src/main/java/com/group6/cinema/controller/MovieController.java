package com.group6.cinema.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group6.cinema.model.Movie;
import com.group6.cinema.service.MovieService;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<Movie> list() {
        return movieService.findAll();
    }

    @PostMapping
    public Movie create(@RequestBody Movie movie) {
        return movieService.save(movie);
    }
}
