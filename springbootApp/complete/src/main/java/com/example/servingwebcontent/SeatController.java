package com.example.servingwebcontent;

import com.example.servingwebcontent.database.SeatDAO;
import com.example.servingwebcontent.model.Seat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SeatController {
    private final SeatDAO seatDAO = new SeatDAO();

    @GetMapping("/seats")
    public String getAllSeats(Model model) {
        List<Seat> seats = seatDAO.getAllSeats();
        model.addAttribute("seats", seats);
        return "seat-list";
    }

    @GetMapping("/seats/add")
    public String showAddForm(Model model) {
        model.addAttribute("seat", new Seat());
        return "add-seat";
    }

    @PostMapping("/seats/add")
    public String addSeat(@ModelAttribute Seat seat) {
        seatDAO.insertSeat(seat);
        return "redirect:/seats";
    }

    @GetMapping("/seats/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        List<Seat> seats = seatDAO.getAllSeats();
        Seat seat = seats.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
        model.addAttribute("seat", seat);
        return "edit-seat";
    }

    @PostMapping("/seats/edit")
    public String editSeat(@ModelAttribute Seat seat) {
        seatDAO.updateSeat(seat);
        return "redirect:/seats";
    }

    @GetMapping("/seats/delete/{id}")
    public String deleteSeat(@PathVariable String id) {
        seatDAO.deleteSeat(id);
        return "redirect:/seats";
    }
} 