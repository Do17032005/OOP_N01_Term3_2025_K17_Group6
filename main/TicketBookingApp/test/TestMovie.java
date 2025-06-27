package test;
import model.Movie;
import model.MovieList;
import java.util.ArrayList;
import java.util.Scanner;


public class TestMovie {
    
    public void testEditDelete() {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        Movie m = new Movie("113", "Averger" ,"Avengers", "2025-05-01", 150, "Action", 16); // Thêm tham số age
        Movie m1 = new Movie("114", "Sieu nhan gao", "Sieu nhan gao", "2025-12-15", 150, "Action", 16); // Thêm tham số age
        Movie m2 = new Movie("115","Quy nhap trang",  "Quy nhap trang", "2025-5-8", 150, "kinh di", 16); // Thêm tham số age
        
        movies.add(m);
        movies.add(m1);
        movies.add(m2);
        
        MovieList ml = new MovieList();
        ml.addMovie(m);
        ml.addMovie(m1);
        ml.addMovie(m2);

        ml.printMovieList(); // Kỳ vọng: In ra danh sách phim đúng định dạng
        // Cập nhật thông tin phim
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập tên phim cần cập nhật: ");
        String title = sc.nextLine();
        System.out.print("Nhập ngày chiếu mới: ");
        String showTime = sc.nextLine();
        System.out.print("Nhập thời lượng mới: ");
        int duration = Integer.parseInt(sc.nextLine());
        System.out.print("Nhập thể loại mới: ");
        String genre = sc.nextLine();
        System.out.print("Nhập độ tuổi mới: ");
        int age = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getTitle().equals(title)) {
                movies.get(i).setShowTime(showTime);
                movies.get(i).setDuration(duration);
                movies.get(i).setGenre(genre);
                movies.get(i).setAge(age);
                System.out.println("Đã cập nhật thông tin phim.");
                break;
            }
        }
        ml.printMovieList(); // Kỳ vọng: In ra danh sách phim đã cập nhật đúng định dạng
        // Xóa phim
        System.out.print("Nhập tên phim cần xóa: ");
        Scanner MvID = new Scanner(System.in);
        String movieId = MvID.nextLine();
        ml.getDeleteMovie(Integer.parseInt(movieId));
        ml.printMovieList(); // danh  sach sau khi xoa
        System.out.println("Đã xóa phim.");
        
        

    }
}