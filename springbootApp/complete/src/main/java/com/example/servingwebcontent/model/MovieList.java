package com.example.servingwebcontent.model;
import java.util.ArrayList;


public class MovieList {
    public ArrayList<Movie> movies = new ArrayList<>();

    public ArrayList<Movie> addMovie(Movie movie) {
        movies.add(movie);
        return movies;
    }
    // Sửa thông tin phim theo ID

    public ArrayList<Movie> getEditMovieById(String name, String movieId) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId().equals(movieId)) {
                movies.get(i).setTitle(name);
                break;
            }
        }
        return movies;
    }

    // Sửa thông theo tên
    public ArrayList<Movie> getEditMovieByName(String name, String title) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getName().equals(name)) {
                movies.get(i).setTitle(title);
                break;
            }
        }
        return movies;
    }

    // Tìm phim theo ID

    public Movie findMovieById(String id) {
        for (Movie movie : this.movies) { // assuming 'movies' is your list of Movie objects
            if (movie.getId().equals(id)) {
                return movie;
            }
        }
        return null;
    }

    public ArrayList<Movie> getDeleteMovie(String movieId) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId().equals(movieId)) {
                movies.remove(i);
                break;
            }
        }
        return movies;
    }

    public void printMovieList() {
        for (Movie movie : movies) {
            System.out.println("Movie ID: " + movie.getId());
            System.out.println("Movie Name: " + movie.getName());
            System.out.println("Movie Title: " + movie.getTitle());
            System.out.println("Show Time: " + movie.getShowTime());
            System.out.println("Show DateTime: " + movie.getDateTime());
            System.out.println("Duration: " + movie.getDuration());
            System.out.println("Genre: " + movie.getGenre());
            System.out.println("Age: " + movie.getAge());
            System.out.println("-------------------------");
        }
    }
}
