package com.studentmanagement;

import java.util.Scanner;

public class StudentDashboard implements Dashboard {
    private Student student;
    private Scanner scanner;

    public StudentDashboard(Student student) {
        this.student = student;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void showDashboard() {
        System.out.println("\nWelcome to Student Dashboard, " + student.getName() + "!");
        System.out.println("1. View Profile");
        System.out.println("2. Update Profile");
        System.out.println("3. View Courses");
        System.out.println("4. Logout");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                viewProfile();
                break;
            case 2:
                updateProfile();
                break;
            case 3:
                viewCourses();
                break;
            case 4:
                logout();
                break;
            default:
                System.out.println("Invalid choice!");
                showDashboard();
        }
    }

    private void viewProfile() {
        System.out.println("\nStudent Profile:");
        System.out.println("Name: " + student.getName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Mobile Number: " + student.getMobileNumber());
        System.out.println("Blood Group: " + student.getBloodGroup());
        System.out.println("Address: " + student.getAddress());
        System.out.println("Course: " + student.getCourse());
        System.out.println("Program: " + student.getProgram());
        System.out.println("Hosteller: " + (student.isHosteller() ? "Yes" : "No"));
        System.out.println("Stream: " + student.getStream());
        
        showDashboard();
    }

    private void updateProfile() {
        System.out.println("\nUpdate Profile:");
        System.out.print("Enter new mobile number: ");
        String mobileNumber = scanner.nextLine();
        System.out.print("Enter new address: ");
        String address = scanner.nextLine();
        
        // Update student details
        student.setPersonalDetails(
            student.getName(),
            mobileNumber,
            student.getBloodGroup(),
            address,
            student.getCourse(),
            student.getProgram(),
            student.isHosteller(),
            student.getStream()
        );
        
        System.out.println("Profile updated successfully!");
        showDashboard();
    }

    private void viewCourses() {
        System.out.println("\nRegistered Courses:");
        for (String course : student.getCourses()) {
            System.out.println("- " + course);
        }
        showDashboard();
    }

    @Override
    public void logout() {
        System.out.println("Logging out...");
    }
} 