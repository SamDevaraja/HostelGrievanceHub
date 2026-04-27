package com.hostelgrievancehub;

import com.hostelgrievancehub.controller.UserController;
import com.hostelgrievancehub.model.Student;
import com.hostelgrievancehub.model.Staff;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton loginButton;
    private JButton signUpButton;
    private JButton aboutButton;
    private UserController userController;

    public LoginFrame() {
        userController = new UserController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Hostel Grievance Hub - Login");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // Light theme
        getContentPane().setBackground(new Color(240, 240, 240));

        // Header panel with logo and title
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        // Logo placeholder (you can add an actual icon later)
        JLabel logoLabel = new JLabel("🏢", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        headerPanel.add(logoLabel, BorderLayout.NORTH);

        JLabel headerLabel = new JLabel("Hostel Grievance Hub", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(new Color(100, 150, 255));
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        JLabel subtitleLabel = new JLabel("Secure Login Portal", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitleLabel.setForeground(new Color(150, 150, 150));
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // Main form panel with modern styling
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email field with icon
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JPanel emailPanel = createInputPanel("📧 Email Address:", emailField = new JTextField(20));
        formPanel.add(emailPanel, gbc);

        // Password field with icon
        gbc.gridy = 1;
        JPanel passwordPanel = createInputPanel("🔒 Password:", passwordField = new JPasswordField(20));
        formPanel.add(passwordPanel, gbc);

        // Role selection with modern combo box
        gbc.gridy = 2;
        JPanel rolePanel = new JPanel(new BorderLayout());
        rolePanel.setBackground(Color.WHITE);
        JLabel roleLabel = new JLabel("👤 Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleLabel.setForeground(Color.BLACK);
        rolePanel.add(roleLabel, BorderLayout.WEST);

        roleComboBox = new JComboBox<>(new String[]{"Student", "Staff"});
        roleComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleComboBox.setBackground(Color.WHITE);
        roleComboBox.setForeground(Color.BLACK);
        roleComboBox.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        rolePanel.add(roleComboBox, BorderLayout.CENTER);
        formPanel.add(rolePanel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button panel with modern buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.setBorder(new EmptyBorder(20, 20, 30, 20));

        loginButton = createModernButton("Login", new Color(100, 150, 255));
        signUpButton = createModernButton("Sign Up", new Color(70, 130, 180));
        aboutButton = createModernButton("About", new Color(100, 100, 100));

        loginButton.addActionListener(new LoginAction());
        signUpButton.addActionListener(e -> {
            new SignupFrame();
            dispose();
        });
        aboutButton.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "Hostel Grievance Hub v1.0\nA comprehensive complaint management system for college hostels.",
            "About", JOptionPane.INFORMATION_MESSAGE));

        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);
        buttonPanel.add(aboutButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createInputPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(Color.BLACK);
        panel.add(label, BorderLayout.NORTH);

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        field.setCaretColor(Color.BLACK);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("Student".equals(role)) {
                Student student = userController.authenticateStudent(email, password);
                if (student != null) {
                    new StudentDashboard(student);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                Staff staff = userController.authenticateStaff(email, password);
                if (staff != null) {
                    new StaffDashboard(staff);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
