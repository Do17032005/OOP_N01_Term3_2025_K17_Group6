package com.example.servingwebcontent;

import com.example.servingwebcontent.database.RoomDAO;
import com.example.servingwebcontent.model.Room;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RoomController {
    private final RoomDAO roomDAO = new RoomDAO();

    @GetMapping("/rooms")
    public String getAllRooms(Model model) {
        List<Room> rooms = roomDAO.getAllRooms();
        model.addAttribute("rooms", rooms);
        return "room-list";
    }

    @GetMapping("/rooms/add")
    public String showAddForm(Model model) {
        model.addAttribute("room", new Room());
        return "add-room";
    }

    @PostMapping("/rooms/add")
    public String addRoom(@ModelAttribute Room room) {
        roomDAO.insertRoom(room);
        return "redirect:/rooms";
    }

    @GetMapping("/rooms/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        List<Room> rooms = roomDAO.getAllRooms();
        Room room = rooms.stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
        model.addAttribute("room", room);
        return "edit-room";
    }

    @PostMapping("/rooms/edit")
    public String editRoom(@ModelAttribute Room room) {
        roomDAO.updateRoom(room);
        return "redirect:/rooms";
    }

    @GetMapping("/rooms/delete/{id}")
    public String deleteRoom(@PathVariable String id) {
        roomDAO.deleteRoom(id);
        return "redirect:/rooms";
    }
} 