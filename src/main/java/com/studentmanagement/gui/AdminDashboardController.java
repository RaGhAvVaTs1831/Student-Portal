package com.studentmanagement.gui;

import com.studentmanagement.database.UserDAO;
import com.studentmanagement.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class AdminDashboardController {
    private static final Logger logger = LoggerFactory.getLogger(AdminDashboardController.class);
    private final UserDAO userDAO = new UserDAO();

    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> usernameColumn;
    @FXML
    private TableColumn<Student, String> emailColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, String> mobileColumn;
    @FXML
    private TableColumn<Student, String> courseColumn;
    @FXML
    private TableColumn<Student, String> programColumn;
    @FXML
    private TableColumn<Student, String> streamColumn;
    @FXML
    private TableColumn<Student, Boolean> hostellerColumn;
    @FXML
    private TableColumn<Student, Void> actionsColumn;
    @FXML
    private ComboBox<String> reportTypeComboBox;
    @FXML
    private TextArea reportArea;

    @FXML
    private void initialize() {
        setupTableColumns();
        setupReportTypes();
        refreshStudentList();
    }

    private void setupReportTypes() {
        ObservableList<String> reportTypes = FXCollections.observableArrayList(
            "Student List",
            "Course Enrollment",
            "Program Statistics"
        );
        reportTypeComboBox.setItems(reportTypes);
    }

    @FXML
    private void handleLogout() {
        try {
            // Load the login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            // Get the current stage
            Stage currentStage = (Stage) studentTable.getScene().getWindow();
            
            // Set the login scene
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Login");
            currentStage.show();
        } catch (IOException e) {
            logger.error("Error returning to login screen", e);
            showError("Error returning to login screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        refreshStudentList();
    }

    private void setupTableColumns() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        mobileColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        programColumn.setCellValueFactory(new PropertyValueFactory<>("program"));
        streamColumn.setCellValueFactory(new PropertyValueFactory<>("stream"));
        hostellerColumn.setCellValueFactory(new PropertyValueFactory<>("hosteller"));

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox actionButtons = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    handleEditStudent(student);
                });

                deleteButton.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    handleDeleteStudent(student);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionButtons);
            }
        });
    }

    public void refreshStudentList() {
        try {
            List<Student> students = userDAO.getAllStudents();
            studentTable.setItems(FXCollections.observableArrayList(students));
        } catch (Exception e) {
            logger.error("Error refreshing student list", e);
            showError("Error loading students: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddStudent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_student.fxml"));
            Parent root = loader.load();

            AddStudentController controller = loader.getController();
            controller.setAdminController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Student");
            stage.show();
        } catch (IOException e) {
            logger.error("Error opening add student dialog", e);
            showError("Error opening add student dialog: " + e.getMessage());
        }
    }

    private void handleEditStudent(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit_student.fxml"));
            Parent root = loader.load();

            EditStudentController controller = loader.getController();
            controller.setStudent(student);
            controller.setAdminController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Student");
            stage.show();
        } catch (IOException e) {
            logger.error("Error opening edit student dialog", e);
            showError("Error opening edit student dialog: " + e.getMessage());
        }
    }

    private void handleDeleteStudent(Student student) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Student");
        alert.setHeaderText("Delete Student");
        alert.setContentText("Are you sure you want to delete student " + student.getName() + "?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                userDAO.deleteStudent(student.getUsername());
                refreshStudentList();
            } catch (Exception e) {
                logger.error("Error deleting student", e);
                showError("Error deleting student: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleGenerateReport() {
        String selectedReport = reportTypeComboBox.getValue();
        if (selectedReport == null) {
            showError("Please select a report type");
            return;
        }

        try {
            String report = switch (selectedReport) {
                case "Student List" -> generateStudentListReport();
                case "Course Enrollment" -> generateCourseEnrollmentReport();
                case "Program Statistics" -> generateProgramStatisticsReport();
                default -> "Unknown report type";
            };
            reportArea.setText(report);
        } catch (Exception e) {
            logger.error("Error generating report", e);
            showError("Error generating report: " + e.getMessage());
        }
    }

    private String generateStudentListReport() {
        List<Student> students = userDAO.getAllStudents();
        StringBuilder report = new StringBuilder("Student List Report\n\n");
        for (Student student : students) {
            report.append(String.format("Name: %s\nEmail: %s\nCourse: %s\nProgram: %s\n\n",
                student.getName(), student.getEmail(), student.getCourse(), student.getProgram()));
        }
        return report.toString();
    }

    private String generateCourseEnrollmentReport() {
        // TODO: Implement course enrollment statistics
        return "Course Enrollment Report\n\nThis report is not yet implemented.";
    }

    private String generateProgramStatisticsReport() {
        // TODO: Implement program statistics
        return "Program Statistics Report\n\nThis report is not yet implemented.";
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 