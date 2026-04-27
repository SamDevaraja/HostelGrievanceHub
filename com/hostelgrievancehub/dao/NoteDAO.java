package com.hostelgrievancehub.dao;

import com.hostelgrievancehub.model.ComplaintNote;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO {

    public void addNote(ComplaintNote note) throws SQLException {
        String sql = "INSERT INTO complaint_notes (complaint_id, staff_id, note, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = MySQLDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, note.getComplaintId());
            stmt.setInt(2, note.getStaffId());
            stmt.setString(3, note.getNote());
            stmt.setTimestamp(4, note.getCreatedAt());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                note.setId(rs.getInt(1));
            }
        }
    }

    public List<ComplaintNote> getNotesByComplaint(int complaintId) throws SQLException {
        List<ComplaintNote> notes = new ArrayList<>();
        String sql = "SELECT * FROM complaint_notes WHERE complaint_id = ? ORDER BY created_at ASC";
        try (Connection conn = MySQLDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, complaintId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notes.add(mapResultSetToNote(rs));
            }
        }
        return notes;
    }

    private ComplaintNote mapResultSetToNote(ResultSet rs) throws SQLException {
        ComplaintNote note = new ComplaintNote();
        note.setId(rs.getInt("id"));
        note.setComplaintId(rs.getInt("complaint_id"));
        note.setStaffId(rs.getInt("staff_id"));
        note.setNote(rs.getString("note"));
        note.setCreatedAt(rs.getTimestamp("created_at"));
        return note;
    }
}
