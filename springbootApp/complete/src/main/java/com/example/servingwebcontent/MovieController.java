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
@RequestMapping("/movie")
public class MovieController {
    
    private final MovieDAO movieDAO;

    @Autowired
    public MovieController(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    // === LIST MOVIES ===
    @GetMapping("/list")
    public String listMovie(Model model) {
        try {
            List<Movie> movies = movieDAO.getAllMovies();
            model.addAttribute("movies", movies);
            return "movie/list";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải danh sách phim: " + e.getMessage());
            return "movie/list";
        }
    }

    // === SHOW ADD FORM ===
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "movie/add";
    }

    // === ADD MOVIE ===
    @PostMapping("/add")
    public String addMovie(@ModelAttribute Movie movie, 
                          Model model, 
                          RedirectAttributes redirectAttributes) {
        try {
            // Validation using ValidationUtils
            ValidationUtils.validateMovie(movie.getTitle(), movie.getDescription(), movie.getDuration());
            
            // Generate ID if not provided
            if (ValidationUtils.isEmpty(movie.getId())) {
                movie.setId(UUID.randomUUID().toString());
            }
            
            movieDAO.insertMovie(movie);
            redirectAttributes.addFlashAttribute("success", "Thêm phim thành công!");
            return "redirect:/movie/list";
            
        } catch (IllegalArgumentException e) {
            // Validation error
            model.addAttribute("error", e.getMessage());
            model.addAttribute("movie", movie);
            return "movie/add";
        } catch (Exception e) {
            // System error
            model.addAttribute("error", "Lỗi hệ thống khi thêm phim: " + e.getMessage());
            model.addAttribute("movie", movie);
            return "movie/add";
        }
    }

    // === SHOW EDIT FORM ===
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                redirectAttributes.addFlashAttribute("error", "ID phim không hợp lệ!");
                return "redirect:/movie/list";
            }
            
            // Find movie by ID
            Movie movie = findMovieById(id);
            if (movie == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy phim với ID: " + id);
                return "redirect:/movie/list";
            }
            
            model.addAttribute("movie", movie);
            return "movie/edit";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            return "redirect:/movie/list";
        }
    }

    // === UPDATE MOVIE ===
    @PostMapping("/edit/{id}")
    public String updateMovie(@PathVariable String id, 
                             @ModelAttribute Movie movie, 
                             Model model, 
                             RedirectAttributes redirectAttributes) {
        try {
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                redirectAttributes.addFlashAttribute("error", "ID phim không hợp lệ!");
                return "redirect:/movie/list";
            }
            
            // Validate movie data
            ValidationUtils.validateMovie(movie.getTitle(), movie.getDescription(), movie.getDuration());
            
            // Set ID from path
            movie.setId(id);
            
            // Check if movie exists
            Movie existingMovie = findMovieById(id);
            if (existingMovie == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy phim để cập nhật!");
                return "redirect:/movie/list";
            }
            
            movieDAO.updateMovie(movie);
            redirectAttributes.addFlashAttribute("success", "Cập nhật phim thành công!");
            return "redirect:/movie/list";
            
        } catch (IllegalArgumentException e) {
            // Validation error
            model.addAttribute("error", e.getMessage());
            model.addAttribute("movie", movie);
            return "movie/edit";
        } catch (Exception e) {
            // System error
            model.addAttribute("error", "Lỗi hệ thống khi cập nhật phim: " + e.getMessage());
            model.addAttribute("movie", movie);
            return "movie/edit";
        }
    }

    // === DELETE MOVIE ===
    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                redirectAttributes.addFlashAttribute("error", "ID phim không hợp lệ!");
                return "redirect:/movie/list";
            }
            
            // Check if movie exists
            Movie existingMovie = findMovieById(id);
            if (existingMovie == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy phim để xóa!");
                return "redirect:/movie/list";
            }
            
            movieDAO.deleteMovie(id);
            redirectAttributes.addFlashAttribute("success", "Xóa phim thành công!");
            return "redirect:/movie/list";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống khi xóa phim: " + e.getMessage());
            return "redirect:/movie/list";
        }
    }

    // === PRIVATE HELPER METHODS ===
    
    /**
     * Tìm movie theo ID
     * Note: MovieDAO chưa có method getMovieById, nên phải filter từ getAllMovies
     */
    private Movie findMovieById(String id) {
        try {
            List<Movie> movies = movieDAO.getAllMovies();
            return movies.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm kiếm phim: " + e.getMessage(), e);
        }
    }
}