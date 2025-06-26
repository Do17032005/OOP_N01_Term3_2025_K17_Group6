package com.example.servingwebcontent;

import com.example.servingwebcontent.database.MovieDAO;
import com.example.servingwebcontent.model.Movie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
public class MovieController {
    private final MovieDAO movieDAO = new MovieDAO();

    @GetMapping("/movies")
    public String getAllMovies(Model model) {
        List<Movie> movies = movieDAO.getAllMovies();
        model.addAttribute("movies", movies);
        return "movie-list"; // Tên file HTML trong templates
    }

    @GetMapping("/movies/add")
    public String showAddForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "add-movie";
    }

    @PostMapping("/movies/add")
    public String addMovie(@ModelAttribute Movie movie, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        if (movie.getId() == null || movie.getId().trim().isEmpty()) {
            movie.setId(UUID.randomUUID().toString());
        }
        movieDAO.insertMovie(movie);
        redirectAttributes.addFlashAttribute("message", "Đã thêm phim mới. Hãy thêm suất chiếu cho phim này!");
        return "redirect:/showtimes/add?movieId=" + movie.getId();
    }

    @GetMapping("/movies/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        List<Movie> movies = movieDAO.getAllMovies();
        Movie movie = movies.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
        if (movie == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy phim với ID này. Có thể phim đã bị xóa hoặc ID không hợp lệ.");
            return "redirect:/movies";
        }
        model.addAttribute("movie", movie);
        return "edit-movie";
    }

    @PostMapping("/movies/edit")
    public String editMovie(@ModelAttribute Movie movie, RedirectAttributes redirectAttributes) {
        try {
            movieDAO.updateMovie(movie);
            redirectAttributes.addFlashAttribute("message", "Cập nhật phim thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật phim thất bại!");
        }
        return "redirect:/movies";
    }

    @GetMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            movieDAO.deleteMovie(id);
            redirectAttributes.addFlashAttribute("message", "Xóa phim thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Xóa phim thất bại!");
        }
        return "redirect:/movies";
    }

    @GetMapping("/movies/edit")
    public String redirectEditNoId(org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Bạn phải chọn phim để sửa!");
        return "redirect:/movies";
    }
}