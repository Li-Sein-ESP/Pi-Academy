package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Database configuration (typically in a separate config file or class)
public class DatabaseConfig {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/education_app";
    private static final String DB_USER = "root"; // Default XAMPP username, or use 'edu_user' if created
    private static final String DB_PASSWORD = ""; // Leave empty if no password, or use 'rootpass'/'edu_pass'

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}