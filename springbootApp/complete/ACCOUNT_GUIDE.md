# Hướng Dẫn Sử Dụng Hệ Thống Tài Khoản

## 🎬 Cinema Booking System - Account Management

### 📋 Tổng Quan
Hệ thống hỗ trợ 2 loại tài khoản chính:
- **ADMIN**: Quản lý toàn bộ hệ thống
- **CUSTOMER**: Khách hàng đặt vé xem phim

---

## 👨‍💼 Tài Khoản Admin

### 🔑 Thông Tin Đăng Nhập Mẫu

| Username | Email | Password | Vai Trò |
|----------|-------|----------|---------|
| admin | admin@cinema.com | admin123 | System Administrator |
| manager | manager@cinema.com | manager123 | Cinema Manager |
| supervisor | supervisor@cinema.com | super123 | Cinema Supervisor |

### 🛠️ Quyền Hạn Admin

#### 1. **Dashboard** (`/admin/dashboard`)
- Xem thống kê tổng quan
- Quản lý hệ thống
- Theo dõi hoạt động

#### 2. **Quản Lý Người Dùng** (`/admin/users`)
- Xem danh sách tất cả users
- Tạo tài khoản mới
- Chỉnh sửa thông tin user
- Xóa tài khoản
- Thay đổi vai trò (ADMIN/CUSTOMER)

#### 3. **Quản Lý Nội Dung**
- **Movies**: Thêm/sửa/xóa phim
- **Showtimes**: Tạo lịch chiếu
- **Rooms**: Quản lý phòng chiếu
- **Seats**: Quản lý ghế ngồi
- **Bookings**: Xem và quản lý đặt vé

#### 4. **Cài Đặt Hệ Thống** (`/admin/settings`)
- Cấu hình hệ thống
- Backup dữ liệu
- Quản lý bảo mật

---

## 👤 Tài Khoản Customer

### 🔑 Thông Tin Đăng Nhập Mẫu

| Username | Email | Password | Tên |
|----------|-------|----------|-----|
| john.doe | john.doe@email.com | password123 | John Doe |
| jane.smith | jane.smith@email.com | password123 | Jane Smith |
| mike.wilson | mike.wilson@email.com | password123 | Mike Wilson |
| sarah.jones | sarah.jones@email.com | password123 | Sarah Jones |
| david.brown | david.brown@email.com | password123 | David Brown |
| emma.davis | emma.davis@email.com | password123 | Emma Davis |
| alex.garcia | alex.garcia@email.com | password123 | Alex Garcia |
| lisa.martinez | lisa.martinez@email.com | password123 | Lisa Martinez |

### 🎫 Quyền Hạn Customer

#### 1. **Xem Phim** (`/movies`)
- Duyệt danh sách phim
- Tìm kiếm và lọc phim
- Xem chi tiết phim

#### 2. **Xem Lịch Chiếu** (`/showtimes`)
- Xem lịch chiếu theo ngày
- Lọc theo phim và thời gian
- Xem thông tin phòng chiếu

#### 3. **Đặt Vé** (`/booking`)
- Chọn phim và suất chiếu
- Chọn ghế ngồi
- Thanh toán vé

#### 4. **Quản Lý Đặt Vé** (`/my-bookings`)
- Xem lịch sử đặt vé
- Hủy đặt vé
- Xem chi tiết vé

#### 5. **Hồ Sơ Cá Nhân** (`/profile`)
- Xem và cập nhật thông tin
- Đổi mật khẩu
- Xem thống kê cá nhân

---

## 🚀 Hướng Dẫn Sử Dụng

### 1. **Đăng Nhập**
```
URL: http://localhost:8080/login
- Nhập email và password
- Hệ thống tự động chuyển hướng theo vai trò
```

### 2. **Đăng Ký Tài Khoản Mới**
```
URL: http://localhost:8080/register
- Điền thông tin cá nhân
- Tài khoản mới mặc định là CUSTOMER
- Admin có thể thay đổi vai trò sau
```

### 3. **Chuyển Đổi Vai Trò**
```
Chỉ Admin mới có quyền:
1. Vào /admin/users
2. Chọn user cần thay đổi
3. Click "Change Role"
4. Chọn ADMIN hoặc CUSTOMER
```

### 4. **Quản Lý Session**
```
- Session được lưu tự động khi đăng nhập
- Logout để kết thúc session
- Session timeout sau 30 phút không hoạt động
```

---

## 🔒 Bảo Mật

### ⚠️ Lưu Ý Quan Trọng
1. **Mật khẩu mặc định**: Chỉ dùng cho môi trường development
2. **Production**: Cần mã hóa password và sử dụng HTTPS
3. **Session Management**: Implement proper session security
4. **Input Validation**: Validate tất cả input từ user

### 🛡️ Best Practices
- Thay đổi password mặc định ngay khi deploy
- Sử dụng password mạnh (8+ ký tự, có số, chữ hoa, ký tự đặc biệt)
- Logout khi không sử dụng
- Không chia sẻ thông tin đăng nhập

---

## 📊 Database Schema

### Bảng `users`
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    user_role ENUM('ADMIN', 'CUSTOMER') DEFAULT 'CUSTOMER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

---

## 🐛 Troubleshooting

### Lỗi Thường Gặp

#### 1. **Không đăng nhập được**
- Kiểm tra email và password
- Đảm bảo tài khoản đã được tạo trong database
- Kiểm tra kết nối database

#### 2. **Không thấy Admin Dashboard**
- Đảm bảo user có role ADMIN
- Kiểm tra session
- Refresh trang sau khi đăng nhập

#### 3. **Lỗi Permission**
- Kiểm tra user role trong database
- Đăng xuất và đăng nhập lại
- Clear browser cache

---

## 📞 Hỗ Trợ

Nếu gặp vấn đề, vui lòng:
1. Kiểm tra logs trong console
2. Xem database connection
3. Verify user credentials
4. Contact system administrator

---

**🎬 Cinema Booking System - Version 1.0**
*Developed with Spring Boot & Bootstrap* 