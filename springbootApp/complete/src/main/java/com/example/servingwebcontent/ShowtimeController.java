package com.example.servingwebcontent;

import com.example.servingwebcontent.database.ShowtimeDAO;
import com.example.servingwebcontent.model.Showtime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.UUID;

@Controller
public class ShowtimeController {
    private final ShowtimeDAO showtimeDAO = new ShowtimeDAO();

    @GetMapping("/showtimes")
    public String getAllShowtimes(Model model) {
        List<Showtime> showtimes = showtimeDAO.getAllShowtimes();
        model.addAttribute("showtimes", showtimes);
        return "showtime-list";
    }

    @GetMapping("/showtimes/add")
    public String showAddForm(@RequestParam(value = "movieId", required = false) String movieId, Model model) {
        Showtime showtime = new Showtime();
        if (movieId != null && !movieId.isEmpty()) {
            showtime.setMovieId(movieId);
        }
        model.addAttribute("showtime", showtime);
        return "add-showtime";
    }

    @PostMapping("/showtimes/add")
    public String addShowtime(@ModelAttribute Showtime showtime, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        if (showtime.getId() == null || showtime.getId().trim().isEmpty()) {
            showtime.setId(UUID.randomUUID().toString());
        }
        try {
            showtimeDAO.insertShowtime(showtime);
            redirectAttributes.addFlashAttribute("message", "Thêm suất chiếu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thêm suất chiếu thất bại!");
        }
        return "redirect:/showtimes";
    }

    @GetMapping("/showtimes/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, HttpSession session) {
        List<Showtime> showtimes = showtimeDAO.getAllShowtimes();
        Showtime showtime = showtimes.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
        model.addAttribute("showtime", showtime);
        model.addAttribute("loggedInCustomer", session.getAttribute("loggedInCustomer"));
        return "edit-showtime";
    }

    @PostMapping("/showtimes/edit")
    public String editShowtime(@ModelAttribute Showtime showtime) {
        showtimeDAO.updateShowtime(showtime);
        return "redirect:/showtimes";
    }

    @GetMapping("/showtimes/delete/{id}")
    public String deleteShowtime(@PathVariable String id) {
        showtimeDAO.deleteShowtime(id);
        return "redirect:/showtimes";
    }
} 