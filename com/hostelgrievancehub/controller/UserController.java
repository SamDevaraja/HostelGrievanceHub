package com.hostelgrievancehub.controller;

import com.hostelgrievancehub.dao.UserDAO;
import com.hostelgrievancehub.model.Student;
import com.hostelgrievancehub.model.Staff;
import java.sql.SQLException;

public class UserController {
    private UserDAO userDAO;

    public UserController() {
        this.userDAO = new UserDAO();
    }

    public boolean registerStudent(String name, String email, String password, String hostel) {
        try {
            Student student = new Student();
            student.setName(name);
            student.setEmail(email);
            student.setPassword(password);
            student.setHostel(hostel);
            userDAO.addStudent(student);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerStaff(String name, String email, String password, String scope) {
        try {
            Staff staff = new Staff();
            staff.setName(name);
            staff.setEmail(email);
            staff.setPassword(password);
            staff.setScope(scope);
            userDAO.addStaff(staff);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Staff getStaffByScope(String scope) {
        try {
            return userDAO.getStaffByScope(scope);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Student authenticateStudent(String email, String password) {
        try {
            Student student = userDAO.getStudentByEmail(email);
            if (student != null && student.getPassword().equals(password)) {
                return student;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Staff authenticateStaff(String email, String password) {
        try {
            Staff staff = userDAO.getStaffByEmail(email);
            if (staff != null && staff.getPassword().equals(password)) {
                return staff;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Student getStudentById(int id) {
        try {
            return userDAO.getStudentById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
