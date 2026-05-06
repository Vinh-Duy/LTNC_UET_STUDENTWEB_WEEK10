package com.student;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StudentApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load file FXML từ thư mục resources
        Parent root = FXMLLoader.load(getClass().getResource("/views/student_form.fxml"));
        
        primaryStage.setTitle("Hệ thống Đăng ký Sinh viên");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}