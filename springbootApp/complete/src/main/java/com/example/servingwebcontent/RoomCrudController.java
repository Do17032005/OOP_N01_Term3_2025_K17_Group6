package com.example.servingwebcontent;

import com.example.servingwebcontent.model.Room;
import com.example.servingwebcontent.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class RoomCrudController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/rooms")
    public String listRooms(Model model, 
                           @RequestParam(required = false) String keyword,
                           @RequestParam(defaultValue = "0") int page) {
        try {
            List<Room> rooms = roomService.getAllRooms();
            
            // Filter by keyword
            if (keyword != null && !keyword.trim().isEmpty()) {
                rooms = rooms.stream()
                    .filter(room -> room.getName().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
            }

            // Pagination
            int pageSize = 10;
            int totalPages = (int) Math.ceil((double) rooms.size() / pageSize);
            int start = page * pageSize;
            int end = Math.min(start + pageSize, rooms.size());
            
            List<Room> pagedRooms = rooms.subList(start, end);

            // Prepare model data
            model.addAttribute("items", pagedRooms);
            model.addAttribute("title", "Rooms");
            model.addAttribute("icon", "bi bi-building");
            model.addAttribute("addUrl", "/rooms/add");
            model.addAttribute("listUrl", "/rooms");
            model.addAttribute("searchUrl", "/rooms");
            model.addAttribute("viewUrl", "/rooms/view");
            model.addAttribute("editUrl", "/rooms/edit");
            model.addAttribute("deleteUrl", "/rooms/delete");
            model.addAttribute("keyword", keyword);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);

            // Define columns
            List<Map<String, Object>> columns = Arrays.asList(
                Map.of("field", "name", "label", "Name", "type", ""),
                Map.of("field", "capacity", "label", "Capacity", "type", ""),
                Map.of("field", "description", "label", "Description", "type", "")
            );
            model.addAttribute("columns", columns);

        } catch (Exception e) {
            model.addAttribute("error", "Error loading rooms: " + e.getMessage());
        }

        return "crud";
    }
} 