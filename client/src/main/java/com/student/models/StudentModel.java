package com.student.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentModel {
    // SỬA TÀI KHOẢN VÀ MẬT KHẨU MYSQL CỦA BẠN Ở ĐÂY
    private static final String URL = "jdbc:mysql://localhost:3306/student_db";
    private static final String USER = "root"; 
    private static final String PASS = ""; 

    // Hàm Save (Lưu vào DB)
    public boolean saveStudent(String name, String studentId, String gender, String services) {
        String sql = "INSERT INTO students (full_name, student_code, gender, services) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, studentId);
            pstmt.setString(3, gender);
            pstmt.setString(4, services);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm Load (Lấy dữ liệu người vừa đăng ký gần nhất)
    public String[] loadLastStudent() {
        String sql = "SELECT * FROM students ORDER BY id DESC LIMIT 1";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return new String[]{
                    rs.getString("full_name"),
                    rs.getString("student_code"),
                    rs.getString("gender"),
                    rs.getString("services")
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}