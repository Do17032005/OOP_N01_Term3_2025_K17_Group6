package com.example.servingwebcontent;

import com.example.servingwebcontent.database.SeatDAO;
import com.example.servingwebcontent.model.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class SeatController {
    
    private final SeatDAO seatDAO;
    
    @Autowired
    public SeatController(SeatDAO seatDAO) {
        this.seatDAO = seatDAO;
    }

    // === SEAT LISTING ===
    
    @GetMapping("/seats")
    public String getAllSeats(Model model) {
        try {
            List<Seat> seats = seatDAO.getAllSeats();
            model.addAttribute("seats", seats);
            return "seat/list";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải danh sách ghế: " + e.getMessage());
            return "seat/list";
        }
    }

    // === SEAT CREATION ===
    
    @GetMapping("/seats/add")
    public String showAddForm(Model model) {
        model.addAttribute("seat", new Seat());
        return "seat/add";
    }

    @PostMapping("/seats/add")
    public String addSeat(@ModelAttribute Seat seat, RedirectAttributes redirectAttributes) {
        try {
            if (!isValidSeat(seat)) {
                redirectAttributes.addFlashAttribute("error", "Thông tin ghế không hợp lệ!");
                return "redirect:/seats/add";
            }
            
            seatDAO.insertSeat(seat);
            redirectAttributes.addFlashAttribute("success", "Thêm ghế thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thêm ghế thất bại: " + e.getMessage());
        }
        return "redirect:/seats";
    }

    // === SEAT EDITING ===
    
    @GetMapping("/seats/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Seat seat = findSeatById(id);
            if (seat == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy ghế với ID: " + id);
                return "redirect:/seats";
            }
            model.addAttribute("seat", seat);
            return "seat/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể tải thông tin ghế: " + e.getMessage());
            return "redirect:/seats";
        }
    }

    @PostMapping("/seats/edit")
    public String editSeat(@ModelAttribute Seat seat, RedirectAttributes redirectAttributes) {
        try {
            if (!isValidSeat(seat)) {
                redirectAttributes.addFlashAttribute("error", "Thông tin ghế không hợp lệ!");
                return "redirect:/seats/edit/" + seat.getId();
            }
            
            seatDAO.updateSeat(seat);
            redirectAttributes.addFlashAttribute("success", "Cập nhật ghế thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật ghế thất bại: " + e.getMessage());
        }
        return "redirect:/seats";
    }

    // === SEAT DELETION ===
    
    @GetMapping("/seats/delete/{id}")
    public String deleteSeat(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            Seat seat = findSeatById(id);
            if (seat == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy ghế để xóa!");
                return "redirect:/seats";
            }
            
            seatDAO.deleteSeat(id);
            redirectAttributes.addFlashAttribute("success", "Xóa ghế thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Xóa ghế thất bại: " + e.getMessage());
        }
        return "redirect:/seats";
    }

    // === PRIVATE HELPER METHODS ===
    
    private Seat findSeatById(String id) {
        return seatDAO.getAllSeats().stream()
            .filter(s -> s.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    private boolean isValidSeat(Seat seat) {
        return seat != null && 
               seat.getSeatNumber() != null && !seat.getSeatNumber().trim().isEmpty() &&
               seat.getRoomId() != null && !seat.getRoomId().trim().isEmpty();
    }
} 