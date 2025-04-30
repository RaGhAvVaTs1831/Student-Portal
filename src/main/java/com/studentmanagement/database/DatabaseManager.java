package com.studentmanagement.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static final String DB_URL = "jdbc:h2:./student_management_db";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    private static Connection connection;

    static {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            initializeDatabase();
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Error initializing database", e);
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private static void initializeDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Create Users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) UNIQUE NOT NULL," +
                    "password VARCHAR(100) NOT NULL," +
                    "email VARCHAR(100) UNIQUE NOT NULL," +
                    "role VARCHAR(20) NOT NULL" +
                    ")");

            // Create Students table
            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT NOT NULL," +
                    "name VARCHAR(100) NOT NULL," +
                    "mobile_number VARCHAR(20)," +
                    "blood_group VARCHAR(5)," +
                    "address TEXT," +
                    "course VARCHAR(50)," +
                    "program VARCHAR(50)," +
                    "is_hosteller BOOLEAN," +
                    "stream VARCHAR(50)," +
                    "FOREIGN KEY (user_id) REFERENCES users(id)" +
                    ")");

            // Create Courses table
            stmt.execute("CREATE TABLE IF NOT EXISTS courses (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "student_id INT NOT NULL," +
                    "course_name VARCHAR(100) NOT NULL," +
                    "FOREIGN KEY (student_id) REFERENCES students(id)" +
                    ")");
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            logger.error("Error getting database connection", e);
            throw new RuntimeException("Failed to get database connection", e);
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("Error closing database connection", e);
        }
    }
} 