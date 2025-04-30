package com.studentmanagement;

public class Admin extends User {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    public Admin() {
        super(ADMIN_USERNAME, ADMIN_PASSWORD, "admin@university.edu");
    }

    @Override
    public boolean login(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }
} 