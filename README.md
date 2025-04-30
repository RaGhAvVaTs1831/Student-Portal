# Student Management System

A Java-based student management system that allows students to register and manage their profiles, while administrators can manage student records.

## Features

- Student Registration and Login
- Student Profile Management
- Admin Dashboard
- Student Records Management
- Course Management

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Building the Project

1. Clone the repository
2. Navigate to the project directory
3. Run the following command to build the project:
   ```bash
   mvn clean package
   ```

## Running the Application

After building the project, you can run it using:

```bash
java -jar target/student-management-system-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Default Admin Credentials

- Username: admin
- Password: admin123

## Usage

1. **Student Login**
   - Students can log in using their credentials
   - View and update their profile
   - View their registered courses

2. **Admin Login**
   - Admins can log in using the default credentials
   - View all student records
   - Add new students
   - Update student details
   - Delete student records

## Project Structure

- `User.java` - Base class for all users
- `Student.java` - Student entity class
- `Admin.java` - Admin entity class
- `Dashboard.java` - Interface for dashboard functionality
- `StudentDashboard.java` - Student dashboard implementation
- `AdminDashboard.java` - Admin dashboard implementation
- `StudentManagementSystem.java` - Main application class

## Data Storage

The application currently stores data in memory using ArrayList. For a production environment, you would want to implement a proper database solution. 