package com.studentmanagement.database;

import com.studentmanagement.model.Admin;
import com.studentmanagement.model.Student;
import com.studentmanagement.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    private static final String DB_URL = "jdbc:h2:./studentdb;DB_CLOSE_DELAY=-1";

    public UserDAO() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "username VARCHAR(50) PRIMARY KEY, " +
                    "password VARCHAR(100), " +
                    "email VARCHAR(100), " +
                    "role VARCHAR(20))");

            stmt.execute("CREATE TABLE IF NOT EXISTS student_details (" +
                    "username VARCHAR(50) PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "mobile_number VARCHAR(20), " +
                    "blood_group VARCHAR(10), " +
                    "address TEXT, " +
                    "course VARCHAR(50), " +
                    "program VARCHAR(50), " +
                    "stream VARCHAR(50), " +
                    "hosteller BOOLEAN, " +
                    "FOREIGN KEY (username) REFERENCES users(username))");

        } catch (SQLException e) {
            logger.error("Error initializing database", e);
            throw new RuntimeException("Error initializing database", e);
        }
    }

    public void createUser(User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        String role = user instanceof Admin ? "ADMIN" : "STUDENT";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)")) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getEmail());
            stmt.setString(4, role);
            stmt.executeUpdate();

            if (user instanceof Student) {
                Student student = (Student) user;
                createStudentDetails(student, conn);
            }

        } catch (SQLException e) {
            logger.error("Error creating user", e);
            throw new RuntimeException("Error creating user", e);
        }
    }

    private void createStudentDetails(Student student, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO student_details (username, name, mobile_number, blood_group, address, " +
                        "course, program, stream, hosteller) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            
            stmt.setString(1, student.getUsername());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getMobileNumber());
            stmt.setString(4, student.getBloodGroup());
            stmt.setString(5, student.getAddress());
            stmt.setString(6, student.getCourse());
            stmt.setString(7, student.getProgram());
            stmt.setString(8, student.getStream());
            stmt.setBoolean(9, student.isHosteller());
            stmt.executeUpdate();
        }
    }

    public User authenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE username = ?")) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    String role = rs.getString("role");
                    String email = rs.getString("email");

                    if (role.equals("ADMIN")) {
                        return new Admin(username, password, email);
                    } else {
                        Student student = new Student(username, password, email);
                        loadStudentDetails(student, conn);
                        return student;
                    }
                }
            }
            return null;
        } catch (SQLException e) {
            logger.error("Error authenticating user", e);
            throw new RuntimeException("Error authenticating user", e);
        }
    }

    private void loadStudentDetails(Student student, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM student_details WHERE username = ?")) {
            
            stmt.setString(1, student.getUsername());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                student.setPersonalDetails(
                    rs.getString("name"),
                    rs.getString("mobile_number"),
                    rs.getString("blood_group"),
                    rs.getString("address"),
                    rs.getString("course"),
                    rs.getString("program"),
                    rs.getBoolean("hosteller"),
                    rs.getString("stream")
                );
            }
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT u.*, sd.* FROM users u " +
                     "JOIN student_details sd ON u.username = sd.username " +
                     "WHERE u.role = 'STUDENT'")) {
            
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Student student = new Student(
                    rs.getString("username"),
                    "",  // We don't load passwords
                    rs.getString("email")
                );

                student.setPersonalDetails(
                    rs.getString("name"),
                    rs.getString("mobile_number"),
                    rs.getString("blood_group"),
                    rs.getString("address"),
                    rs.getString("course"),
                    rs.getString("program"),
                    rs.getBoolean("hosteller"),
                    rs.getString("stream")
                );

                students.add(student);
            }
        } catch (SQLException e) {
            logger.error("Error getting all students", e);
            throw new RuntimeException("Error getting all students", e);
        }

        return students;
    }

    public void updateStudent(Student student) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE users SET email = ? WHERE username = ?")) {
                stmt.setString(1, student.getEmail());
                stmt.setString(2, student.getUsername());
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE student_details SET name = ?, mobile_number = ?, blood_group = ?, " +
                    "address = ?, course = ?, program = ?, stream = ?, hosteller = ? " +
                    "WHERE username = ?")) {
                
                stmt.setString(1, student.getName());
                stmt.setString(2, student.getMobileNumber());
                stmt.setString(3, student.getBloodGroup());
                stmt.setString(4, student.getAddress());
                stmt.setString(5, student.getCourse());
                stmt.setString(6, student.getProgram());
                stmt.setString(7, student.getStream());
                stmt.setBoolean(8, student.isHosteller());
                stmt.setString(9, student.getUsername());
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            logger.error("Error updating student", e);
            throw new RuntimeException("Error updating student", e);
        }
    }

    public void deleteStudent(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM student_details WHERE username = ?")) {
                stmt.setString(1, username);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM users WHERE username = ?")) {
                stmt.setString(1, username);
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            logger.error("Error deleting student", e);
            throw new RuntimeException("Error deleting student", e);
        }
    }
} 