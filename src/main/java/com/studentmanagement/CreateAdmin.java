package com.studentmanagement;

import com.studentmanagement.database.UserDAO;
import com.studentmanagement.model.Admin;

public class CreateAdmin {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        
        // Create admin account
        Admin admin = new Admin("admin", "admin123", "admin@example.com");
        
        try {
            userDAO.createUser(admin);
            System.out.println("Admin account created successfully!");
            System.out.println("Username: admin");
            System.out.println("Password: admin123");
            System.out.println("Email: admin@example.com");
        } catch (Exception e) {
            System.err.println("Error creating admin account: " + e.getMessage());
        }
    }
} 