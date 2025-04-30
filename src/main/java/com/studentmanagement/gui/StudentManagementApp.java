package com.studentmanagement.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentManagementApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(StudentManagementApp.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Student Management System");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            logger.error("Error starting application", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 