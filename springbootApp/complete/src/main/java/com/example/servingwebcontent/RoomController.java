package com.example.servingwebcontent;

import com.example.servingwebcontent.database.RoomDAO;
import com.example.servingwebcontent.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/room")
public class RoomController {
    
    private final RoomDAO roomDAO;

    @Autowired
    public RoomController(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    // === LIST ROOMS ===
    @GetMapping("/list")
    public String listRoom(Model model) {
        try {
            List<Room> rooms = roomDAO.getAllRooms();
            model.addAttribute("rooms", rooms);
            return "room/list";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải danh sách phòng: " + e.getMessage());
            return "room/list";
        }
    }

    // === SHOW ADD FORM ===
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("room", new Room());
        return "room/add";
    }

    // === ADD ROOM ===
    @PostMapping("/add")
    public String addRoom(@ModelAttribute Room room, 
                         Model model, 
                         RedirectAttributes redirectAttributes) {
        try {
            // Validation using ValidationUtils
            ValidationUtils.validateRoom(room.getName(), room.getTotalSeats());
            
            // Generate ID if not provided
            if (ValidationUtils.isEmpty(room.getId())) {
                room.setId(UUID.randomUUID().toString());
            }
            
            roomDAO.insertRoom(room);
            redirectAttributes.addFlashAttribute("success", "Thêm phòng thành công!");
            return "redirect:/room/list";
            
        } catch (IllegalArgumentException e) {
            // Validation error
            model.addAttribute("error", e.getMessage());
            model.addAttribute("room", room);
            return "room/add";
        } catch (Exception e) {
            // System error
            model.addAttribute("error", "Lỗi hệ thống khi thêm phòng: " + e.getMessage());
            model.addAttribute("room", room);
            return "room/add";
        }
    }

    // === SHOW EDIT FORM ===
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                redirectAttributes.addFlashAttribute("error", "ID phòng không hợp lệ!");
                return "redirect:/room/list";
            }
            
            // Find room by ID
            Room room = findRoomById(id);
            if (room == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy phòng với ID: " + id);
                return "redirect:/room/list";
            }
            
            model.addAttribute("room", room);
            return "room/edit";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            return "redirect:/room/list";
        }
    }

    // === UPDATE ROOM ===
    @PostMapping("/edit/{id}")
    public String updateRoom(@PathVariable String id, 
                            @ModelAttribute Room room, 
                            Model model, 
                            RedirectAttributes redirectAttributes) {
        try {
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                redirectAttributes.addFlashAttribute("error", "ID phòng không hợp lệ!");
                return "redirect:/room/list";
            }
            
            // Validate room data
            ValidationUtils.validateRoom(room.getName(), room.getTotalSeats());
            
            // Set ID from path
            room.setId(id);
            
            // Check if room exists
            Room existingRoom = findRoomById(id);
            if (existingRoom == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy phòng để cập nhật!");
                return "redirect:/room/list";
            }
            
            roomDAO.updateRoom(room);
            redirectAttributes.addFlashAttribute("success", "Cập nhật phòng thành công!");
            return "redirect:/room/list";
            
        } catch (IllegalArgumentException e) {
            // Validation error
            model.addAttribute("error", e.getMessage());
            model.addAttribute("room", room);
            return "room/edit";
        } catch (Exception e) {
            // System error
            model.addAttribute("error", "Lỗi hệ thống khi cập nhật phòng: " + e.getMessage());
            model.addAttribute("room", room);
            return "room/edit";
        }
    }

    // === DELETE ROOM ===
    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                redirectAttributes.addFlashAttribute("error", "ID phòng không hợp lệ!");
                return "redirect:/room/list";
            }
            
            // Check if room exists
            Room existingRoom = findRoomById(id);
            if (existingRoom == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy phòng để xóa!");
                return "redirect:/room/list";
            }
            
            roomDAO.deleteRoom(id);
            redirectAttributes.addFlashAttribute("success", "Xóa phòng thành công!");
            return "redirect:/room/list";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống khi xóa phòng: " + e.getMessage());
            return "redirect:/room/list";
        }
    }

    // === PRIVATE HELPER METHODS ===
    
    /**
     * Tìm room theo ID
     * Note: RoomDAO chưa có method getRoomById, nên phải filter từ getAllRooms
     */
    private Room findRoomById(String id) {
        try {
            List<Room> rooms = roomDAO.getAllRooms();
            return rooms.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm kiếm phòng: " + e.getMessage(), e);
        }
    }
}