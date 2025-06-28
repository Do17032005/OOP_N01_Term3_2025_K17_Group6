package com.example.servingwebcontent;

import com.example.servingwebcontent.database.RoomDAO;
import com.example.servingwebcontent.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RoomController {
    
    private final RoomDAO roomDAO;
    
    @Autowired
    public RoomController(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    // === ROOM LISTING ===
    
    @GetMapping("/rooms")
    public String getAllRooms(Model model) {
        try {
            List<Room> rooms = roomDAO.getAllRooms();
            model.addAttribute("rooms", rooms);
            return "room/list";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải danh sách phòng: " + e.getMessage());
            return "room/list";
        }
    }

    // === ROOM CREATION ===
    
    @GetMapping("/rooms/add")
    public String showAddForm(Model model) {
        model.addAttribute("room", new Room());
        return "room/add";
    }

    @PostMapping("/rooms/add")
    public String addRoom(@ModelAttribute Room room, RedirectAttributes redirectAttributes) {
        try {
            if (!isValidRoom(room)) {
                redirectAttributes.addFlashAttribute("error", "Thông tin phòng không hợp lệ!");
                return "redirect:/rooms/add";
            }
            
            roomDAO.insertRoom(room);
            redirectAttributes.addFlashAttribute("success", "Thêm phòng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thêm phòng thất bại: " + e.getMessage());
        }
        return "redirect:/rooms";
    }

    // === ROOM EDITING ===
    
    @GetMapping("/rooms/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Room room = findRoomById(id);
            if (room == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy phòng với ID: " + id);
                return "redirect:/rooms";
            }
            model.addAttribute("room", room);
            return "room/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể tải thông tin phòng: " + e.getMessage());
            return "redirect:/rooms";
        }
    }

    @PostMapping("/rooms/edit")
    public String editRoom(@ModelAttribute Room room, RedirectAttributes redirectAttributes) {
        try {
            if (!isValidRoom(room)) {
                redirectAttributes.addFlashAttribute("error", "Thông tin phòng không hợp lệ!");
                return "redirect:/rooms/edit/" + room.getId();
            }
            
            roomDAO.updateRoom(room);
            redirectAttributes.addFlashAttribute("success", "Cập nhật phòng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật phòng thất bại: " + e.getMessage());
        }
        return "redirect:/rooms";
    }

    // === ROOM DELETION ===
    
    @GetMapping("/rooms/delete/{id}")
    public String deleteRoom(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            Room room = findRoomById(id);
            if (room == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy phòng để xóa!");
                return "redirect:/rooms";
            }
            
            roomDAO.deleteRoom(id);
            redirectAttributes.addFlashAttribute("success", "Xóa phòng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Xóa phòng thất bại: " + e.getMessage());
        }
        return "redirect:/rooms";
    }

    // === PRIVATE HELPER METHODS ===
    
    private Room findRoomById(String id) {
        return roomDAO.getAllRooms().stream()
            .filter(r -> r.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    private boolean isValidRoom(Room room) {
        return room != null && 
               room.getName() != null && !room.getName().trim().isEmpty() &&
               room.getTotalSeats() > 0;
    }
} 