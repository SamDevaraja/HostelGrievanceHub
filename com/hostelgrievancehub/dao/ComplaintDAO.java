package com.hostelgrievancehub.dao;

import com.hostelgrievancehub.model.Complaint;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {

    public void addComplaint(Complaint complaint) throws SQLException {
        String sql = "INSERT INTO complaints (student_id, title, room_number, category, description, priority, status, assigned_staff_id, created_at, updated_at, image_base64) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = MySQLDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, complaint.getStudentId());
            stmt.setString(2, complaint.getTitle());
            stmt.setString(3, complaint.getRoomNumber());
            stmt.setString(4, complaint.getCategory());
            stmt.setString(5, complaint.getDescription());
            stmt.setString(6, complaint.getPriority());
            stmt.setString(7, complaint.getStatus());
            stmt.setInt(8, complaint.getAssignedStaffId());
            stmt.setTimestamp(9, complaint.getCreatedAt());
            stmt.setTimestamp(10, complaint.getUpdatedAt());
            stmt.setString(11, complaint.getImageBase64());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                complaint.setId(rs.getInt(1));
            }
        }
    }

    public List<Complaint> getComplaintsByStudent(int studentId) throws SQLException {
        List<Complaint> complaints = new ArrayList<>();
        String sql = "SELECT * FROM complaints WHERE student_id = ?";
        try (Connection conn = MySQLDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                complaints.add(mapResultSetToComplaint(rs));
            }
        }
        return complaints;
    }

    public List<Complaint> getComplaintsByStaff(int staffId) throws SQLException {
        List<Complaint> complaints = new ArrayList<>();
        String sql = "SELECT * FROM complaints WHERE assigned_staff_id = ?";
        try (Connection conn = MySQLDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, staffId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                complaints.add(mapResultSetToComplaint(rs));
            }
        }
        return complaints;
    }

    public void updateComplaintStatus(int complaintId, String status, Timestamp updatedAt) throws SQLException {
        String sql = "UPDATE complaints SET status = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = MySQLDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setTimestamp(2, updatedAt);
            stmt.setInt(3, complaintId);
            stmt.executeUpdate();
        }
    }

    public void updateComplaintPriority(int id, String priority) throws SQLException {
        String sql = "UPDATE complaints SET priority = ? WHERE id = ?";
        try (Connection conn = MySQLDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, priority);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public Complaint getComplaintById(int id) throws SQLException {
        String sql = "SELECT * FROM complaints WHERE id = ?";
        try (Connection conn = MySQLDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToComplaint(rs);
            }
        }
        return null;
    }

    private Complaint mapResultSetToComplaint(ResultSet rs) throws SQLException {
        Complaint complaint = new Complaint();
        complaint.setId(rs.getInt("id"));
        complaint.setStudentId(rs.getInt("student_id"));
        complaint.setTitle(rs.getString("title"));
        complaint.setRoomNumber(rs.getString("room_number"));
        complaint.setCategory(rs.getString("category"));
        complaint.setDescription(rs.getString("description"));
        complaint.setPriority(rs.getString("priority"));
        complaint.setStatus(rs.getString("status"));
        complaint.setAssignedStaffId(rs.getInt("assigned_staff_id"));
        complaint.setCreatedAt(rs.getTimestamp("created_at"));
        complaint.setUpdatedAt(rs.getTimestamp("updated_at"));
        complaint.setImageBase64(rs.getString("image_base64"));
        return complaint;
    }
}
