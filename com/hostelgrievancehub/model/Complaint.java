package com.hostelgrievancehub.model;

import java.sql.Timestamp;

public class Complaint {
    private int id;
    private int studentId;
    private String title;
    private String roomNumber;
    private String category; // e.g., Plumbing, Carpentry, Electrical, General
    private String description;
    private String priority; // Low, Medium, High
    private String status; // Pending, Ongoing, Resolved, Escalated
    private int assignedStaffId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String imageBase64; // Optional

    public Complaint() {}

    public Complaint(int id, int studentId, String title, String roomNumber, String category, String description, String priority, String status, int assignedStaffId, Timestamp createdAt, Timestamp updatedAt, String imageBase64) {
        this.id = id;
        this.studentId = studentId;
        this.title = title;
        this.roomNumber = roomNumber;
        this.category = category;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.assignedStaffId = assignedStaffId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.imageBase64 = imageBase64;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getAssignedStaffId() { return assignedStaffId; }
    public void setAssignedStaffId(int assignedStaffId) { this.assignedStaffId = assignedStaffId; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
}
