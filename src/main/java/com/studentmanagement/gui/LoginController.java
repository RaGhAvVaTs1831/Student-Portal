package com.studentmanagement.gui;

import com.studentmanagement.database.UserDAO;
import com.studentmanagement.model.Admin;
import com.studentmanagement.model.Student;
import com.studentmanagement.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final UserDAO userDAO = new UserDAO();

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {
        roleComboBox.getItems().addAll("Student", "Admin");
        roleComboBox.setValue("Student");
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password");
            return;
        }

        try {
            User user = userDAO.authenticateUser(username, password);
            if (user != null) {
                if (role.equals("Admin") && user instanceof Admin) {
                    openAdminDashboard((Admin) user);
                } else if (role.equals("Student") && user instanceof Student) {
                    openStudentDashboard((Student) user);
                } else {
                    errorLabel.setText("Invalid role selected");
                }
            } else {
                errorLabel.setText("Invalid username or password");
            }
        } catch (Exception e) {
            logger.error("Error during login", e);
            errorLabel.setText("Error during login: " + e.getMessage());
        }
    }

    private void openAdminDashboard(Admin admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Student Management System - Admin Dashboard");
        } catch (IOException e) {
            logger.error("Error opening admin dashboard", e);
            errorLabel.setText("Error opening admin dashboard: " + e.getMessage());
        }
    }

    private void openStudentDashboard(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/student_dashboard.fxml"));
            Parent root = loader.load();

            StudentDashboardController controller = loader.getController();
            controller.setStudent(student);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Student Management System - Student Dashboard");
        } catch (IOException e) {
            logger.error("Error opening student dashboard", e);
            errorLabel.setText("Error opening student dashboard: " + e.getMessage());
        }
    }
} 