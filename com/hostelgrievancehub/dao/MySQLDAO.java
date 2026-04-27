package com.hostelgrievancehub.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hostelhub?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root"; // Change as needed
    private static final String DB_PASSWORD = "Samdev@2005"; // Change as needed

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        } catch (SQLException e) {
            System.err.println("Failed to connect: " + e.getMessage());
            throw e;
        }
    }
}
