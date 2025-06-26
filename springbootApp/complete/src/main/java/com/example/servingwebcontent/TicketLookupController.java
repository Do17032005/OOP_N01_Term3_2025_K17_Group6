package com.example.servingwebcontent;

import com.example.servingwebcontent.database.TicketDAO;
import com.example.servingwebcontent.model.Ticket;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TicketLookupController {
    private final TicketDAO ticketDAO = new TicketDAO();

    @GetMapping("/tickets/lookup")
    public String showLookupForm() {
        return "ticket-lookup";
    }

    @PostMapping("/tickets/lookup")
    public String lookupTicket(@RequestParam String ticketId, Model model) {
        Ticket ticket = ticketDAO.findTicketById(ticketId);
        model.addAttribute("ticket", ticket);
        return "ticket-lookup-result";
    }

    @GetMapping("/tickets/print/{id}")
    public String printTicket(@PathVariable String id, Model model) {
        Ticket ticket = ticketDAO.findTicketById(id);
        model.addAttribute("ticket", ticket);
        return "ticket-print";
    }
} 