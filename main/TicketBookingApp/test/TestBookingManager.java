package test;

import model.Customer;
import model.Movie;
import model.MovieList;
import model.Ticket;
import model.TicketList;

public class TestBookingManager {
    public static void test(String[] args) {

        // Khởi tạo khách hàng (Customer có id, name, email, phone)
        Customer customer1 = new Customer("C001", "Nguyen Van A", "a@gmail.com", "0123456789");
        Customer customer2 = new Customer("C002", "Tran Thi B", "b@gmail.com", "0987654321");
        Customer customer3 = new Customer("C003", "Le Van C", "C@gmail.com", "1234567890");
        Customer customer4 = new Customer("C004", "Pham Thi D", "D@gmail.com", "9876543210");
        

        // Khởi tạo phim (Movie có id, name, showTime, duration, genre, age)
        Movie movie1 = new Movie("M001", "Avengers", "Avengers", "2025-06-06", 150, "Action", 13);
        Movie movie2 = new Movie("M002", "Spider", "Spider", "2025-07-07", 120, "Action", 12);
        Movie movie3 = new Movie("M003", "Batman", "Batman", "2025-08-08", 140, "Action", 14);
        Movie movie4 = new Movie("M004", "Superman", "Superman", "2025-09-09", 130, "Action", 15);
  

        // Khởi tạo vé (Ticket có id, name, ticketId, Movie, seat, showTime, price)
        Ticket ticket1 = new Ticket("T001", "Vé 1", "1001", movie1, "A1", "18:00", 90000);
        Ticket ticket2 = new Ticket("T002", "Vé 2", "1002", movie2, "B2", "20:00", 75000);
        Ticket ticket3 = new Ticket("T003", "Vé 3", "1003", movie3, "C3", "19:00", 80000);
        Ticket ticket4 = new Ticket("T004", "Vé 4", "1004", movie4, "D4", "21:00", 85000);

        // Thêm phim vào
        // danh sách phim
        MovieList movieList = new MovieList();
        movieList.addMovie(movie1);
        movieList.addMovie(movie2);
        movieList.addMovie(movie3);
        movieList.addMovie(movie4);

        // In danh sách phim
        System.out.println("--- Danh sách phim ---");
        movieList.printMovieList();
        System.out.println("___________________________________________");
        // Thêm khách hàng vào danh sách khách hàng
        model.CustomerList customerList = new model.CustomerList();
        customerList.addCustomer(customer1);
        customerList.addCustomer(customer2);
        customerList.addCustomer(customer3);
        customerList.addCustomer(customer4);


        // In danh sách khách hàng
        System.out.println("\n--- Danh sách khách hàng ---");
        customerList.printCustomerList();
        System.out.println("___________________________________________");

        // Khởi tạo danh sách vé
        TicketList ticketList = new TicketList();
        ticketList.addTicket(ticket1);
        ticketList.addTicket(ticket2);
        ticketList.addTicket(ticket3);
        ticketList.addTicket(ticket4);

        // In danh sach ve theo id
        
        System.out.println("\n--- Danh sách vé theo ID ---");
        ticketList.printTicketsByCustomerId(customer1.getId());
        ticketList.printTicketsByCustomerId(customer2.getId());
        ticketList.printTicketsByCustomerId(customer3.getId());
        ticketList.printTicketsByCustomerId(customer4.getId());
        System.out.println("___________________________________________");

        // kiem tra gio chieu
        System.out.println("\n--- Vé sắp chiếu trong 1 giờ tới ---");
        // In vé sắp chiếu trong 1 giờ tới
        System.out.println("___________________________________________");
        ticketList.printUpComingTickets();


        

        // Thống kê số lượng vé đã đặt
        System.out.println("____________________________________________");
        System.out.println("\n--- Thống kê vé đã đặt ---");
        ticketList.printTicketList();
        System.out.println("____________________________________________");
        System.out.println("\nTổng số vé đã đặt: " + ticketList.getTicketCount());

       
    }
}
