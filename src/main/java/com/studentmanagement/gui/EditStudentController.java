package com.studentmanagement.gui;

import com.studentmanagement.database.UserDAO;
import com.studentmanagement.model.Student;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditStudentController {
    private static final Logger logger = LoggerFactory.getLogger(EditStudentController.class);
    private final UserDAO userDAO = new UserDAO();
    private Student student;
    private AdminDashboardController adminController;

    @FXML
    private TextField usernameField;
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

    public void setStudent(Student student) {
        this.student = student;
        loadStudentData();
    }

    public void setAdminController(AdminDashboardController adminController) {
        this.adminController = adminController;
    }

    private void loadStudentData() {
        if (student != null) {
            usernameField.setText(student.getUsername());
            emailField.setText(student.getEmail());
            nameField.setText(student.getName());
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
    private void handleSave() {
        if (validateInput()) {
            try {
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

                userDAO.updateStudent(student);
                adminController.refreshStudentList();
                closeDialog();
            } catch (Exception e) {
                logger.error("Error updating student", e);
                errorLabel.setText("Error updating student: " + e.getMessage());
            }
        }
    }

    private boolean validateInput() {
        if (nameField.getText().isEmpty()) {
            errorLabel.setText("Name is required");
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