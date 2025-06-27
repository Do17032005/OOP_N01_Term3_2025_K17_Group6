package com.example.servingwebcontent.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Aivenconnection {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://avnadmin:AVNS_OY6UdTSUCEJY08Wic_V@mysql-1bf49a9c-nghiengame005.c.aivencloud.com:27021/defaultdb?ssl-mode=REQUIRED";
            String user = "avnadmin";
            String password = "AVNS_OY6UdTSUCEJY08Wic_V";
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
} 