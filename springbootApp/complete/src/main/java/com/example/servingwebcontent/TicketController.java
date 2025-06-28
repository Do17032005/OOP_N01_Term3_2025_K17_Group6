package com.example.servingwebcontent;

import com.example.servingwebcontent.database.TicketDAO;
import com.example.servingwebcontent.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TicketController {
    
    private final TicketDAO ticketDAO;
    
    @Autowired
    public TicketController(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    // === TICKET LISTING ===
    
    @GetMapping("/tickets")
    public String getAllTickets(Model model) {
        try {
            List<Ticket> tickets = ticketDAO.getAllTickets();
            model.addAttribute("tickets", tickets);
            return "ticket/list";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải danh sách vé: " + e.getMessage());
            return "ticket/list";
        }
    }

    // === TICKET CREATION ===
    
    @GetMapping("/tickets/add")
    public String showAddForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "ticket/add";
    }

    @PostMapping("/tickets/add")
    public String addTicket(@ModelAttribute Ticket ticket, RedirectAttributes redirectAttributes) {
        try {
            if (!isValidTicket(ticket)) {
                redirectAttributes.addFlashAttribute("error", "Thông tin vé không hợp lệ!");
                return "redirect:/tickets/add";
            }
            
            ticketDAO.insertTicket(ticket);
            redirectAttributes.addFlashAttribute("success", "Thêm vé thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thêm vé thất bại: " + e.getMessage());
        }
        return "redirect:/tickets";
    }

    // === TICKET EDITING ===
    
    @GetMapping("/tickets/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Ticket ticket = findTicketById(id);
            if (ticket == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy vé với ID: " + id);
                return "redirect:/tickets";
            }
            model.addAttribute("ticket", ticket);
            return "ticket/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể tải thông tin vé: " + e.getMessage());
            return "redirect:/tickets";
        }
    }

    @PostMapping("/tickets/edit")
    public String editTicket(@ModelAttribute Ticket ticket, RedirectAttributes redirectAttributes) {
        try {
            if (!isValidTicket(ticket)) {
                redirectAttributes.addFlashAttribute("error", "Thông tin vé không hợp lệ!");
                return "redirect:/tickets/edit/" + ticket.getId();
            }
            
            ticketDAO.updateTicket(ticket);
            redirectAttributes.addFlashAttribute("success", "Cập nhật vé thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật vé thất bại: " + e.getMessage());
        }
        return "redirect:/tickets";
    }

    // === TICKET DELETION ===
    
    @GetMapping("/tickets/delete/{id}")
    public String deleteTicket(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            Ticket ticket = findTicketById(id);
            if (ticket == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy vé để xóa!");
                return "redirect:/tickets";
            }
            
            ticketDAO.deleteTicket(id);
            redirectAttributes.addFlashAttribute("success", "Xóa vé thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Xóa vé thất bại: " + e.getMessage());
        }
        return "redirect:/tickets";
    }

    // === PRIVATE HELPER METHODS ===
    
    private Ticket findTicketById(String id) {
        return ticketDAO.getAllTickets().stream()
            .filter(t -> t.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    private boolean isValidTicket(Ticket ticket) {
        return ticket != null && 
               ticket.getShowtimeId() != null && !ticket.getShowtimeId().trim().isEmpty() &&
               ticket.getSeatId() != null && !ticket.getSeatId().trim().isEmpty() &&
               ticket.getCustomerId() != null && !ticket.getCustomerId().trim().isEmpty() &&
               ticket.getPrice() > 0;
    }
}
