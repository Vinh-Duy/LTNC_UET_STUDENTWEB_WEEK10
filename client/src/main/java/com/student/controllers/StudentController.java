package com.student.controllers;

import com.student.models.StudentModel;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;

public class StudentController {

    @FXML private TextField txtName;
    @FXML private TextField txtStudentId;
    @FXML private ToggleGroup genderGroup;
    @FXML private CheckBox chkKtx;
    @FXML private CheckBox chkXe;
    @FXML private CheckBox chkThuVien;
    @FXML private Label lblMessage;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Button btnSubmit; // Cần thêm ID này vào FXML nếu chưa có

    private StudentModel model = new StudentModel();

    @FXML
    public void initialize() {
        // 1. KIỂM TRA DỮ LIỆU THỜI GIAN THỰC (Real-time Validation)
        txtStudentId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{8}") && !newValue.isEmpty()) {
                txtStudentId.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            } else {
                txtStudentId.setStyle(""); // Trở lại bình thường khi nhập đúng 8 số
            }
        });

        // 2. SỰ KIỆN BÀN PHÍM (Keyboard Events): Nhấn Enter chuyển focus
        txtName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                txtStudentId.requestFocus();
            }
        });

        // 3. PHÍM TẮT TOÀN CỤC (Shortcut Ctrl+S/Cmd+S)
        Platform.runLater(() -> {
            if (txtName.getScene() != null) {
                txtName.getScene().getAccelerators().put(
                    new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN),
                    () -> handleSubmit(null)
                );
            }
        });
    }

    // TÍNH NĂNG SAVE (ĐÃ REFACTOR ĐA LUỒNG)
    @FXML
    public void handleSubmit(ActionEvent event) {
        String name = txtName.getText().trim();
        String studentId = txtStudentId.getText().trim();

        if (name.isEmpty() || studentId.isEmpty() || !studentId.matches("\\d{8}")) {
            showMessage("Lỗi: Dữ liệu không hợp lệ! Hãy kiểm tra lại.", Color.RED);
            return;
        }

        RadioButton selectedGender = (RadioButton) genderGroup.getSelectedToggle();
        String gender = (selectedGender != null) ? selectedGender.getText() : "Khác";

        StringBuilder services = new StringBuilder();
        if (chkKtx.isSelected()) services.append("KTX, ");
        if (chkXe.isSelected()) services.append("Gửi Xe, ");
        if (chkThuVien.isSelected()) services.append("Thư Viện, ");

        // Hiển thị vòng xoay và thông báo chờ
        progressIndicator.setVisible(true);
        lblMessage.setText("Đang lưu dữ liệu...");
        lblMessage.setTextFill(Color.BLUE);

        // Chạy đa luồng để tránh đơ giao diện
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Giả lập xử lý nặng để thấy vòng xoay
                boolean isSaved = model.saveStudent(name, studentId, gender, services.toString());

                // Cập nhật giao diện an toàn với Platform.runLater
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    if (isSaved) {
                        showMessage("Lưu vào DB thành công: " + name, Color.GREEN);
                    } else {
                        showMessage("Lỗi DB: Không thể lưu (Có thể do trùng Mã SV)", Color.RED);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // TÍNH NĂNG LOAD (ĐÃ REFACTOR ĐA LUỒNG)
    @FXML
    public void handleLoad(ActionEvent event) {
        progressIndicator.setVisible(true);
        lblMessage.setText("Đang tải dữ liệu...");
        lblMessage.setTextFill(Color.BLUE);

        new Thread(() -> {
            try {
                Thread.sleep(1000); // Giả lập độ trễ
                String[] data = model.loadLastStudent();

                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    if (data != null) {
                        txtName.setText(data[0]);
                        txtStudentId.setText(data[1]);
                        
                        for (Toggle toggle : genderGroup.getToggles()) {
                            if (((RadioButton) toggle).getText().equals(data[2])) {
                                genderGroup.selectToggle(toggle);
                                break;
                            }
                        }

                        String services = data[3] != null ? data[3] : "";
                        chkKtx.setSelected(services.contains("KTX"));
                        chkXe.setSelected(services.contains("Gửi Xe"));
                        chkThuVien.setSelected(services.contains("Thư Viện"));

                        showMessage("Đã tải dữ liệu mới nhất từ Database!", Color.BLUE);
                    } else {
                        showMessage("Lỗi: Không tìm thấy dữ liệu nào!", Color.RED);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    public void handleReset(ActionEvent event) {
        txtName.clear();
        txtStudentId.clear();
        txtStudentId.setStyle(""); // Reset màu viền
        genderGroup.selectToggle(genderGroup.getToggles().get(0));
        chkKtx.setSelected(false);
        chkXe.setSelected(false);
        chkThuVien.setSelected(false);
        lblMessage.setText("");
        progressIndicator.setVisible(false);
    }

    private void showMessage(String message, Color color) {
        lblMessage.setText(message);
        lblMessage.setTextFill(color);
    }
}