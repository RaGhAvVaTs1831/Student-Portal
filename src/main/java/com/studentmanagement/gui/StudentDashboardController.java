package com.studentmanagement.gui;

import com.studentmanagement.database.UserDAO;
import com.studentmanagement.model.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StudentDashboardController {
    private static final Logger logger = LoggerFactory.getLogger(StudentDashboardController.class);
    private final UserDAO userDAO = new UserDAO();
    private Student student;

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
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
    private TableView<String> coursesTable;
    @FXML
    private TableColumn<String, String> courseNameColumn;
    @FXML
    private TableColumn<String, Void> courseActionsColumn;
    @FXML
    private Text welcomeText;

    public void setStudent(Student student) {
        this.student = student;
        loadStudentData();
    }

    private void loadStudentData() {
        if (student != null) {
            welcomeText.setText("Welcome, " + student.getName() + "!");
            nameField.setText(student.getName());
            emailField.setText(student.getEmail());
            mobileField.setText(student.getMobileNumber());
            bloodGroupField.setText(student.getBloodGroup());
            addressArea.setText(student.getAddress());
            courseField.setText(student.getCourse());
            programField.setText(student.getProgram());
            streamField.setText(student.getStream());
            hostellerCheckBox.setSelected(student.isHosteller());
        }
    }

    @FXML
    private void handleUpdateProfile() {
        try {
            student.setName(nameField.getText());
            student.setMobileNumber(mobileField.getText());
            student.setBloodGroup(bloodGroupField.getText());
            student.setAddress(addressArea.getText());
            student.setCourse(courseField.getText());
            student.setProgram(programField.getText());
            student.setStream(streamField.getText());
            student.setHosteller(hostellerCheckBox.isSelected());

            userDAO.updateStudent(student);
            showInfo("Profile updated successfully!");
        } catch (Exception e) {
            logger.error("Error updating profile", e);
            showError("Error updating profile: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddCourse() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Course");
        dialog.setHeaderText("Add a new course");
        dialog.setContentText("Please enter the course name:");

        dialog.showAndWait().ifPresent(courseName -> {
            if (!courseName.trim().isEmpty()) {
                // TODO: Add course to database
                showInfo("Course added successfully!");
            } else {
                showError("Course name cannot be empty");
            }
        });
    }

    @FXML
    private void handleLogout() {
        try {
            // Load the login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            // Get the current stage
            Stage currentStage = (Stage) nameField.getScene().getWindow();
            
            // Set the login scene
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Login");
            currentStage.show();
        } catch (IOException e) {
            logger.error("Error returning to login screen", e);
            showError("Error returning to login screen: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 