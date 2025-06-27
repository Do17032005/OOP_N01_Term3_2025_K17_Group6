package com.example.servingwebcontent.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserList {
    private List<User> users = new ArrayList<>();

    public List<User> getAll() {
        return new ArrayList<>(users);
    }

    public Optional<User> findById(Long id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public void add(User user) {
        users.add(user);
    }

    public boolean remove(Long id) {
        return users.removeIf(u -> u.getId().equals(id));
    }

    public boolean update(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                return true;
            }
        }
        return false;
    }
} 