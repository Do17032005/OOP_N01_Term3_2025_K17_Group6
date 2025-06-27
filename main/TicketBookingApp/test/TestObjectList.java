package test;

import model.Movie;
import model.Customer;
import model.Ticket;
import model.ObjectList;


public class TestObjectList {
    public static void main(String[] args) {
        try {
            // Movie test
            ObjectList<Movie> movieList = new ObjectList<>();
            Movie m1 = new Movie("113","Avengers", "Avengers", "2025-05-01", 150, "Action", 16);

            movieList.add(m1);
            movieList.printAll();
            movieList.update(m1.getId(), "Avengers: Endgame");
            movieList.printAll();
            movieList.delete(m1.getId());
            movieList.printAll();

            System.out.println("-----------------------------------");

            // Customer test
            ObjectList<Customer> customerList = new ObjectList<>();
            Customer c1 = new Customer("KH01", "Nguyen Van A", "a@gmail.com", "0123456789");
            customerList.add(c1);
            customerList.printAll();
            customerList.update(c1.getId(), "Nguyen Van B");
            customerList.printAll();
            customerList.delete(c1.getId());
            customerList.printAll();
            System.out.println("-----------------------------------");

            // Ticket test
            ObjectList<Ticket> ticketList = new ObjectList<>();
            Ticket t1 = new Ticket("113", "Nguyen Van A", "T001", m1, "A1", "18:00", 85000);
            ticketList.add(t1);
            ticketList.printAll();
            ticketList.update(t1.getTicketId(), "TICKET-NEW");
            ticketList.printAll();
            ticketList.delete(t1.getTicketId());
            ticketList.printAll();
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }
}