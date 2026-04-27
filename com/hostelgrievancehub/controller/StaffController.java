package com.hostelgrievancehub.controller;

import com.hostelgrievancehub.dao.ComplaintDAO;
import com.hostelgrievancehub.dao.NoteDAO;
import com.hostelgrievancehub.dao.NotificationDAO;
import com.hostelgrievancehub.dao.UserDAO;
import com.hostelgrievancehub.model.Complaint;
import com.hostelgrievancehub.model.ComplaintNote;
import com.hostelgrievancehub.model.Notification;
import com.hostelgrievancehub.model.Staff;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class StaffController {
    private ComplaintDAO complaintDAO;
    private NoteDAO noteDAO;
    private NotificationDAO notificationDAO;
    private UserDAO userDAO;

    public StaffController() {
        this.complaintDAO = new ComplaintDAO();
        this.noteDAO = new NoteDAO();
        this.notificationDAO = new NotificationDAO();
        this.userDAO = new UserDAO();
    }

    public List<Complaint> getComplaintsForStaff(int staffId) {
        try {
            return complaintDAO.getComplaintsByStaff(staffId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateComplaintStatus(int complaintId, String status) {
        try {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            complaintDAO.updateComplaintStatus(complaintId, status, now);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addNoteToComplaint(int complaintId, int staffId, String note) {
        try {
            ComplaintNote complaintNote = new ComplaintNote();
            complaintNote.setComplaintId(complaintId);
            complaintNote.setStaffId(staffId);
            complaintNote.setNote(note);
            complaintNote.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            noteDAO.addNote(complaintNote);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ComplaintNote> getNotesForComplaint(int complaintId) {
        try {
            return noteDAO.getNotesByComplaint(complaintId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Staff> getAllStaff() {
        try {
            return userDAO.getAllStaff();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean sendNotification(int userId, String message) {
        try {
            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setMessage(message);
            notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            notification.setRead(false);
            notificationDAO.addNotification(notification);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Notification> getNotificationsByUser(int userId) {
        try {
            return notificationDAO.getNotificationsByUser(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean markNotificationAsRead(int notificationId) {
        try {
            notificationDAO.markAsRead(notificationId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
