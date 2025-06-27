package com.example.servingwebcontent;

import com.example.servingwebcontent.model.Ticket;
import com.example.servingwebcontent.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class TicketCrudController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/tickets")
    public String listTickets(Model model, 
                            @RequestParam(required = false) String keyword,
                            @RequestParam(required = false) String status,
                            @RequestParam(defaultValue = "0") int page) {
        try {
            List<Ticket> tickets = ticketService.getAllTickets();
            
            // Filter by keyword
            if (keyword != null && !keyword.trim().isEmpty()) {
                tickets = tickets.stream()
                    .filter(ticket -> ticket.getTicketNumber().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
            }
            
            // Filter by status
            if (status != null && !status.trim().isEmpty()) {
                tickets = tickets.stream()
                    .filter(ticket -> ticket.getStatus().toString().equalsIgnoreCase(status))
                    .toList();
            }

            // Pagination
            int pageSize = 10;
            int totalPages = (int) Math.ceil((double) tickets.size() / pageSize);
            int start = page * pageSize;
            int end = Math.min(start + pageSize, tickets.size());
            
            List<Ticket> pagedTickets = tickets.subList(start, end);

            // Prepare model data
            model.addAttribute("items", pagedTickets);
            model.addAttribute("title", "Tickets");
            model.addAttribute("icon", "bi bi-receipt");
            model.addAttribute("addUrl", "/tickets/add");
            model.addAttribute("listUrl", "/tickets");
            model.addAttribute("searchUrl", "/tickets");
            model.addAttribute("viewUrl", "/tickets/view");
            model.addAttribute("editUrl", "/tickets/edit");
            model.addAttribute("deleteUrl", "/tickets/delete");
            model.addAttribute("keyword", keyword);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);

            // Define columns
            List<Map<String, Object>> columns = Arrays.asList(
                Map.of("field", "ticketNumber", "label", "Ticket #", "type", ""),
                Map.of("field", "user.fullName", "label", "Customer", "type", ""),
                Map.of("field", "seat.seatNumber", "label", "Seat", "type", ""),
                Map.of("field", "showtime.movie.title", "label", "Movie", "type", ""),
                Map.of("field", "price", "label", "Price", "type", "currency"),
                Map.of("field", "status", "label", "Status", "type", "status")
            );
            model.addAttribute("columns", columns);

            // Filter options
            List<Map<String, String>> filterOptions = Arrays.asList(
                Map.of("value", "PENDING", "label", "Pending"),
                Map.of("value", "CONFIRMED", "label", "Confirmed"),
                Map.of("value", "CANCELLED", "label", "Cancelled"),
                Map.of("value", "REFUNDED", "label", "Refunded")
            );
            model.addAttribute("filterOptions", filterOptions);
            model.addAttribute("selectedFilter", status);

        } catch (Exception e) {
            model.addAttribute("error", "Error loading tickets: " + e.getMessage());
        }

        return "crud";
    }
} 