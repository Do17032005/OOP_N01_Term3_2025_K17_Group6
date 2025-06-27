package com.example.servingwebcontent.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketList {
    private List<Ticket> tickets = new ArrayList<>();

    public List<Ticket> getAll() {
        return new ArrayList<>(tickets);
    }

    public Optional<Ticket> findById(Long id) {
        return tickets.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public void add(Ticket ticket) {
        tickets.add(ticket);
    }

    public boolean remove(Long id) {
        return tickets.removeIf(t -> t.getId().equals(id));
    }

    public boolean update(Ticket ticket) {
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getId().equals(ticket.getId())) {
                tickets.set(i, ticket);
                return true;
            }
        }
        return false;
    }
} 