package test;

import java.util.Scanner;
import model.Customer;
import model.CustomerList;

public class TestCustomer {

    public void testEditCustomer() {
        // Tạo danh sách khách hàng mẫu
        Customer s1 = new Customer("KH02", "Nguyen Van Ann", "ann@gmail.com", "0651234567");
        Customer s2 = new Customer("KH03", "Tran Van Minh", "tran@gmail.com", "0657654321");
        Customer s3 = new Customer("KH04", "Nguyen Anh", "nguyen@gmail.com", "0123456789");

        CustomerList CusList = new CustomerList();
        CusList.addCustomer(s1);
        CusList.addCustomer(s2);
        CusList.addCustomer(s3);

        // Scanner dùng chung
        Scanner sc = new Scanner(System.in);

        System.out.println("Danh sách khách hàng ban đầu:");
        CusList.printCustomerList();

        // Cập nhật thông tin
        System.out.println("\nNhập ID khách hàng cần sửa:");
        String id = sc.nextLine();
        System.out.println("Nhập tên khách hàng mới:");
        String newName = sc.nextLine();
        CusList.editCustomerName(id, newName);

        System.out.println("\nDanh sách sau khi chỉnh sửa:");
        CusList.printCustomerList();

        // Xóa khách hàng
        System.out.println("\nNhập ID khách hàng cần xóa:");
        String delId = sc.nextLine();
        CusList.deleteCustomer(delId);

        System.out.println("\nDanh sách sau khi xóa:");
        CusList.printCustomerList();
        sc.close();
        System.out.println("\nTest hoàn thành thành công!");
    }

    public static void main(String[] args) {
        TestCustomer ts = new TestCustomer();
        ts.testEditCustomer();
        System.out.println("Test kết thúc!");
    }
}
