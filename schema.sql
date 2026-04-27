-- HostelGrievanceHub Database Schema

CREATE DATABASE IF NOT EXISTS hostelhub;
USE hostelhub;

SET FOREIGN_KEY_CHECKS = 0;

-- Students Table
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    hostel VARCHAR(255) NOT NULL
);

-- Staff Table
CREATE TABLE IF NOT EXISTS staff (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    scope VARCHAR(255) NOT NULL
);

-- Complaints Table
CREATE TABLE IF NOT EXISTS complaints (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    room_number VARCHAR(50),
    category VARCHAR(100),
    description TEXT,
    priority VARCHAR(50),
    status VARCHAR(50) DEFAULT 'Pending',
    assigned_staff_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    image_base64 LONGTEXT,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (assigned_staff_id) REFERENCES staff(id)
);

-- Complaint Notes Table
CREATE TABLE IF NOT EXISTS complaint_notes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    complaint_id INT NOT NULL,
    staff_id INT,
    note TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (complaint_id) REFERENCES complaints(id),
    FOREIGN KEY (staff_id) REFERENCES staff(id)
);

-- Notifications Table
CREATE TABLE IF NOT EXISTS notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE
);

-- Initial Data for Testing
TRUNCATE TABLE staff;
INSERT INTO staff (name, email, password, scope) VALUES ('Admin', 'admin@hostel.com', 'Samdev@2005', 'ALL');

SET FOREIGN_KEY_CHECKS = 1;
