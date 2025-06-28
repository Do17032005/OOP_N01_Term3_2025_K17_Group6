# Cinema Ticket Booking System - Nhóm 6

## Thành viên nhóm
- Hoàng Văn Độ - 23010581  
- Nguyễn Tiến Doanh - 23010472  
- Dương Thiện Hùng - 23010601  
- Nguyen Le Thu

---

## Mô tả dự án

**Cinema Ticket Booking System** là hệ thống quản lý và đặt vé xem phim trực tuyến, xây dựng bằng **Java Spring Boot**. Ứng dụng hỗ trợ quản lý toàn diện về phim, phòng chiếu, suất chiếu, ghế ngồi, vé, khách hàng và quy trình đặt vé.

---

## Chức năng chính

### Quản lý phim (Movie Management)
- Thêm, sửa, xóa, hiển thị danh sách phim
- Quản lý thông tin: tiêu đề, ngày phát hành, thời gian chiếu, thời lượng, thể loại, độ tuổi, mô tả
- Kiểm tra độ tuổi phù hợp cho khán giả

### Quản lý phòng chiếu (Room Management)
- Thêm, sửa, xóa, hiển thị danh sách phòng
- Quản lý thông tin: tên phòng, tổng số ghế

### Quản lý suất chiếu (Showtime Management)
- Thêm, sửa, xóa, hiển thị danh sách suất chiếu
- Liên kết phim với phòng chiếu và thời gian bắt đầu

### Quản lý ghế ngồi (Seat Management)
- Thêm, sửa, xóa, hiển thị danh sách ghế
- Quản lý ghế theo phòng và số ghế

### Quản lý khách hàng (Customer Management)
- Thêm, sửa, xóa, hiển thị danh sách khách hàng
- Quản lý thông tin: tên, email, số điện thoại
- Hệ thống đăng ký và đăng nhập

### Quản lý vé (Ticket Management)
- Thêm, sửa, xóa, hiển thị danh sách vé
- Liên kết vé với suất chiếu, ghế ngồi và khách hàng
- Quản lý giá vé
- Tìm kiếm và in vé

### Quy trình đặt vé (Booking Process)
- Chọn phim → Chọn suất chiếu → Chọn ghế → Xác nhận thông tin khách hàng
- Thanh toán và tạo vé
- In vé tự động

---

## Kiến trúc và Mô hình dữ liệu

### Lớp cơ sở (Base Classes)
1. **ObjectGeneral**
   - Lớp cơ sở cho tất cả các đối tượng
   - Thuộc tính: `id`, `name`
   - Phương thức: getter/setter cho id và name

2. **ObjectList<T extends ObjectGeneral>**
   - Lớp generic để quản lý danh sách các đối tượng
   - Hỗ trợ CRUD operations: Create, Read, Update, Delete
   - Sử dụng ArrayList để lưu trữ

### Các lớp Model chính

1. **Movie** (extends ObjectGeneral)
   - Thuộc tính: `title`, `showTime`, `dateTime`, `duration`, `genre`, `age`, `description`
   - Phương thức: `display()`, `isSuitableForAge(int viewerAge)`

2. **Customer** (extends ObjectGeneral)
   - Thuộc tính: `email`, `phoneNumber`
   - Phương thức: `displayInfo()`

3. **Room**
   - Thuộc tính: `id`, `name`, `totalSeats`
   - Phương thức: toString()

4. **Showtime**
   - Thuộc tính: `id`, `movieId`, `roomId`, `startTime` (LocalDateTime)
   - Phương thức: toString()

5. **Seat**
   - Thuộc tính: `id`, `roomId`, `seatNumber`
   - Phương thức: toString()

6. **Ticket**
   - Thuộc tính: `id`, `showtimeId`, `seatId`, `customerId`, `price`
   - Phương thức: toString()

### Các lớp List (Quản lý danh sách)
- **MovieList** (extends ObjectList<Movie>)
- **CustomerList** (extends ObjectList<Customer>)
- **TicketList** (extends ObjectList<Ticket>)

### Lớp Database Access (DAO)
- **AivenConnection**: Quản lý kết nối database
- **MovieDAO**: Thao tác dữ liệu phim
- **CustomerDAO**: Thao tác dữ liệu khách hàng
- **RoomDAO**: Thao tác dữ liệu phòng
- **SeatDAO**: Thao tác dữ liệu ghế
- **ShowtimeDAO**: Thao tác dữ liệu suất chiếu
- **TicketDAO**: Thao tác dữ liệu vé

### Controllers (Spring Boot)
- **MovieController**: Xử lý request liên quan đến phim
- **CustomerController**: Xử lý request liên quan đến khách hàng
- **RoomController**: Xử lý request liên quan đến phòng
- **SeatController**: Xử lý request liên quan đến ghế
- **ShowtimeController**: Xử lý request liên quan đến suất chiếu
- **TicketController**: Xử lý request liên quan đến vé
- **BookingController**: Xử lý quy trình đặt vé
- **TicketLookupController**: Xử lý tìm kiếm vé

