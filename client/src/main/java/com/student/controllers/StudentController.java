package com.student.controllers;

import com.student.models.StudentModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

public class StudentController {

    @FXML private TextField txtName;
    @FXML private TextField txtStudentId;
    @FXML private ToggleGroup genderGroup;
    @FXML private CheckBox chkKtx;
    @FXML private CheckBox chkXe;
    @FXML private CheckBox chkThuVien;
    @FXML private Label lblMessage;

    private StudentModel model = new StudentModel();

    // TÍNH NĂNG SAVE
    @FXML
    public void handleSubmit(ActionEvent event) {
        String name = txtName.getText().trim();
        String studentId = txtStudentId.getText().trim();

        if (name.isEmpty() || studentId.isEmpty() || !studentId.matches("\\d{8}")) {
            showMessage("Lỗi: Dữ liệu không hợp lệ! Hãy kiểm tra lại.", Color.RED);
            return;
        }

        RadioButton selectedGender = (RadioButton) genderGroup.getSelectedToggle();
        String gender = selectedGender.getText();

        // Gộp các dịch vụ thành 1 chuỗi
        StringBuilder services = new StringBuilder();
        if (chkKtx.isSelected()) services.append("KTX, ");
        if (chkXe.isSelected()) services.append("Gửi Xe, ");
        if (chkThuVien.isSelected()) services.append("Thư Viện, ");

        // Gọi Model để Save vào Database
        boolean isSaved = model.saveStudent(name, studentId, gender, services.toString());

        if (isSaved) {
            showMessage("Lưu vào DB thành công: " + name, Color.GREEN);
        } else {
            showMessage("Lỗi DB: Không thể lưu (Có thể do trùng Mã SV)", Color.RED);
        }
    }

    // TÍNH NĂNG LOAD
    @FXML
    public void handleLoad(ActionEvent event) {
        String[] data = model.loadLastStudent();
        if (data != null) {
            txtName.setText(data[0]);
            txtStudentId.setText(data[1]);
            
            // Set Giới tính
            for (Toggle toggle : genderGroup.getToggles()) {
                if (((RadioButton) toggle).getText().equals(data[2])) {
                    genderGroup.selectToggle(toggle);
                    break;
                }
            }

            // Set Dịch vụ
            String services = data[3] != null ? data[3] : "";
            chkKtx.setSelected(services.contains("KTX"));
            chkXe.setSelected(services.contains("Gửi Xe"));
            chkThuVien.setSelected(services.contains("Thư Viện"));

            showMessage("Đã tải dữ liệu mới nhất từ Database!", Color.BLUE);
        } else {
            showMessage("Lỗi: Không tìm thấy dữ liệu nào trong DB!", Color.RED);
        }
    }

    @FXML
    public void handleReset(ActionEvent event) {
        txtName.clear();
        txtStudentId.clear();
        genderGroup.selectToggle(genderGroup.getToggles().get(0));
        chkKtx.setSelected(false);
        chkXe.setSelected(false);
        chkThuVien.setSelected(false);
        lblMessage.setText("");
    }

    private void showMessage(String message, Color color) {
        lblMessage.setText(message);
        lblMessage.setTextFill(color);
    }
}