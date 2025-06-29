package com.example.servingwebcontent;

import com.example.servingwebcontent.database.ShowtimeDAO;
import com.example.servingwebcontent.database.MovieDAO;
import com.example.servingwebcontent.database.RoomDAO;
import com.example.servingwebcontent.model.Showtime;
import com.example.servingwebcontent.model.Movie;
import com.example.servingwebcontent.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/showtime")
public class ShowtimeController {
    
    private final ShowtimeDAO showtimeDAO;
    private final MovieDAO movieDAO;
    private final RoomDAO roomDAO;

    @Autowired
    public ShowtimeController(ShowtimeDAO showtimeDAO, MovieDAO movieDAO, RoomDAO roomDAO) {
        this.showtimeDAO = showtimeDAO;
        this.movieDAO = movieDAO;
        this.roomDAO = roomDAO;
    }

    // === LIST SHOWTIMES ===
    @GetMapping("/list")
    public String listShowtime(Model model) {
        try {
            List<Showtime> showtimes = showtimeDAO.getAllShowtimes();
            model.addAttribute("showtimes", showtimes);
            return "showtime/list";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải danh sách suất chiếu: " + e.getMessage());
            return "showtime/list";
        }
    }

    // === SHOW ADD FORM ===
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("showtime", new Showtime());
        model.addAttribute("movies", movieDAO.getAllMovies());
        model.addAttribute("rooms", roomDAO.getAllRooms());
        return "showtime/add";
    }
    
    // === ADD SHOWTIME ===
    @PostMapping("/add")
    public String addShowtime(@ModelAttribute Showtime showtime, 
                             Model model, 
                             RedirectAttributes redirectAttributes) {
        try {
            // Validation using ValidationUtils
            ValidationUtils.validateShowtime(showtime.getMovieId(), showtime.getRoomId(), 
                showtime.getStartTime() != null ? showtime.getStartTime().toString() : null);
            
            // Generate ID if not provided
            if (ValidationUtils.isEmpty(showtime.getId())) {
                showtime.setId(UUID.randomUUID().toString());
            }
            
            showtimeDAO.insertShowtime(showtime);
            redirectAttributes.addFlashAttribute("success", "Thêm suất chiếu thành công!");
            return "redirect:/showtime/list";
            
        } catch (IllegalArgumentException e) {
            // Validation error
            model.addAttribute("error", e.getMessage());
            model.addAttribute("showtime", showtime);
            model.addAttribute("movies", movieDAO.getAllMovies());
            model.addAttribute("rooms", roomDAO.getAllRooms());
            return "showtime/add";
        } catch (Exception e) {
            // System error
            model.addAttribute("error", "Lỗi hệ thống khi thêm suất chiếu: " + e.getMessage());
            model.addAttribute("showtime", showtime);
            model.addAttribute("movies", movieDAO.getAllMovies());
            model.addAttribute("rooms", roomDAO.getAllRooms());
            return "showtime/add";
        }
    }
    
    // === SHOW EDIT FORM ===
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                redirectAttributes.addFlashAttribute("error", "ID suất chiếu không hợp lệ!");
                return "redirect:/showtime/list";
            }
            
            // Find showtime by ID
            Showtime showtime = findShowtimeById(id);
            if (showtime == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy suất chiếu với ID: " + id);
                return "redirect:/showtime/list";
            }
            
            model.addAttribute("showtime", showtime);
            model.addAttribute("movies", movieDAO.getAllMovies());
            model.addAttribute("rooms", roomDAO.getAllRooms());
            return "showtime/edit";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            return "redirect:/showtime/list";
        }
    }

    // === UPDATE SHOWTIME ===
    @PostMapping("/edit/{id}")
    public String updateShowtime(@PathVariable String id, 
                                @ModelAttribute Showtime showtime, 
                                Model model, 
                                RedirectAttributes redirectAttributes) {
        try {
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                redirectAttributes.addFlashAttribute("error", "ID suất chiếu không hợp lệ!");
                return "redirect:/showtime/list";
            }
            
            // Validate showtime data
            ValidationUtils.validateShowtime(showtime.getMovieId(), showtime.getRoomId(), 
                showtime.getStartTime() != null ? showtime.getStartTime().toString() : null);
            
            // Set ID from path
            showtime.setId(id);
            
            // Check if showtime exists
            Showtime existingShowtime = findShowtimeById(id);
            if (existingShowtime == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy suất chiếu để cập nhật!");
                return "redirect:/showtime/list";
            }
            
            showtimeDAO.updateShowtime(showtime);
            redirectAttributes.addFlashAttribute("success", "Cập nhật suất chiếu thành công!");
            return "redirect:/showtime/list";
            
        } catch (IllegalArgumentException e) {
            // Validation error
            model.addAttribute("error", e.getMessage());
            model.addAttribute("showtime", showtime);
            model.addAttribute("movies", movieDAO.getAllMovies());
            model.addAttribute("rooms", roomDAO.getAllRooms());
            return "showtime/edit";
        } catch (Exception e) {
            // System error
            model.addAttribute("error", "Lỗi hệ thống khi cập nhật suất chiếu: " + e.getMessage());
            model.addAttribute("showtime", showtime);
            model.addAttribute("movies", movieDAO.getAllMovies());
            model.addAttribute("rooms", roomDAO.getAllRooms());
            return "showtime/edit";
        }
    }

    // === DELETE SHOWTIME ===
    @PostMapping("/delete/{id}")
    public String deleteShowtime(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                redirectAttributes.addFlashAttribute("error", "ID suất chiếu không hợp lệ!");
                return "redirect:/showtime/list";
            }
            
            // Check if showtime exists
            Showtime existingShowtime = findShowtimeById(id);
            if (existingShowtime == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy suất chiếu để xóa!");
                return "redirect:/showtime/list";
            }
            
            showtimeDAO.deleteShowtime(id);
            redirectAttributes.addFlashAttribute("success", "Xóa suất chiếu thành công!");
            return "redirect:/showtime/list";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống khi xóa suất chiếu: " + e.getMessage());
            return "redirect:/showtime/list";
        }
    }

    // === PRIVATE HELPER METHODS ===
    
    /**
     * Tìm showtime theo ID
     * Note: ShowtimeDAO chưa có method getShowtimeById, nên phải filter từ getAllShowtimes
     */
    private Showtime findShowtimeById(String id) {
        try {
            List<Showtime> showtimes = showtimeDAO.getAllShowtimes();
            return showtimes.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm kiếm suất chiếu: " + e.getMessage(), e);
        }
    }
} 