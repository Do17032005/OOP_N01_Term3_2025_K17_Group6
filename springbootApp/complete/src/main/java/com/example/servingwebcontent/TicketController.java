package com.example.servingwebcontent;

import com.example.servingwebcontent.database.TicketDAO;
import com.example.servingwebcontent.model.Ticket;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TicketController {
    private final TicketDAO ticketDAO = new TicketDAO();

    @GetMapping("/tickets")
    public String getAllTickets(Model model) {
        List<Ticket> tickets = ticketDAO.getAllTickets();
        model.addAttribute("tickets", tickets);
        return "ticket-list";
    }

    @GetMapping("/tickets/add")
    public String showAddForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "add-ticket";
    }

    @PostMapping("/tickets/add")
    public String addTicket(@ModelAttribute Ticket ticket) {
        ticketDAO.insertTicket(ticket);
        return "redirect:/tickets";
    }

    @GetMapping("/tickets/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        List<Ticket> tickets = ticketDAO.getAllTickets();
        Ticket ticket = tickets.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
        model.addAttribute("ticket", ticket);
        return "edit-ticket";
    }

    @PostMapping("/tickets/edit")
    public String editTicket(@ModelAttribute Ticket ticket) {
        ticketDAO.updateTicket(ticket);
        return "redirect:/tickets";
    }

    @GetMapping("/tickets/delete/{id}")
    public String deleteTicket(@PathVariable String id) {
        ticketDAO.deleteTicket(id);
        return "redirect:/tickets";
    }
}
