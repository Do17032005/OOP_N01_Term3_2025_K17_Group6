package com.example.servingwebcontent;

import com.example.servingwebcontent.database.SeatDAO;
import com.example.servingwebcontent.database.RoomDAO;
import com.example.servingwebcontent.model.Seat;
import com.example.servingwebcontent.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/seat")
public class SeatController {
    
    private final SeatDAO seatDAO;
    private final RoomDAO roomDAO;
    
    @Autowired
    public SeatController(SeatDAO seatDAO, RoomDAO roomDAO) {
        this.seatDAO = seatDAO;
        this.roomDAO = roomDAO;
    }

    // === LIST SEATS ===
    @GetMapping("/list")
    public String listSeats(Model model) {
        try {
            List<Seat> seats = seatDAO.getAllSeats();
            model.addAttribute("seats", seats);
            return "seat/list";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải danh sách ghế: " + e.getMessage());
            return "seat/list";
        }
    }

    // === ADD SEAT ===
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("seat", new Seat());
        model.addAttribute("rooms", roomDAO.getAllRooms());
        return "seat/add";
    }

    @PostMapping("/add")
    public String addSeat(@ModelAttribute Seat seat, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (seat.getId() == null || seat.getId().trim().isEmpty()) {
                seat.setId(java.util.UUID.randomUUID().toString());
            }
            seatDAO.insertSeat(seat);
            redirectAttributes.addFlashAttribute("success", "Thêm ghế thành công!");
            return "redirect:/seat/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Thêm ghế thất bại: " + e.getMessage());
            model.addAttribute("seat", seat);
            model.addAttribute("rooms", roomDAO.getAllRooms());
            return "seat/add";
        }
    }
    
    // === EDIT SEAT ===
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Seat seat = seatDAO.getSeatById(id);
            if (seat == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy ghế!");
                return "redirect:/seat/list";
            }
            model.addAttribute("seat", seat);
            model.addAttribute("rooms", roomDAO.getAllRooms());
            return "seat/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể tải thông tin ghế: " + e.getMessage());
            return "redirect:/seat/list";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateSeat(@PathVariable String id, @ModelAttribute Seat seat, Model model, RedirectAttributes redirectAttributes) {
        try {
            seat.setId(id);
            seatDAO.updateSeat(seat);
            redirectAttributes.addFlashAttribute("success", "Cập nhật ghế thành công!");
            return "redirect:/seat/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Cập nhật ghế thất bại: " + e.getMessage());
            model.addAttribute("seat", seat);
            model.addAttribute("rooms", roomDAO.getAllRooms());
            return "seat/edit";
        }
    }

    // === DELETE SEAT ===
    @GetMapping("/delete/{id}")
    public String showDeleteConfirmation(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Seat seat = seatDAO.getSeatById(id);
            if (seat == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy ghế!");
                return "redirect:/seat/list";
            }
            model.addAttribute("seat", seat);
            return "seat/delete";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể tải thông tin ghế: " + e.getMessage());
            return "redirect:/seat/list";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteSeat(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            seatDAO.deleteSeat(id);
            redirectAttributes.addFlashAttribute("success", "Xóa ghế thành công!");
            return "redirect:/seat/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Xóa ghế thất bại: " + e.getMessage());
            return "redirect:/seat/list";
        }
    }
} 