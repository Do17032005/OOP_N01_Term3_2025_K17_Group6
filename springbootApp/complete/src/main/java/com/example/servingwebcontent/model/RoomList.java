package com.example.servingwebcontent.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomList {
    private List<Room> rooms = new ArrayList<>();

    public List<Room> getAll() {
        return new ArrayList<>(rooms);
    }

    public Optional<Room> findById(Long id) {
        return rooms.stream().filter(r -> r.getId().equals(id)).findFirst();
    }

    public void add(Room room) {
        rooms.add(room);
    }

    public boolean remove(Long id) {
        return rooms.removeIf(r -> r.getId().equals(id));
    }

    public boolean update(Room room) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getId().equals(room.getId())) {
                rooms.set(i, room);
                return true;
            }
        }
        return false;
    }
} 