package test;
import model.Movie;
import model.Ticket;
import model.TicketList;
import java.util.ArrayList;
import java.util.Scanner;


public class TestTicket {

    public void testEditDelete() {
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        Movie m = new Movie("113","One Piece", "One Piece", "2025-05-10", 120, "Adventure", 13);
        Ticket t = new Ticket("113", "Nguyen Van A", "T001", m, "A5", "18:00", 85000);
        Ticket t1 = new Ticket("114", "Nguyen Van B", "T002", m, "A6", "19:00", 85000);
        Ticket t2 = new Ticket("115", "Nguyen Van C", "T003", m, "A7", "20:00", 85000);

        tickets.add(t);
        tickets.add(t1);
        tickets.add(t2);

        TicketList tk = new TicketList();
        tk.addTicket(t);
        tk.addTicket(t1);
        tk.addTicket(t2);
        tk.printTicketList(); // Kỳ vọng: In ra danh sách vé đúng định dạng

        // Cập nhật thông tin vé
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập ID vé cần cập nhật: ");
        String ticketId = sc.nextLine();
        System.out.print("Nhập ghế mới: ");
        String seat = sc.nextLine();
        System.out.print("Nhập thời gian mới: ");
        String time = sc.nextLine();
        System.out.print("Nhập giá vé mới: ");
        int price = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getTicketId().equals(ticketId)) {
                tickets.get(i).setSeat(seat);
                tickets.get(i).setShowTime(time);
                tickets.get(i).setPrice(price);
                System.out.println("Đã cập nhật thông tin vé.");
                break;
            }
        }

        tk.printTicketList(); // Kỳ vọng: In ra danh sách vé đã cập nhật đúng định dạng

        // Xóa vé
        System.out.print("Nhập ID vé cần xóa: ");
        String ticketIdToDelete = sc.nextLine();
        tk.getDeleteTickets(Integer.parseInt(ticketIdToDelete));
        // Xóa vé
        tk.printTicketList(); // Kỳ vọng: In ra danh sách vé sau khi xóa
        System.out.println("Đã xóa vé.");


    }    
}

