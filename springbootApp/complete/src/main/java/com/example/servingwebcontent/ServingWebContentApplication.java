package com.example.servingwebcontent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServingWebContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServingWebContentApplication.class, args);
        System.out.println("Ứng dụng đã khởi động!");
        System.out.println("Trang chủ:      http://localhost:8080/");
    }

}
