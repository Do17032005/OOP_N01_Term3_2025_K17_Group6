package com.example.servingwebcontent.database;

import com.example.servingwebcontent.model.Customer;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerDAO {
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "SELECT * FROM Customer";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Customer c = new Customer(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phoneNumber")
                    );
                    customers.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
        return customers;
    }

    public void insertCustomer(Customer c) {
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "INSERT INTO Customer (id, name, email, phoneNumber) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, c.getId());
                ps.setString(2, c.getName());
                ps.setString(3, c.getEmail());
                ps.setString(4, c.getPhoneNumber());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thêm khách hàng vào database: " + e.getMessage(), e);
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
    }

    public void updateCustomer(Customer c) {
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "UPDATE Customer SET name=?, email=?, phoneNumber=? WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, c.getName());
                ps.setString(2, c.getEmail());
                ps.setString(3, c.getPhoneNumber());
                ps.setString(4, c.getId());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("loi khi cap nhat khach hang");
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
    }

    public Customer getCustomerById(String id) {
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "SELECT * FROM Customer WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Customer(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phoneNumber")
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("loi khi lay khach hang theo id");
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
        return null;
    }

    public void deleteCustomer(String id) {
        Connection conn = null;
        try {
            conn = AivenConnection.getConnection();
            String sql = "DELETE FROM Customer WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, id);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("loi khi xoa khach hang");
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
    }
} 