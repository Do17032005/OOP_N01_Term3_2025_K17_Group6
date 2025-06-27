package com.example.servingwebcontent;

import com.example.servingwebcontent.model.Showtime;
import com.example.servingwebcontent.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class ShowtimeCrudController {

    @Autowired
    private ShowtimeService showtimeService;

    @GetMapping("/showtimes")
    public String listShowtimes(Model model, 
                               @RequestParam(required = false) String keyword,
                               @RequestParam(defaultValue = "0") int page) {
        try {
            List<Showtime> showtimes = showtimeService.getAllShowtimes();
            
            // Filter by keyword
            if (keyword != null && !keyword.trim().isEmpty()) {
                showtimes = showtimes.stream()
                    .filter(showtime -> showtime.getMovie().getTitle().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
            }

            // Pagination
            int pageSize = 10;
            int totalPages = (int) Math.ceil((double) showtimes.size() / pageSize);
            int start = page * pageSize;
            int end = Math.min(start + pageSize, showtimes.size());
            
            List<Showtime> pagedShowtimes = showtimes.subList(start, end);

            // Prepare model data
            model.addAttribute("items", pagedShowtimes);
            model.addAttribute("title", "Showtimes");
            model.addAttribute("icon", "bi bi-calendar-event");
            model.addAttribute("addUrl", "/showtimes/add");
            model.addAttribute("listUrl", "/showtimes");
            model.addAttribute("searchUrl", "/showtimes");
            model.addAttribute("viewUrl", "/showtimes/view");
            model.addAttribute("editUrl", "/showtimes/edit");
            model.addAttribute("deleteUrl", "/showtimes/delete");
            model.addAttribute("keyword", keyword);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);

            // Define columns
            List<Map<String, Object>> columns = Arrays.asList(
                Map.of("field", "movie.title", "label", "Movie", "type", ""),
                Map.of("field", "room.name", "label", "Room", "type", ""),
                Map.of("field", "startTime", "label", "Start Time", "type", "date"),
                Map.of("field", "endTime", "label", "End Time", "type", "date"),
                Map.of("field", "price", "label", "Price", "type", "currency")
            );
            model.addAttribute("columns", columns);

        } catch (Exception e) {
            model.addAttribute("error", "Error loading showtimes: " + e.getMessage());
        }

        return "crud";
    }
} 