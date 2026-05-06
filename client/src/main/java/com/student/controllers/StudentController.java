package com.student.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

public class StudentController {

    // Nhúng các thành phần giao diện từ file FXML sang
    @FXML private TextField txtName;
    @FXML private TextField txtStudentId;
    @FXML private ToggleGroup genderGroup;
    @FXML private CheckBox chkKtx;
    @FXML private CheckBox chkXe;
    @FXML private CheckBox chkThuVien;
    @FXML private Label lblMessage; // Nơi hiển thị thông báo lỗi/thành công

    // Xử lý sự kiện khi bấm nút "Xác nhận Đăng ký"
    @FXML
    public void handleSubmit(ActionEvent event) {
        // 1. Đọc dữ liệu từ View
        String name = txtName.getText().trim();
        String studentId = txtStudentId.getText().trim();

        // 2. Kiểm tra dữ liệu (Validation)
        if (name.isEmpty()) {
            showMessage("Lỗi: Họ và tên không được để trống!", Color.RED);
            return;
        }

        if (studentId.isEmpty()) {
            showMessage("Lỗi: Mã sinh viên không được để trống!", Color.RED);
            return;
        }

        // Bắt lỗi sai định dạng: Mã SV phải là 8 chữ số
        if (!studentId.matches("\\d{8}")) { 
            showMessage("Lỗi: Mã sinh viên phải bao gồm đúng 8 chữ số (VD: 25020082)!", Color.RED);
            return;
        }

        // Lấy giới tính đang được chọn
        RadioButton selectedGender = (RadioButton) genderGroup.getSelectedToggle();
        String gender = selectedGender.getText();

        // 3. Phản hồi trực quan khi thành công
        showMessage("Thành công! Đã ghi nhận SV: " + name + " (" + gender + ")", Color.GREEN);
    }

    // Xử lý sự kiện khi bấm nút "Làm mới Form"
    @FXML
    public void handleReset(ActionEvent event) {
        txtName.clear();
        txtStudentId.clear();
        genderGroup.selectToggle(genderGroup.getToggles().get(0)); // Mặc định về Nam
        chkKtx.setSelected(false);
        chkXe.setSelected(false);
        chkThuVien.setSelected(false);
        lblMessage.setText(""); // Xóa thông báo
    }

    // Hàm phụ trợ để in thông báo màu sắc
    private void showMessage(String message, Color color) {
        lblMessage.setText(message);
        lblMessage.setTextFill(color);
    }
}