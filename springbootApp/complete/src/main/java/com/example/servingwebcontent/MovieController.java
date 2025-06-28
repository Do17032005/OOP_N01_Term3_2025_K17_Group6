package com.example.servingwebcontent;

import com.example.servingwebcontent.database.MovieDAO;
import com.example.servingwebcontent.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
public class MovieController {
    
    private final MovieDAO movieDAO;
    
    @Autowired
    public MovieController(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    // === MOVIE LISTING ===
    
    @GetMapping("/movies")
    public String getAllMovies(Model model) {
        try {
            List<Movie> movies = movieDAO.getAllMovies();
            model.addAttribute("movies", movies);
            return "movie/list";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải danh sách phim: " + e.getMessage());
            return "movie/list";
        }
    }

    // === MOVIE CREATION ===
    
    @GetMapping("/movies/add")
    public String showAddForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "movie/add";
    }

    @PostMapping("/movies/add")
    public String addMovie(@ModelAttribute Movie movie, RedirectAttributes redirectAttributes) {
        try {
            // Validate movie data
            if (!isValidMovie(movie)) {
                redirectAttributes.addFlashAttribute("error", "Thông tin phim không hợp lệ!");
                return "redirect:/movies/add";
            }
            
            // Generate ID if not provided
            if (movie.getId() == null || movie.getId().trim().isEmpty()) {
                movie.setId(UUID.randomUUID().toString());
            }
            
            movieDAO.insertMovie(movie);
            redirectAttributes.addFlashAttribute("success", "Đã thêm phim mới. Hãy thêm suất chiếu cho phim này!");
            return "redirect:/showtimes/add?movieId=" + movie.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thêm phim thất bại: " + e.getMessage());
            return "redirect:/movies/add";
        }
    }

    // === MOVIE EDITING ===
    
    @GetMapping("/movies/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Movie movie = findMovieById(id);
            if (movie == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy phim với ID: " + id);
                return "redirect:/movies";
            }
            model.addAttribute("movie", movie);
            return "movie/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể tải thông tin phim: " + e.getMessage());
            return "redirect:/movies";
        }
    }

    @PostMapping("/movies/edit")
    public String editMovie(@ModelAttribute Movie movie, RedirectAttributes redirectAttributes) {
        try {
            if (!isValidMovie(movie)) {
                redirectAttributes.addFlashAttribute("error", "Thông tin phim không hợp lệ!");
                return "redirect:/movies/edit/" + movie.getId();
            }
            
            movieDAO.updateMovie(movie);
            redirectAttributes.addFlashAttribute("success", "Cập nhật phim thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật phim thất bại: " + e.getMessage());
        }
        return "redirect:/movies";
    }

    // === MOVIE DELETION ===
    
    @GetMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            Movie movie = findMovieById(id);
            if (movie == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy phim để xóa!");
                return "redirect:/movies";
            }
            
            movieDAO.deleteMovie(id);
            redirectAttributes.addFlashAttribute("success", "Xóa phim thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Xóa phim thất bại: " + e.getMessage());
        }
        return "redirect:/movies";
    }

    // === ERROR HANDLING ===
    
    @GetMapping("/movies/edit")
    public String redirectEditNoId(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Bạn phải chọn phim để sửa!");
        return "redirect:/movies";
    }

    // === PRIVATE HELPER METHODS ===
    
    private Movie findMovieById(String id) {
        return movieDAO.getAllMovies().stream()
            .filter(m -> m.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    private boolean isValidMovie(Movie movie) {
        return movie != null && 
               movie.getName() != null && !movie.getName().trim().isEmpty() &&
               movie.getTitle() != null && !movie.getTitle().trim().isEmpty() &&
               movie.getGenre() != null && !movie.getGenre().trim().isEmpty() &&
               movie.getDuration() > 0 &&
               movie.getAge() >= 0;
    }
}