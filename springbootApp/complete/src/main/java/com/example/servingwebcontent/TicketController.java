package com.example.servingwebcontent;

import com.example.servingwebcontent.database.*;
import com.example.servingwebcontent.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/ticket")
public class TicketController {
    
    private final TicketDAO ticketDAO;
    private final CustomerDAO customerDAO;
    private final ShowtimeDAO showtimeDAO;
    private final MovieDAO movieDAO;
    private final SeatDAO seatDAO;
    private final RoomDAO roomDAO;
    
    @Autowired
    public TicketController(TicketDAO ticketDAO, CustomerDAO customerDAO, 
                           ShowtimeDAO showtimeDAO, MovieDAO movieDAO, 
                           SeatDAO seatDAO, RoomDAO roomDAO) {
        this.ticketDAO = ticketDAO;
        this.customerDAO = customerDAO;
        this.showtimeDAO = showtimeDAO;
        this.movieDAO = movieDAO;
        this.seatDAO = seatDAO;
        this.roomDAO = roomDAO;
    }

    // === PRINT TICKET ===
    @GetMapping("/print")
    public String printTicket(@RequestParam String ticketId, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Validate ticket ID
            if (ticketId == null || ticketId.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Mã vé không hợp lệ!");
                return "redirect:/ticket/lookup";
            }
            
            // Find ticket
            Ticket ticket = findTicketById(ticketId);
            if (ticket == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy vé với mã: " + ticketId);
                return "redirect:/ticket/lookup";
            }
            
            // Get related information
            Customer customer = findCustomerById(ticket.getCustomerId());
            Showtime showtime = findShowtimeById(ticket.getShowtimeId());
            Movie movie = null;
            Room room = null;
            Seat seat = findSeatById(ticket.getSeatId());
            
            if (showtime != null) {
                movie = findMovieById(showtime.getMovieId());
                room = findRoomById(showtime.getRoomId());
            }
            
            // Add all information to model
            model.addAttribute("ticket", ticket);
            model.addAttribute("customer", customer);
            model.addAttribute("showtime", showtime);
            model.addAttribute("movie", movie);
            model.addAttribute("seat", seat);
            model.addAttribute("room", room);
            
            return "ticket/print";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi in vé: " + e.getMessage());
            return "redirect:/ticket/lookup";
        }
    }

    // === LOOKUP TICKET ===
    @GetMapping("/lookup")
    public String showLookupForm() {
        return "ticket/lookup";
    }

    @PostMapping("/lookup")
    public String lookupTicket(@RequestParam String ticketId, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (ticketId == null || ticketId.trim().isEmpty()) {
                model.addAttribute("error", "Vui lòng nhập mã vé!");
                return "ticket/lookup";
            }
            
            Ticket ticket = findTicketById(ticketId);
            if (ticket == null) {
                model.addAttribute("error", "Không tìm thấy vé với mã: " + ticketId);
                return "ticket/lookup";
            }
            
            // Get related information
            Customer customer = findCustomerById(ticket.getCustomerId());
            Showtime showtime = findShowtimeById(ticket.getShowtimeId());
            Movie movie = null;
            Room room = null;
            Seat seat = findSeatById(ticket.getSeatId());
            
            if (showtime != null) {
                movie = findMovieById(showtime.getMovieId());
                room = findRoomById(showtime.getRoomId());
            }
            
            model.addAttribute("ticket", ticket);
            model.addAttribute("customer", customer);
            model.addAttribute("showtime", showtime);
            model.addAttribute("movie", movie);
            model.addAttribute("seat", seat);
            model.addAttribute("room", room);
            
            return "ticket/lookup-result";
            
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tra cứu vé: " + e.getMessage());
            return "ticket/lookup";
        }
    }

    // === LIST TICKETS ===
    @GetMapping("/list")
    public String listTicket(Model model) {
        try {
            List<Ticket> tickets = ticketDAO.getAllTickets();
            model.addAttribute("tickets", tickets);
            return "ticket/list";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải danh sách vé: " + e.getMessage());
            return "ticket/list";
        }
    }

    // === SHOW ADD FORM ===
    @GetMapping("/add")
    public String showAddForm(Model model) {
        // Debug: Kiểm tra dữ liệu có sẵn
        try {
            System.out.println("=== DEBUG: Kiểm tra dữ liệu có sẵn ===");
            List<Customer> customers = customerDAO.getAllCustomers();
            List<Showtime> showtimes = showtimeDAO.getAllShowtimes();
            List<Seat> seats = seatDAO.getAllSeats();
            
            System.out.println("Customers: " + customers.size());
            customers.forEach(c -> System.out.println("  - " + c.getId() + ": " + c.getName()));
            
            System.out.println("Showtimes: " + showtimes.size());
            showtimes.forEach(s -> System.out.println("  - " + s.getId() + ": " + s.getStartTime()));
            
            System.out.println("Seats: " + seats.size());
            seats.forEach(s -> System.out.println("  - " + s.getId() + ": " + s.getSeatNumber()));
            
        } catch (Exception e) {
            System.out.println("Error checking available data: " + e.getMessage());
        }
        
        model.addAttribute("ticket", new Ticket());
        return "ticket/add";
    }

    // === ADD TICKET ===
    @PostMapping("/add")
    public String addTicket(@ModelAttribute Ticket ticket, 
                           Model model, 
                           RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== DEBUG: Thêm vé ===");
            System.out.println("Ticket data: " + ticket);
            System.out.println("ShowtimeId: " + ticket.getShowtimeId());
            System.out.println("SeatId: " + ticket.getSeatId());
            System.out.println("CustomerId: " + ticket.getCustomerId());
            System.out.println("Price: " + ticket.getPrice());
            
            // Validation using ValidationUtils
            ValidationUtils.validateTicket(ticket.getShowtimeId(), ticket.getSeatId(), 
                ticket.getCustomerId(), ticket.getPrice());
            System.out.println("Validation passed");
            
            // Check if referenced entities exist
            Customer customer = findCustomerById(ticket.getCustomerId());
            if (customer == null) {
                throw new IllegalArgumentException("Không tìm thấy khách hàng với ID: " + ticket.getCustomerId());
            }
            System.out.println("Customer found: " + customer.getName());
            
            Showtime showtime = findShowtimeById(ticket.getShowtimeId());
            if (showtime == null) {
                throw new IllegalArgumentException("Không tìm thấy suất chiếu với ID: " + ticket.getShowtimeId());
            }
            System.out.println("Showtime found: " + showtime.getId());
            
            Seat seat = findSeatById(ticket.getSeatId());
            if (seat == null) {
                throw new IllegalArgumentException("Không tìm thấy ghế với ID: " + ticket.getSeatId());
            }
            System.out.println("Seat found: " + seat.getSeatNumber());
            
            // Generate ID if not provided
            if (ValidationUtils.isEmpty(ticket.getId())) {
                ticket.setId(UUID.randomUUID().toString());
                System.out.println("Generated new ID: " + ticket.getId());
            }
            
            System.out.println("Calling ticketDAO.insertTicket...");
            ticketDAO.insertTicket(ticket);
            System.out.println("Ticket inserted successfully");
            
            redirectAttributes.addFlashAttribute("success", "Thêm vé thành công!");
            return "redirect:/ticket/list";
            
        } catch (IllegalArgumentException e) {
            // Validation error
            System.out.println("Validation error: " + e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("ticket", ticket);
            return "ticket/add";
        } catch (Exception e) {
            // System error
            System.out.println("System error: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Lỗi hệ thống khi thêm vé: " + e.getMessage());
            model.addAttribute("ticket", ticket);
            return "ticket/add";
        }
    }

    // === SHOW EDIT FORM ===
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                redirectAttributes.addFlashAttribute("error", "ID vé không hợp lệ!");
                return "redirect:/ticket/list";
            }
            
            // Find ticket by ID
            Ticket ticket = findTicketById(id);
            if (ticket == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy vé với ID: " + id);
                return "redirect:/ticket/list";
            }
            
            model.addAttribute("ticket", ticket);
            return "ticket/edit";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            return "redirect:/ticket/list";
        }
    }

    // === UPDATE TICKET ===
    @PostMapping("/edit/{id}")
    public String updateTicket(@PathVariable String id, 
                              @ModelAttribute Ticket ticket, 
                              Model model, 
                              RedirectAttributes redirectAttributes) {
        try {
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                redirectAttributes.addFlashAttribute("error", "ID vé không hợp lệ!");
                return "redirect:/ticket/list";
            }
            
            // Validate ticket data
            ValidationUtils.validateTicket(ticket.getShowtimeId(), ticket.getSeatId(), 
                ticket.getCustomerId(), ticket.getPrice());
            
            // Set ID from path
            ticket.setId(id);
            
            // Check if ticket exists
            Ticket existingTicket = findTicketById(id);
            if (existingTicket == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy vé để cập nhật!");
                return "redirect:/ticket/list";
            }
            
            ticketDAO.updateTicket(ticket);
            redirectAttributes.addFlashAttribute("success", "Cập nhật vé thành công!");
            return "redirect:/ticket/list";
            
        } catch (IllegalArgumentException e) {
            // Validation error
            model.addAttribute("error", e.getMessage());
            model.addAttribute("ticket", ticket);
            return "ticket/edit";
        } catch (Exception e) {
            // System error
            model.addAttribute("error", "Lỗi hệ thống khi cập nhật vé: " + e.getMessage());
            model.addAttribute("ticket", ticket);
            return "ticket/edit";
        }
    }

    // === DELETE TICKET ===
    @PostMapping("/delete/{id}")
    public String deleteTicket(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                redirectAttributes.addFlashAttribute("error", "ID vé không hợp lệ!");
                return "redirect:/ticket/list";
            }
            
            // Check if ticket exists
            Ticket existingTicket = findTicketById(id);
            if (existingTicket == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy vé để xóa!");
                return "redirect:/ticket/list";
            }
            
            ticketDAO.deleteTicket(id);
            redirectAttributes.addFlashAttribute("success", "Xóa vé thành công!");
            return "redirect:/ticket/list";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống khi xóa vé: " + e.getMessage());
            return "redirect:/ticket/list";
        }
    }

    // === PRIVATE HELPER METHODS ===
    
    /**
     * Tìm ticket theo ID
     * Note: TicketDAO chưa có method getTicketById, nên phải filter từ getAllTickets
     */
    private Ticket findTicketById(String id) {
        try {
            List<Ticket> tickets = ticketDAO.getAllTickets();
            return tickets.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm kiếm vé: " + e.getMessage(), e);
        }
    }
    
    private Customer findCustomerById(String id) {
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
    
    private Showtime findShowtimeById(String id) {
        try {
            List<Showtime> showtimes = showtimeDAO.getAllShowtimes();
            return showtimes.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
    
    private Movie findMovieById(String id) {
        try {
            List<Movie> movies = movieDAO.getAllMovies();
            return movies.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
    
    private Seat findSeatById(String id) {
        try {
            List<Seat> seats = seatDAO.getAllSeats();
            return seats.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
    
    private Room findRoomById(String id) {
        try {
            List<Room> rooms = roomDAO.getAllRooms();
            return rooms.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    // === CREATE SAMPLE DATA ===
    @GetMapping("/create-sample-data")
    public String createSampleData(RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== Tạo dữ liệu mẫu ===");
            
            // Tạo customer mẫu
            Customer customer = new Customer();
            customer.setId("C001");
            customer.setName("Nguyễn Văn A");
            customer.setEmail("nguyenvana@gmail.com");
            customer.setPhoneNumber("0123456789");
            customerDAO.insertCustomer(customer);
            System.out.println("Đã tạo customer: " + customer.getId());
            
            // Tạo movie mẫu
            Movie movie = new Movie();
            movie.setId("M001");
            movie.setName("Avengers: Endgame");
            movie.setTitle("Avengers: Endgame");
            movie.setDescription("Phim siêu anh hùng");
            movie.setDuration(180);
            movie.setGenre("Action");
            movie.setAge(13);
            movieDAO.insertMovie(movie);
            System.out.println("Đã tạo movie: " + movie.getId());
            
            // Tạo room mẫu
            Room room = new Room();
            room.setId("R001");
            room.setName("Phòng 1");
            room.setTotalSeats(50);
            roomDAO.insertRoom(room);
            System.out.println("Đã tạo room: " + room.getId());
            
            // Tạo seat mẫu
            Seat seat = new Seat();
            seat.setId("S001");
            seat.setSeatNumber("A1");
            seat.setRoomId("R001");
            seatDAO.insertSeat(seat);
            System.out.println("Đã tạo seat: " + seat.getId());
            
            // Tạo showtime mẫu
            Showtime showtime = new Showtime();
            showtime.setId("ST001");
            showtime.setMovieId("M001");
            showtime.setRoomId("R001");
            showtime.setStartTime(java.time.LocalDateTime.now().plusDays(1).withHour(18).withMinute(0));
            showtimeDAO.insertShowtime(showtime);
            System.out.println("Đã tạo showtime: " + showtime.getId());
            
            redirectAttributes.addFlashAttribute("success", "Đã tạo dữ liệu mẫu thành công!");
            
        } catch (Exception e) {
            System.out.println("Lỗi tạo dữ liệu mẫu: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Lỗi tạo dữ liệu mẫu: " + e.getMessage());
        }
        
        return "redirect:/ticket/add";
    }
}
