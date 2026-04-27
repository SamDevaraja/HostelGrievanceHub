package com.hostelgrievancehub.dao;

import com.hostelgrievancehub.model.Student;
import com.hostelgrievancehub.model.Staff;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (name, email, password, hostel) VALUES (?, ?, ?, ?)";
        try (Connection conn = MySQLDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getPassword());
            stmt.setString(4, student.getHostel());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                student.setId(rs.getInt(1));
            }
        }
    }

    public void addStaff(Staff staff) throws SQLException {
        String sql = "INSERT INTO staff (name, email, password, scope) VALUES (?, ?, ?, ?)";
        try (Connection conn = MySQLDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, staff.getName());
            stmt.setString(2, staff.getEmail());
            stmt.setString(3, staff.getPassword());
            stmt.setString(4, staff.getScope());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                staff.setId(rs.getInt(1));
            }
        }
    }

    public Student getStudentByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM students WHERE email = ?";
        try (Connection conn = MySQLDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
        }
        return null;
    }

    public Staff getStaffByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM staff WHERE email = ?";
        try (Connection conn = MySQLDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToStaff(rs);
            }
        }
        return null;
    }

    public Student getStudentById(int id) throws SQLException {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = MySQLDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
        }
        return null;
    }

    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff";
        try (Connection conn = MySQLDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                staffList.add(mapResultSetToStaff(rs));
            }
        }
        return staffList;
    }

    public Staff getStaffByScope(String scope) throws SQLException {
        String sql = "SELECT * FROM staff WHERE scope = ? LIMIT 1";
        try (Connection conn = MySQLDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, scope);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToStaff(rs);
            }
        }
        return null;
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setName(rs.getString("name"));
        student.setEmail(rs.getString("email"));
        student.setPassword(rs.getString("password"));
        student.setHostel(rs.getString("hostel"));
        return student;
    }

    private Staff mapResultSetToStaff(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setId(rs.getInt("id"));
        staff.setName(rs.getString("name"));
        staff.setEmail(rs.getString("email"));
        staff.setPassword(rs.getString("password"));
        staff.setScope(rs.getString("scope"));
        return staff;
    }
}
