package com.example.servingwebcontent.model;
import java.util.ArrayList;
import java.util.List;


public class TicketList {
    ArrayList<Ticket> ticket = new ArrayList<>(); // Danh sách vé

    public ArrayList<Ticket> addTicket(Ticket tickets) {
        ticket.add(tickets); // Thêm vé vào danh sách
        return ticket;
    }

    // In danh sách vé của một khách hàng theo ID
    public void printTicketsByCustomerId(String customerId, List<Showtime> showtimes, List<Movie> movies, List<Seat> seats) {
        System.out.println("--- Danh sách vé theo ID khách hàng: " + customerId + " ---");
        boolean found = false;
        for (Ticket t : ticket) {
            if (t.getCustomerId().equals(customerId)) {
                printTicketDetail(t, showtimes, movies, seats);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Không tìm thấy vé nào cho khách hàng này.");
        }
    }

    // Thống kê số lượng vé đã đặt
    public int getTicketCount() {
        return ticket.size();
    }
    // Lấy tất cả vé
    public List<Ticket> getAllTickets() {
        return ticket;
    }
    // Tìm vé theo id
    public Ticket findTicketById(String id) {
        for (Ticket t : ticket) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }
    // Cập nhật vé theo id
    public boolean updateTicketById(String id, Ticket newTicket) {
        for (int i = 0; i < ticket.size(); i++) {
            if (ticket.get(i).getId().equals(id)) {
                ticket.set(i, newTicket);
                return true;
            }
        }
        return false;
    }
    // Xóa vé theo id
    public boolean removeTicketById(String id) {
        return ticket.removeIf(t -> t.getId().equals(id));
    }
    // In danh sách vé (cần truyền vào các list liên quan)
    public void printTicketList(List<Showtime> showtimes, List<Movie> movies, List<Seat> seats, List<Customer> customers) {
        int len = ticket.size();
        for (int i=0; i < len; i++) {
            printTicketDetail(ticket.get(i), showtimes, movies, seats, customers);
        }
    }
    // In chi tiết 1 vé
    public void printTicketDetail(Ticket t, List<Showtime> showtimes, List<Movie> movies, List<Seat> seats) {
        printTicketDetail(t, showtimes, movies, seats, null);
    }
    public void printTicketDetail(Ticket t, List<Showtime> showtimes, List<Movie> movies, List<Seat> seats, List<Customer> customers) {
        System.out.println("Ticket ID: " + t.getId());
        Showtime st = showtimes.stream().filter(s -> s.getId().equals(t.getShowtimeId())).findFirst().orElse(null);
        if (st != null) {
            Movie m = movies.stream().filter(mv -> mv.getId().equals(st.getMovieId())).findFirst().orElse(null);
            if (m != null) {
                System.out.println("Movie: " + m.getName());
                System.out.println("Show Time: " + st.getStartTime());
            }
        }
        Seat seat = seats.stream().filter(s -> s.getId().equals(t.getSeatId())).findFirst().orElse(null);
        if (seat != null) {
            System.out.println("Seat: " + seat.getSeatNumber());
        }
        if (customers != null) {
            Customer c = customers.stream().filter(cus -> cus.getId().equals(t.getCustomerId())).findFirst().orElse(null);
            if (c != null) {
                System.out.println("Customer: " + c.getName());
            }
        }
        System.out.println("Price: " + t.getPrice());
        System.out.println("-------------------------");
    }
}