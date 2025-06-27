package com.group6.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.group6.cinema.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
