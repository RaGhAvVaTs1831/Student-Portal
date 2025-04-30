package com.studentmanagement.gui;

import com.studentmanagement.database.UserDAO;
import com.studentmanagement.model.Student;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddStudentController {
    private static final Logger logger = LoggerFactory.getLogger(AddStudentController.class);
    private final UserDAO userDAO = new UserDAO();
    private AdminDashboardController adminController;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField mobileField;
    @FXML
    private TextField bloodGroupField;
    @FXML
    private TextArea addressArea;
    @FXML
    private TextField courseField;
    @FXML
    private TextField programField;
    @FXML
    private TextField streamField;
    @FXML
    private CheckBox hostellerCheckBox;
    @FXML
    private Label errorLabel;

    public void setAdminController(AdminDashboardController adminController) {
        this.adminController = adminController;
    }

    @FXML
    private void handleAddStudent() {
        if (validateInput()) {
            try {
                Student student = new Student(
                    usernameField.getText(),
                    passwordField.getText(),
                    emailField.getText()
                );

                student.setPersonalDetails(
                    nameField.getText(),
                    mobileField.getText(),
                    bloodGroupField.getText(),
                    addressArea.getText(),
                    courseField.getText(),
                    programField.getText(),
                    hostellerCheckBox.isSelected(),
                    streamField.getText()
                );

                userDAO.createUser(student);
                adminController.refreshStudentList();
                closeDialog();
            } catch (Exception e) {
                logger.error("Error adding student", e);
                errorLabel.setText("Error adding student: " + e.getMessage());
            }
        }
    }

    private boolean validateInput() {
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty() ||
            emailField.getText().isEmpty() || nameField.getText().isEmpty()) {
            errorLabel.setText("Please fill in all required fields");
            return false;
        }

        if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errorLabel.setText("Please enter a valid email address");
            return false;
        }

        return true;
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
} 