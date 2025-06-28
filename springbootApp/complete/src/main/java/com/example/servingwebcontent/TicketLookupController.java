package com.example.servingwebcontent;

import com.example.servingwebcontent.database.TicketDAO;
import com.example.servingwebcontent.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TicketLookupController {
    
    private final TicketDAO ticketDAO;
    
    @Autowired
    public TicketLookupController(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    // === TICKET LOOKUP ===
    
    @GetMapping("/tickets/lookup")
    public String showLookupForm() {
        return "ticket/lookup";
    }

    @PostMapping("/tickets/lookup")
    public String lookupTicket(@RequestParam String ticketId, Model model) {
        try {
            if (ticketId == null || ticketId.trim().isEmpty()) {
                model.addAttribute("error", "Vui lòng nhập mã vé!");
                return "ticket/lookup";
            }
            
            Ticket ticket = ticketDAO.findTicketById(ticketId);
            model.addAttribute("ticket", ticket);
            
            if (ticket == null) {
                model.addAttribute("error", "Không tìm thấy vé với mã: " + ticketId);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Tra cứu vé thất bại: " + e.getMessage());
        }
        return "ticket/lookup-result";
    }

    // === TICKET PRINTING ===
    
    @GetMapping("/tickets/print/{id}")
    public String printTicket(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (id == null || id.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Mã vé không hợp lệ!");
                return "redirect:/tickets/lookup";
            }
            
            Ticket ticket = ticketDAO.findTicketById(id);
            if (ticket == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy vé để in!");
                return "redirect:/tickets/lookup";
            }
            
            model.addAttribute("ticket", ticket);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "In vé thất bại: " + e.getMessage());
            return "redirect:/tickets/lookup";
        }
        return "ticket/print";
    }
} 