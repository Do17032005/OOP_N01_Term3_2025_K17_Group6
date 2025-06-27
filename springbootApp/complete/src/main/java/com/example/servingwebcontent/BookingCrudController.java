package com.example.servingwebcontent;

import com.example.servingwebcontent.model.Booking;
import com.example.servingwebcontent.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class BookingCrudController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/bookings")
    public String listBookings(Model model, 
                              @RequestParam(required = false) String keyword,
                              @RequestParam(required = false) String status,
                              @RequestParam(defaultValue = "0") int page) {
        try {
            List<Booking> bookings = bookingService.getAllBookings();
            
            // Filter by keyword
            if (keyword != null && !keyword.trim().isEmpty()) {
                bookings = bookings.stream()
                    .filter(booking -> booking.getBookingNumber().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
            }
            
            // Filter by status
            if (status != null && !status.trim().isEmpty()) {
                bookings = bookings.stream()
                    .filter(booking -> booking.getStatus().toString().equalsIgnoreCase(status))
                    .toList();
            }

            // Pagination
            int pageSize = 10;
            int totalPages = (int) Math.ceil((double) bookings.size() / pageSize);
            int start = page * pageSize;
            int end = Math.min(start + pageSize, bookings.size());
            
            List<Booking> pagedBookings = bookings.subList(start, end);

            // Prepare model data
            model.addAttribute("items", pagedBookings);
            model.addAttribute("title", "Bookings");
            model.addAttribute("icon", "bi bi-ticket-perforated");
            model.addAttribute("addUrl", "/bookings/add");
            model.addAttribute("listUrl", "/bookings");
            model.addAttribute("searchUrl", "/bookings");
            model.addAttribute("viewUrl", "/bookings/view");
            model.addAttribute("editUrl", "/bookings/edit");
            model.addAttribute("deleteUrl", "/bookings/delete");
            model.addAttribute("keyword", keyword);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);

            // Define columns
            List<Map<String, Object>> columns = Arrays.asList(
                Map.of("field", "bookingNumber", "label", "Booking #", "type", ""),
                Map.of("field", "user.fullName", "label", "Customer", "type", ""),
                Map.of("field", "showtime.movie.title", "label", "Movie", "type", ""),
                Map.of("field", "bookingDate", "label", "Booking Date", "type", "date"),
                Map.of("field", "totalAmount", "label", "Total Amount", "type", "currency"),
                Map.of("field", "status", "label", "Status", "type", "status")
            );
            model.addAttribute("columns", columns);

            // Filter options
            List<Map<String, String>> filterOptions = Arrays.asList(
                Map.of("value", "PENDING", "label", "Pending"),
                Map.of("value", "CONFIRMED", "label", "Confirmed"),
                Map.of("value", "CANCELLED", "label", "Cancelled"),
                Map.of("value", "COMPLETED", "label", "Completed")
            );
            model.addAttribute("filterOptions", filterOptions);
            model.addAttribute("selectedFilter", status);

        } catch (Exception e) {
            model.addAttribute("error", "Error loading bookings: " + e.getMessage());
        }

        return "crud";
    }
} 