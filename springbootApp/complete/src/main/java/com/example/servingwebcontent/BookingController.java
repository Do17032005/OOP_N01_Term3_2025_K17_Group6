package com.example.servingwebcontent;

import com.example.servingwebcontent.database.*;
import com.example.servingwebcontent.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/booking")
public class BookingController {
    
    private final MovieDAO movieDAO;
    private final ShowtimeDAO showtimeDAO;
    private final SeatDAO seatDAO;
    private final TicketDAO ticketDAO;
    private final CustomerDAO customerDAO;
    
    @Autowired
    public BookingController(MovieDAO movieDAO, ShowtimeDAO showtimeDAO, 
                           SeatDAO seatDAO, TicketDAO ticketDAO, CustomerDAO customerDAO) {
        this.movieDAO = movieDAO;
        this.showtimeDAO = showtimeDAO;
        this.seatDAO = seatDAO;
        this.ticketDAO = ticketDAO;
        this.customerDAO = customerDAO;
    }

    // === MOVIE SELECTION ===
    
    @GetMapping("/movies")
    public String selectMovie(Model model, HttpSession session) {
        try {
            List<Movie> movies = movieDAO.getAllMovies();
            if (movies.isEmpty()) {
                model.addAttribute("error", "Hiện tại không có phim nào để đặt vé!");
            }
            model.addAttribute("movies", movies);
            model.addAttribute("loggedInCustomer", session.getAttribute("loggedInCustomer"));
            return "booking/movies";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải danh sách phim: " + e.getMessage());
            return "booking/movies";
        }
    }

    // === SHOWTIME SELECTION ===
    
    @GetMapping("/showtimes")
    public String selectShowtime(@RequestParam String movieId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            if (movieId == null || movieId.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng chọn phim!");
                return "redirect:/booking/movies";
            }
            
            List<Showtime> showtimes = showtimeDAO.getAllShowtimes().stream()
                .filter(s -> s.getMovieId().equals(movieId))
                .collect(Collectors.toList());
                
            if (showtimes.isEmpty()) {
                model.addAttribute("error", "Không có suất chiếu nào cho phim này!");
            }
            
            model.addAttribute("showtimes", showtimes);
            model.addAttribute("movieId", movieId);
            model.addAttribute("loggedInCustomer", session.getAttribute("loggedInCustomer"));
            return "booking/showtimes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể tải suất chiếu: " + e.getMessage());
            return "redirect:/booking/movies";
        }
    }

    // === SEAT SELECTION ===
    
    @GetMapping("/seats")
    public String selectSeat(@RequestParam String showtimeId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            if (showtimeId == null || showtimeId.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng chọn suất chiếu!");
                return "redirect:/booking/movies";
            }
            
            List<Seat> allSeats = seatDAO.getAllSeats();
            List<Ticket> tickets = ticketDAO.getAllTickets().stream()
                .filter(t -> t.getShowtimeId().equals(showtimeId))
                .collect(Collectors.toList());
                
            Set<String> bookedSeatIds = tickets.stream()
                .map(Ticket::getSeatId)
                .collect(Collectors.toSet());
                
            List<Seat> availableSeats = allSeats.stream()
                .filter(seat -> !bookedSeatIds.contains(seat.getId()))
                .collect(Collectors.toList());
                
            if (availableSeats.isEmpty()) {
                model.addAttribute("error", "Không còn ghế trống cho suất chiếu này!");
            }
            
            model.addAttribute("seats", availableSeats);
            model.addAttribute("showtimeId", showtimeId);
            model.addAttribute("loggedInCustomer", session.getAttribute("loggedInCustomer"));
            return "booking/seats";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể tải danh sách ghế: " + e.getMessage());
            return "redirect:/booking/movies";
        }
    }

    // === CUSTOMER INFORMATION ===
    
    @GetMapping("/customer")
    public String enterCustomer(@RequestParam String showtimeId, @RequestParam String seatId, 
                               Model model, RedirectAttributes redirectAttributes) {
        try {
            if (showtimeId == null || showtimeId.trim().isEmpty() || 
                seatId == null || seatId.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Thông tin suất chiếu hoặc ghế không hợp lệ!");
                return "redirect:/booking/movies";
            }
            
            // Check if seat is still available
            if (!isSeatAvailable(showtimeId, seatId)) {
                redirectAttributes.addFlashAttribute("error", "Ghế này đã được đặt!");
                return "redirect:/booking/seats?showtimeId=" + showtimeId;
            }
            
            model.addAttribute("customer", new Customer());
            model.addAttribute("showtimeId", showtimeId);
            model.addAttribute("seatId", seatId);
            return "booking/customer";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể tải form thông tin: " + e.getMessage());
            return "redirect:/booking/movies";
        }
    }

    // === BOOKING CONFIRMATION ===
    
    @PostMapping("/confirm")
    public String confirmBooking(@RequestParam String showtimeId, @RequestParam String seatId,
                                 @ModelAttribute Customer customer, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (!isValidBookingData(showtimeId, seatId, customer)) {
                redirectAttributes.addFlashAttribute("error", "Thông tin đặt vé không hợp lệ!");
                return "redirect:/booking/customer?showtimeId=" + showtimeId + "&seatId=" + seatId;
            }
            
            // Check if seat is still available
            if (!isSeatAvailable(showtimeId, seatId)) {
                redirectAttributes.addFlashAttribute("error", "Ghế này đã được đặt!");
                return "redirect:/booking/seats?showtimeId=" + showtimeId;
            }
            
            // Save customer if not exists
            if (customer.getId() == null || customer.getId().trim().isEmpty()) {
                customer.setId(UUID.randomUUID().toString());
            }
            customerDAO.insertCustomer(customer);
            
            // Create ticket
            Ticket ticket = new Ticket(UUID.randomUUID().toString(), showtimeId, seatId, customer.getId(), 50000);
            ticketDAO.insertTicket(ticket);
            
            model.addAttribute("ticket", ticket);
            model.addAttribute("customer", customer);
            return "booking/success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đặt vé thất bại: " + e.getMessage());
            return "redirect:/booking/customer?showtimeId=" + showtimeId + "&seatId=" + seatId;
        }
    }

    // === PRIVATE HELPER METHODS ===
    
    private boolean isSeatAvailable(String showtimeId, String seatId) {
        return ticketDAO.getAllTickets().stream()
            .noneMatch(t -> t.getShowtimeId().equals(showtimeId) && t.getSeatId().equals(seatId));
    }
    
    private boolean isValidBookingData(String showtimeId, String seatId, Customer customer) {
        return showtimeId != null && !showtimeId.trim().isEmpty() &&
               seatId != null && !seatId.trim().isEmpty() &&
               customer != null &&
               customer.getName() != null && !customer.getName().trim().isEmpty() &&
               customer.getEmail() != null && !customer.getEmail().trim().isEmpty() &&
               customer.getPhoneNumber() != null && !customer.getPhoneNumber().trim().isEmpty();
    }
} 