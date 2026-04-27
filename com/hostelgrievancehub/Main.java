package com.hostelgrievancehub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3307/hostelhub?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish connection
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            conn.close();
            // If successful, open LoginFrame
            new LoginFrame();
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to connect: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Failed to connect to database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (SQLException e) {
            System.err.println("Failed to connect: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Failed to connect to database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
