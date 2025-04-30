package com.studentmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminDashboard implements Dashboard {
    private List<Student> students;
    private Scanner scanner;

    public AdminDashboard(List<Student> students) {
        this.students = students;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void showDashboard() {
        System.out.println("\nWelcome to Admin Dashboard!");
        System.out.println("1. View All Students");
        System.out.println("2. Add New Student");
        System.out.println("3. Update Student Details");
        System.out.println("4. Delete Student");
        System.out.println("5. Logout");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                viewAllStudents();
                break;
            case 2:
                addNewStudent();
                break;
            case 3:
                updateStudent();
                break;
            case 4:
                deleteStudent();
                break;
            case 5:
                logout();
                break;
            default:
                System.out.println("Invalid choice!");
                showDashboard();
        }
    }

    private void viewAllStudents() {
        System.out.println("\nAll Students:");
        for (Student student : students) {
            System.out.println("\nStudent Details:");
            System.out.println("Name: " + student.getName());
            System.out.println("Email: " + student.getEmail());
            System.out.println("Mobile Number: " + student.getMobileNumber());
            System.out.println("Course: " + student.getCourse());
            System.out.println("Program: " + student.getProgram());
            System.out.println("----------------------------------------");
        }
        showDashboard();
    }

    private void addNewStudent() {
        System.out.println("\nAdd New Student:");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter mobile number: ");
        String mobileNumber = scanner.nextLine();
        System.out.print("Enter blood group: ");
        String bloodGroup = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter course: ");
        String course = scanner.nextLine();
        System.out.print("Enter program: ");
        String program = scanner.nextLine();
        System.out.print("Is hosteller? (true/false): ");
        boolean isHosteller = scanner.nextBoolean();
        scanner.nextLine();
        System.out.print("Enter stream: ");
        String stream = scanner.nextLine();

        Student newStudent = new Student(username, password, email);
        newStudent.setPersonalDetails(name, mobileNumber, bloodGroup, address, course, program, isHosteller, stream);
        students.add(newStudent);

        System.out.println("Student added successfully!");
        showDashboard();
    }

    private void updateStudent() {
        System.out.print("\nEnter student email to update: ");
        String email = scanner.nextLine();

        for (Student student : students) {
            if (student.getEmail().equals(email)) {
                System.out.print("Enter new mobile number: ");
                String mobileNumber = scanner.nextLine();
                System.out.print("Enter new address: ");
                String address = scanner.nextLine();
                System.out.print("Enter new course: ");
                String course = scanner.nextLine();
                System.out.print("Enter new program: ");
                String program = scanner.nextLine();

                student.setPersonalDetails(
                    student.getName(),
                    mobileNumber,
                    student.getBloodGroup(),
                    address,
                    course,
                    program,
                    student.isHosteller(),
                    student.getStream()
                );

                System.out.println("Student details updated successfully!");
                showDashboard();
                return;
            }
        }

        System.out.println("Student not found!");
        showDashboard();
    }

    private void deleteStudent() {
        System.out.print("\nEnter student email to delete: ");
        String email = scanner.nextLine();

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getEmail().equals(email)) {
                students.remove(i);
                System.out.println("Student deleted successfully!");
                showDashboard();
                return;
            }
        }

        System.out.println("Student not found!");
        showDashboard();
    }

    @Override
    public void logout() {
        System.out.println("Logging out...");
    }
} 