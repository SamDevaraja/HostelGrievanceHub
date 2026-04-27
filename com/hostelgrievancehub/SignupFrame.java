package com.hostelgrievancehub;

import com.hostelgrievancehub.controller.UserController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SignupFrame extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleComboBox;
    private JButton signUpButton;
    private JButton backButton;
    private UserController userController;

    public SignupFrame() {
        userController = new UserController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Hostel Grievance Hub - Sign Up");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // Light theme
        getContentPane().setBackground(new Color(240, 240, 240));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        // Logo placeholder
        JLabel logoLabel = new JLabel("👤", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        headerPanel.add(logoLabel, BorderLayout.NORTH);

        JLabel headerLabel = new JLabel("Create Account", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(new Color(100, 150, 255));
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        JLabel subtitleLabel = new JLabel("Join Hostel Grievance Hub", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitleLabel.setForeground(new Color(150, 150, 150));
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // Main form panel with modern styling
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 30, 20, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 8, 12, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name field
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JPanel namePanel = createInputPanel("👤 Full Name:", nameField = new JTextField(20));
        formPanel.add(namePanel, gbc);

        // Email field
        gbc.gridy = 1;
        JPanel emailPanel = createInputPanel("📧 Email Address:", emailField = new JTextField(20));
        formPanel.add(emailPanel, gbc);

        // Password field
        gbc.gridy = 2;
        JPanel passwordPanel = createInputPanel("🔒 Password:", passwordField = new JPasswordField(20));
        formPanel.add(passwordPanel, gbc);

        // Confirm password field
        gbc.gridy = 3;
        JPanel confirmPanel = createInputPanel("🔒 Confirm Password:", confirmPasswordField = new JPasswordField(20));
        formPanel.add(confirmPanel, gbc);

        // Role selection
        gbc.gridy = 4;
        JPanel rolePanel = new JPanel(new BorderLayout());
        rolePanel.setBackground(Color.WHITE);
        JLabel roleLabel = new JLabel("👥 Account Type:");
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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.setBorder(new EmptyBorder(20, 20, 30, 20));

        signUpButton = createModernButton("Create Account", new Color(34, 139, 34));
        backButton = createModernButton("Back to Login", new Color(100, 100, 100));

        signUpButton.addActionListener(new SignUpAction());
        backButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        buttonPanel.add(signUpButton);
        buttonPanel.add(backButton);

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
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
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

    private class SignUpAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(SignupFrame.this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(SignupFrame.this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = false;
            String hostel = "GH1"; // default for student
            String scope = "ALL"; // default for staff

            if ("Student".equals(role)) {
                success = userController.registerStudent(name, email, password, hostel);
            } else {
                success = userController.registerStaff(name, email, password, scope);
            }

            if (success) {
                JOptionPane.showMessageDialog(SignupFrame.this, "Registration successful. Please login.");
                new LoginFrame();
                dispose();
            } else {
                JOptionPane.showMessageDialog(SignupFrame.this, "Registration failed. Email may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
