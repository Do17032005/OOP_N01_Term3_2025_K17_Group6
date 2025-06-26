package com.example.servingwebcontent;

import com.example.servingwebcontent.database.*;
import com.example.servingwebcontent.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/booking")
public class BookingController {
    private final MovieDAO movieDAO = new MovieDAO();
    private final ShowtimeDAO showtimeDAO = new ShowtimeDAO();
    private final SeatDAO seatDAO = new SeatDAO();
    private final TicketDAO ticketDAO = new TicketDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    @GetMapping("/movies")
    public String selectMovie(Model model, HttpSession session) {
        model.addAttribute("movies", movieDAO.getAllMovies());
        model.addAttribute("loggedInCustomer", session.getAttribute("loggedInCustomer"));
        return "booking-movies";
    }

    @GetMapping("/showtimes")
    public String selectShowtime(@RequestParam String movieId, Model model, HttpSession session) {
        List<Showtime> showtimes = showtimeDAO.getAllShowtimes().stream()
            .filter(s -> s.getMovieId().equals(movieId)).collect(Collectors.toList());
        model.addAttribute("showtimes", showtimes);
        model.addAttribute("movieId", movieId);
        model.addAttribute("loggedInCustomer", session.getAttribute("loggedInCustomer"));
        return "booking-showtimes";
    }

    @GetMapping("/seats")
    public String selectSeat(@RequestParam String showtimeId, Model model, HttpSession session) {
        List<Seat> allSeats = seatDAO.getAllSeats();
        List<Ticket> tickets = ticketDAO.getAllTickets().stream()
            .filter(t -> t.getShowtimeId().equals(showtimeId)).collect(Collectors.toList());
        Set<String> bookedSeatIds = tickets.stream().map(Ticket::getSeatId).collect(Collectors.toSet());
        List<Seat> availableSeats = allSeats.stream()
            .filter(seat -> !bookedSeatIds.contains(seat.getId())).collect(Collectors.toList());
        model.addAttribute("seats", availableSeats);
        model.addAttribute("showtimeId", showtimeId);
        model.addAttribute("loggedInCustomer", session.getAttribute("loggedInCustomer"));
        return "booking-seats";
    }

    @GetMapping("/customer")
    public String enterCustomer(@RequestParam String showtimeId, @RequestParam String seatId, Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("showtimeId", showtimeId);
        model.addAttribute("seatId", seatId);
        return "booking-customer";
    }

    @PostMapping("/confirm")
    public String confirmBooking(@RequestParam String showtimeId, @RequestParam String seatId,
                                 @ModelAttribute Customer customer, Model model) {
        // Lưu customer nếu chưa có
        customerDAO.insertCustomer(customer);
        // Tạo ticket
        Ticket ticket = new Ticket(UUID.randomUUID().toString(), showtimeId, seatId, customer.getId(), 50000);
        ticketDAO.insertTicket(ticket);
        model.addAttribute("ticket", ticket);
        return "booking-success";
    }
} 