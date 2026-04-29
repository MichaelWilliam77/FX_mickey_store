package com.example.fx_final_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;


public class DBConnection {

    private static final String URL = "jdbc:oracle:thin:store/store@localhost:1522/XE";
    // private static final String USER = "STUDENT";
    // private static final String PASSWORD = "student";

    public static Connection getConnection() {
        Connection conn = null;

        try {
            // Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(URL);
            System.out.println("Connected Successfully!");
        } catch (Exception ex) {
            System.out.println(ex.toString());
            System.out.println("Fail");
        }

        return conn;
    }
}

