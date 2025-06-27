package com.example.servingwebcontent;

import com.example.servingwebcontent.model.Seat;
import com.example.servingwebcontent.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class SeatCrudController {

    @Autowired
    private SeatService seatService;

    @GetMapping("/seats")
    public String listSeats(Model model, 
                           @RequestParam(required = false) String keyword,
                           @RequestParam(required = false) String type,
                           @RequestParam(defaultValue = "0") int page) {
        try {
            List<Seat> seats = seatService.getAllSeats();
            
            // Filter by keyword
            if (keyword != null && !keyword.trim().isEmpty()) {
                seats = seats.stream()
                    .filter(seat -> seat.getSeatNumber().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
            }
            
            // Filter by type
            if (type != null && !type.trim().isEmpty()) {
                seats = seats.stream()
                    .filter(seat -> seat.getSeatType().toString().equalsIgnoreCase(type))
                    .toList();
            }

            // Pagination
            int pageSize = 10;
            int totalPages = (int) Math.ceil((double) seats.size() / pageSize);
            int start = page * pageSize;
            int end = Math.min(start + pageSize, seats.size());
            
            List<Seat> pagedSeats = seats.subList(start, end);

            // Prepare model data
            model.addAttribute("items", pagedSeats);
            model.addAttribute("title", "Seats");
            model.addAttribute("icon", "bi bi-grid-3x3-gap");
            model.addAttribute("addUrl", "/seats/add");
            model.addAttribute("listUrl", "/seats");
            model.addAttribute("searchUrl", "/seats");
            model.addAttribute("viewUrl", "/seats/view");
            model.addAttribute("editUrl", "/seats/edit");
            model.addAttribute("deleteUrl", "/seats/delete");
            model.addAttribute("keyword", keyword);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);

            // Define columns
            List<Map<String, Object>> columns = Arrays.asList(
                Map.of("field", "seatNumber", "label", "Seat Number", "type", ""),
                Map.of("field", "room.name", "label", "Room", "type", ""),
                Map.of("field", "seatType", "label", "Type", "type", "status"),
                Map.of("field", "rowNumber", "label", "Row", "type", ""),
                Map.of("field", "columnNumber", "label", "Column", "type", "")
            );
            model.addAttribute("columns", columns);

            // Filter options
            List<Map<String, String>> filterOptions = Arrays.asList(
                Map.of("value", "STANDARD", "label", "Standard"),
                Map.of("value", "PREMIUM", "label", "Premium"),
                Map.of("value", "VIP", "label", "VIP")
            );
            model.addAttribute("filterOptions", filterOptions);
            model.addAttribute("selectedFilter", type);

        } catch (Exception e) {
            model.addAttribute("error", "Error loading seats: " + e.getMessage());
        }

        return "crud";
    }
} 