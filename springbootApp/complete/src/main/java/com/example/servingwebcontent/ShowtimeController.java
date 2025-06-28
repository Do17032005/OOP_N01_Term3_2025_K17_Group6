package com.example.servingwebcontent;

import com.example.servingwebcontent.database.ShowtimeDAO;
import com.example.servingwebcontent.model.Showtime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.UUID;

@Controller
public class ShowtimeController {
    
    private final ShowtimeDAO showtimeDAO;
    
    @Autowired
    public ShowtimeController(ShowtimeDAO showtimeDAO) {
        this.showtimeDAO = showtimeDAO;
    }

    // === SHOWTIME LISTING ===
    
    @GetMapping("/showtimes")
    public String getAllShowtimes(Model model) {
        try {
            List<Showtime> showtimes = showtimeDAO.getAllShowtimes();
            model.addAttribute("showtimes", showtimes);
            return "showtime/list";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải danh sách suất chiếu: " + e.getMessage());
            return "showtime/list";
        }
    }

    // === SHOWTIME CREATION ===
    
    @GetMapping("/showtimes/add")
    public String showAddForm(@RequestParam(value = "movieId", required = false) String movieId, Model model) {
        try {
            Showtime showtime = new Showtime();
            if (movieId != null && !movieId.trim().isEmpty()) {
                showtime.setMovieId(movieId);
            }
            model.addAttribute("showtime", showtime);
            return "showtime/add";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải form thêm suất chiếu: " + e.getMessage());
            return "showtime/add";
        }
    }

    @PostMapping("/showtimes/add")
    public String addShowtime(@ModelAttribute Showtime showtime, RedirectAttributes redirectAttributes) {
        try {
            if (!isValidShowtime(showtime)) {
                redirectAttributes.addFlashAttribute("error", "Thông tin suất chiếu không hợp lệ!");
                return "redirect:/showtimes/add";
            }
            
            if (showtime.getId() == null || showtime.getId().trim().isEmpty()) {
                showtime.setId(UUID.randomUUID().toString());
            }
            
            showtimeDAO.insertShowtime(showtime);
            redirectAttributes.addFlashAttribute("success", "Thêm suất chiếu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thêm suất chiếu thất bại: " + e.getMessage());
        }
        return "redirect:/showtimes";
    }

    // === SHOWTIME EDITING ===
    
    @GetMapping("/showtimes/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Showtime showtime = findShowtimeById(id);
            if (showtime == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy suất chiếu với ID: " + id);
                return "redirect:/showtimes";
            }
            model.addAttribute("showtime", showtime);
            model.addAttribute("loggedInCustomer", session.getAttribute("loggedInCustomer"));
            return "showtime/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể tải thông tin suất chiếu: " + e.getMessage());
            return "redirect:/showtimes";
        }
    }

    @PostMapping("/showtimes/edit")
    public String editShowtime(@ModelAttribute Showtime showtime, RedirectAttributes redirectAttributes) {
        try {
            if (!isValidShowtime(showtime)) {
                redirectAttributes.addFlashAttribute("error", "Thông tin suất chiếu không hợp lệ!");
                return "redirect:/showtimes/edit/" + showtime.getId();
            }
            
            showtimeDAO.updateShowtime(showtime);
            redirectAttributes.addFlashAttribute("success", "Cập nhật suất chiếu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật suất chiếu thất bại: " + e.getMessage());
        }
        return "redirect:/showtimes";
    }

    // === SHOWTIME DELETION ===
    
    @GetMapping("/showtimes/delete/{id}")
    public String deleteShowtime(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            Showtime showtime = findShowtimeById(id);
            if (showtime == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy suất chiếu để xóa!");
                return "redirect:/showtimes";
            }
            
            showtimeDAO.deleteShowtime(id);
            redirectAttributes.addFlashAttribute("success", "Xóa suất chiếu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Xóa suất chiếu thất bại: " + e.getMessage());
        }
        return "redirect:/showtimes";
    }

    // === PRIVATE HELPER METHODS ===
    
    private Showtime findShowtimeById(String id) {
        return showtimeDAO.getAllShowtimes().stream()
            .filter(s -> s.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    private boolean isValidShowtime(Showtime showtime) {
        return showtime != null && 
               showtime.getMovieId() != null && !showtime.getMovieId().trim().isEmpty() &&
               showtime.getRoomId() != null && !showtime.getRoomId().trim().isEmpty() &&
               showtime.getStartTime() != null;
    }
} 