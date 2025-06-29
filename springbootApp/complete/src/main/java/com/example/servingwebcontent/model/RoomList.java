package com.example.servingwebcontent.model;
import java.util.ArrayList;

public class RoomList {
    public ArrayList<Room> rooms = new ArrayList<>();

    public ArrayList<Room> addRoom(Room room) {
        rooms.add(room);
        return rooms;
    }

    public ArrayList<Room> getEditRoomById(String id, String name) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getId().equals(id)) {
                rooms.get(i).setName(name);
                break;
            }
        }
        return rooms;
    }

    public ArrayList<Room> getDeleteRoom(String id) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getId().equals(id)) {
                rooms.remove(i);
                break;
            }
        }
        return rooms;
    }

    public void printRoomList() {
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println(rooms.get(i));
        }
    }
}
