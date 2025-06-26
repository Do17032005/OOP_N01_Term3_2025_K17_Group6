package com.example.servingwebcontent.model;

import java.util.ArrayList;

public class CustomerList {
    private ArrayList<Customer> cus = new ArrayList<Customer>();

    // Thêm khách hàng
    public void addCustomer(Customer customer) {
        cus.add(customer);
    }

    // Sửa tên khách hàng theo ID
    public void editCustomerName(String customerId, String newName) {
        for (Customer c : cus) {
            if (c.getId().equals(customerId)) {
                c.setName(newName);
                System.out.println("Đã cập nhật tên khách hàng.");
                return;
            }
        }
        System.out.println("Không tìm thấy khách hàng có ID: " + customerId);
    }

    // Xóa khách hàng theo ID
    public void deleteCustomer(String customerId) {
        for (int i = 0; i < cus.size(); i++) {
            if (cus.get(i).getId().equals(customerId)) {
                cus.remove(i);
                System.out.println("Đã xóa khách hàng có ID: " + customerId);
                return;
            }
        }
        System.out.println("Không tìm thấy khách hàng có ID: " + customerId);
    }

    // In danh sách khách hàng
    public void printCustomerList() {
        int len= cus.size();
        for (int i=0; i < len; i++) {
                System.out.println("Customer ID: " + cus.get(i).getId());
                System.out.println("Customer Name: " + cus.get(i).getName());
                System.out.println("Customer Phone: " + cus.get(i).getPhoneNumber());
                System.out.println("Customer Email: " + cus.get(i).getEmail());
                System.out.println("-------------------------");
        }
    }

    public ArrayList<Customer> getList() {
        return cus;
    }
} 