---

## Công nghệ sử dụng

- **Java 17**
- **Spring Boot 3.3.0**
- **Spring Web MVC**
- **Thymeleaf** (Template Engine)
- **Spring JDBC** (Database Access)
- **MySQL 8.0.26** (Database)
- **Maven** (Build Tool)
- **Spring Boot DevTools** (Development)

---

## Cấu trúc dự án

```
springbootApp/complete/
├── src/main/java/com/example/servingwebcontent/
│   ├── model/                    # Các lớp model
│   │   ├── ObjectGeneral.java    # Lớp cơ sở
│   │   ├── ObjectList.java       # Lớp quản lý danh sách
│   │   ├── Movie.java           # Model phim
│   │   ├── Customer.java        # Model khách hàng
│   │   ├── Room.java            # Model phòng
│   │   ├── Showtime.java        # Model suất chiếu
│   │   ├── Seat.java            # Model ghế
│   │   ├── Ticket.java          # Model vé
│   │   ├── MovieList.java       # Quản lý danh sách phim
│   │   ├── CustomerList.java    # Quản lý danh sách khách hàng
│   │   └── TicketList.java      # Quản lý danh sách vé
│   ├── database/                 # Lớp truy cập dữ liệu
│   │   ├── AivenConnection.java  # Kết nối database
│   │   ├── MovieDAO.java        # DAO phim
│   │   ├── CustomerDAO.java     # DAO khách hàng
│   │   ├── RoomDAO.java         # DAO phòng
│   │   ├── SeatDAO.java         # DAO ghế
│   │   ├── ShowtimeDAO.java     # DAO suất chiếu
│   │   └── TicketDAO.java       # DAO vé
│   ├── *Controller.java          # Các controller
│   └── ServingWebContentApplication.java
├── src/main/resources/
│   ├── templates/               # Thymeleaf templates
│   │   ├── fragments/           # Template fragments
│   │   ├── add-*.html          # Form thêm mới
│   │   ├── edit-*.html         # Form chỉnh sửa
│   │   ├── *-list.html         # Trang danh sách
│   │   ├── booking-*.html      # Trang đặt vé
│   │   └── ticket-*.html       # Trang vé
│   └── static/                  # Static resources
└── pom.xml                      # Maven configuration
```

---

## Hướng dẫn chạy dự án

### Yêu cầu hệ thống
- Java 17 hoặc cao hơn
- MySQL 8.0 hoặc cao hơn
- Maven 3.6+

### Cài đặt và chạy

1. **Clone repository:**
   ```bash
   git clone https://github.com/Do17032005/OOP_N01_Term3_2025_K17_Group6
   cd OOP_N01_Term3_2025_K17_Group6
   ```

2. **Cấu hình database:**
   - Tạo database MySQL
   - Cấu hình kết nối trong `application.properties`

3. **Build và chạy ứng dụng:**
   ```bash
   cd springbootApp/complete
   mvn clean install
   mvn spring-boot:run
   ```

4. **Truy cập ứng dụng:**
   ```
   http://localhost:8080/
   ```

---

## Tính năng nổi bật

### 1. Kiến trúc OOP
- Sử dụng kế thừa với `ObjectGeneral` làm lớp cơ sở
- Generic classes với `ObjectList<T>`
- Encapsulation với getter/setter methods

### 2. Database Integration
- Sử dụng Spring JDBC cho truy cập database
- DAO pattern cho tách biệt logic business và data access
- Connection pooling với Aiven

### 3. Web Interface
- Thymeleaf templates cho giao diện
- Responsive design
- Form validation
- Navigation fragments

### 4. Booking Workflow
- Multi-step booking process
- Real-time seat selection
- Ticket generation và printing
- Customer authentication

---

## Hình ảnh & sơ đồ

### 1. Class Diagram
![Class Diagram](nd2.jpg)

### 2. Activity Diagram
![Activity Diagram](https://github.com/Rumnn/resource/blob/main/activitydiagram.jpg)

### 3. Lưu đồ thuật toán
![Lưu đồ thuật toán](https://github.com/Rumnn/resource/blob/main/LuuDoThuatToan.jpg)

### 4. Giao diện thêm phim
![UI thêm phim](https://github.com/Do17032005/IMG/blob/main/UI%20nh%E1%BA%ADp%20phim.png)

### 5. UI vé sắp chiếu
![UI vé sắp chiếu](https://github.com/Rumnn/resource/blob/main/upcoming.PNG)

---

## Liên kết

- [GitHub Repository](https://github.com/Do17032005/OOP_N01_Term3_2025_K17_Group6)
- [README.md trên GitHub](https://github.com/Do17032005/OOP_N01_Term3_2025_K17_Group6/edit/main/README.md)

