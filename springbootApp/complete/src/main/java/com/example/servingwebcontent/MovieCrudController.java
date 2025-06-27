package com.example.servingwebcontent;

import com.example.servingwebcontent.model.Movie;
import com.example.servingwebcontent.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class MovieCrudController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    public String listMovies(Model model, 
                           @RequestParam(required = false) String keyword,
                           @RequestParam(required = false) String genre,
                           @RequestParam(defaultValue = "0") int page) {
        try {
            List<Movie> movies = movieService.getAllMovies();
            
            // Filter by keyword
            if (keyword != null && !keyword.trim().isEmpty()) {
                movies = movies.stream()
                    .filter(movie -> movie.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                                   movie.getGenre().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
            }
            
            // Filter by genre
            if (genre != null && !genre.trim().isEmpty()) {
                movies = movies.stream()
                    .filter(movie -> movie.getGenre().equalsIgnoreCase(genre))
                    .toList();
            }

            // Pagination
            int pageSize = 10;
            int totalPages = (int) Math.ceil((double) movies.size() / pageSize);
            int start = page * pageSize;
            int end = Math.min(start + pageSize, movies.size());
            
            List<Movie> pagedMovies = movies.subList(start, end);

            // Prepare model data
            model.addAttribute("items", pagedMovies);
            model.addAttribute("title", "Movies");
            model.addAttribute("icon", "bi bi-film");
            model.addAttribute("addUrl", "/movies/add");
            model.addAttribute("listUrl", "/movies");
            model.addAttribute("searchUrl", "/movies");
            model.addAttribute("viewUrl", "/movies/view");
            model.addAttribute("editUrl", "/movies/edit");
            model.addAttribute("deleteUrl", "/movies/delete");
            model.addAttribute("keyword", keyword);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);

            // Define columns
            List<Map<String, Object>> columns = Arrays.asList(
                Map.of("field", "title", "label", "Title", "type", ""),
                Map.of("field", "genre", "label", "Genre", "type", ""),
                Map.of("field", "duration", "label", "Duration", "type", ""),
                Map.of("field", "ageRating", "label", "Age Rating", "type", ""),
                Map.of("field", "price", "label", "Price", "type", "currency")
            );
            model.addAttribute("columns", columns);

            // Filter options
            List<Map<String, String>> filterOptions = Arrays.asList(
                Map.of("value", "Action", "label", "Action"),
                Map.of("value", "Comedy", "label", "Comedy"),
                Map.of("value", "Drama", "label", "Drama"),
                Map.of("value", "Horror", "label", "Horror"),
                Map.of("value", "Romance", "label", "Romance"),
                Map.of("value", "Sci-Fi", "label", "Sci-Fi"),
                Map.of("value", "Thriller", "label", "Thriller")
            );
            model.addAttribute("filterOptions", filterOptions);
            model.addAttribute("selectedFilter", genre);

        } catch (Exception e) {
            model.addAttribute("error", "Error loading movies: " + e.getMessage());
        }

        return "crud";
    }
} 