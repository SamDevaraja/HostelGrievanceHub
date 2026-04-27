package com.hostelgrievancehub.model;

import java.sql.Timestamp;

public class ComplaintNote {
    private int id;
    private int complaintId;
    private int staffId;
    private String note;
    private Timestamp createdAt;

    public ComplaintNote() {}

    public ComplaintNote(int id, int complaintId, int staffId, String note, Timestamp createdAt) {
        this.id = id;
        this.complaintId = complaintId;
        this.staffId = staffId;
        this.note = note;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getComplaintId() { return complaintId; }
    public void setComplaintId(int complaintId) { this.complaintId = complaintId; }

    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
