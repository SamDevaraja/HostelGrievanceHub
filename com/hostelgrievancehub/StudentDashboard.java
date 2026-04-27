package com.hostelgrievancehub;

import com.hostelgrievancehub.controller.UserController;
import com.hostelgrievancehub.dao.ComplaintDAO;
import com.hostelgrievancehub.model.Complaint;
import com.hostelgrievancehub.model.Notification;
import com.hostelgrievancehub.model.Staff;
import com.hostelgrievancehub.model.Student;
import com.hostelgrievancehub.util.Base64Util;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.List;

import com.hostelgrievancehub.controller.StaffController;

// Commented out JFreeChart imports to avoid compilation errors in headless environment
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class StudentDashboard extends JFrame {
    private Student student;
    private ComplaintDAO complaintDAO;
    private StaffController staffController;
    private JTable complaintsTable;
    private DefaultTableModel tableModel;

    public StudentDashboard(Student student) {
        this.student = student;
        this.complaintDAO = new ComplaintDAO();
        this.staffController = new StaffController();
        initializeUI();
        loadComplaints();
    }

    private void initializeUI() {
        setTitle("Student Dashboard - Hostel Grievance Hub");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // Modern light theme
        getContentPane().setBackground(new Color(240, 240, 240));

        // Header panel with modern styling
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 255, 255));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));

        // Logo and title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(255, 255, 255));

        JLabel logoLabel = new JLabel("🏢", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        titlePanel.add(logoLabel);

        JLabel headerLabel = new JLabel("Hostel Grievance Hub", SwingConstants.LEFT);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0, 102, 204));
        titlePanel.add(headerLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // User info and logout
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(new Color(255, 255, 255));

        JLabel userLabel = new JLabel("👤 " + student.getName() + " (" + student.getHostel() + ")");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.BLACK);
        userPanel.add(userLabel);

        JButton logoutButton = createModernButton("Logout", new Color(220, 53, 69));
        logoutButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });
        userPanel.add(logoutButton);

        headerPanel.add(userPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(Color.BLACK);

        // Complaints tab
        JPanel complaintsPanel = new JPanel(new BorderLayout());
        complaintsPanel.setBackground(Color.WHITE);
        complaintsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Left panel - Complaint form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel formTitle = new JLabel("📝 Submit New Complaint", SwingConstants.CENTER);
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(new Color(0, 102, 204));
        formPanel.add(formTitle, gbc);

        // Title field
        gbc.gridy = 1; gbc.gridwidth = 1;
        JTextField titleField = new JTextField(18);
        JPanel titleInputPanel = createInputPanel("📋 Title:", titleField);
        formPanel.add(titleInputPanel, gbc);

        // Room field
        gbc.gridy = 2;
        JTextField roomField = new JTextField(18);
        JPanel roomPanel = createInputPanel("🏠 Room Number:", roomField);
        formPanel.add(roomPanel, gbc);

        // Category
        gbc.gridy = 3;
        JPanel categoryPanel = new JPanel(new BorderLayout());
        categoryPanel.setBackground(new Color(255, 255, 255));
        JLabel categoryLabel = new JLabel("🔧 Category:");
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryLabel.setForeground(Color.BLACK);
        categoryPanel.add(categoryLabel, BorderLayout.WEST);

        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{"Plumbing", "Carpentry", "Electrical", "General"});
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryCombo.setBackground(Color.WHITE);
        categoryCombo.setForeground(Color.BLACK);
        categoryCombo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        categoryPanel.add(categoryCombo, BorderLayout.CENTER);
        formPanel.add(categoryPanel, gbc);

        // Description
        gbc.gridy = 4;
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBackground(new Color(255, 255, 255));
        JLabel descLabel = new JLabel("📝 Description:");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(Color.BLACK);
        descPanel.add(descLabel, BorderLayout.NORTH);

        JTextArea descArea = new JTextArea(4, 18);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setBackground(Color.WHITE);
        descArea.setForeground(Color.BLACK);
        descArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        descArea.setCaretColor(Color.BLACK);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(200, 80));
        descPanel.add(descScroll, BorderLayout.CENTER);
        formPanel.add(descPanel, gbc);

        // Priority
        gbc.gridy = 5;
        JPanel priorityPanel = new JPanel(new BorderLayout());
        priorityPanel.setBackground(new Color(255, 255, 255));
        JLabel priorityLabel = new JLabel("⚡ Priority:");
        priorityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        priorityLabel.setForeground(Color.BLACK);
        priorityPanel.add(priorityLabel, BorderLayout.WEST);

        JComboBox<String> priorityCombo = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        priorityCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        priorityCombo.setBackground(Color.WHITE);
        priorityCombo.setForeground(Color.BLACK);
        priorityCombo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        priorityPanel.add(priorityCombo, BorderLayout.CENTER);
        formPanel.add(priorityPanel, gbc);

        // Submit button
        gbc.gridy = 6; gbc.gridwidth = 2;
        JButton submitButton = createModernButton("🚀 Submit Complaint", new Color(34, 139, 34));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText().trim();
                String room = roomField.getText().trim();
                String category = (String) categoryCombo.getSelectedItem();
                String description = descArea.getText().trim();
                String priority = (String) priorityCombo.getSelectedItem();

                if (title.isEmpty() || room.isEmpty() || description.isEmpty()) {
                    JOptionPane.showMessageDialog(StudentDashboard.this, "Please fill all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Complaint complaint = new Complaint();
                complaint.setStudentId(student.getId());
                complaint.setTitle(title);
                complaint.setRoomNumber(room);
                complaint.setCategory(category);
                complaint.setDescription(description);
                complaint.setPriority(priority);
                complaint.setStatus("Pending");
                UserController userController = new UserController();
                Staff assignedStaff = userController.getStaffByScope(student.getHostel());
                if (assignedStaff != null) {
                    complaint.setAssignedStaffId(assignedStaff.getId());
                } else {
                    // Fallback to admin if no specific staff for hostel
                    Staff admin = userController.getStaffByScope("ALL");
                    if (admin != null) {
                        complaint.setAssignedStaffId(admin.getId());
                    } else {
                        complaint.setAssignedStaffId(1); // Default fallback
                    }
                }
                complaint.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                complaint.setUpdatedAt(new Timestamp(System.currentTimeMillis()));



                try {
                    complaintDAO.addComplaint(complaint);
                    JOptionPane.showMessageDialog(StudentDashboard.this, "Complaint submitted successfully.");
                    loadComplaints();
                    // Clear form
                    titleField.setText("");
                    roomField.setText("");
                    descArea.setText("");

                    // Notify staff about new complaint
                    staffController.sendNotification(complaint.getAssignedStaffId(), "New complaint registered: " + complaint.getTitle() + " from " + student.getName() + " (" + student.getHostel() + ")");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(StudentDashboard.this, "Failed to submit complaint.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        formPanel.add(submitButton, gbc);

        mainPanel.add(formPanel, BorderLayout.WEST);

        // Right panel - Complaints table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(255, 255, 255));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Table title
        JLabel tableTitle = new JLabel("📋 Your Complaints", SwingConstants.CENTER);
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(new Color(0, 102, 204));
        tablePanel.add(tableTitle, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Title", "Status", "Priority", "Created"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        complaintsTable = new JTable(tableModel);
        styleTable(complaintsTable);
        JScrollPane tableScroll = new JScrollPane(complaintsTable);
        tableScroll.setPreferredSize(new Dimension(600, 500));
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        complaintsPanel.add(mainPanel, BorderLayout.CENTER);

        tabbedPane.addTab("📋 Complaints", complaintsPanel);

        // Analytics tab
        JPanel analyticsPanel = new JPanel(new BorderLayout());
        analyticsPanel.setBackground(Color.WHITE);
        analyticsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Title for analytics
        JLabel analyticsTitle = new JLabel("📊 Your Complaint Analytics", SwingConstants.CENTER);
        analyticsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        analyticsTitle.setForeground(new Color(0, 102, 204));
        analyticsPanel.add(analyticsTitle, BorderLayout.NORTH);

        // Analytics content with charts
        JPanel analyticsContent = new JPanel(new GridLayout(1, 2, 20, 20));
        analyticsContent.setBackground(Color.WHITE);
        analyticsContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Pie chart for status distribution
        JPanel pieChartPanel = new JPanel(new BorderLayout());
        pieChartPanel.setBackground(Color.WHITE);
        pieChartPanel.setBorder(BorderFactory.createTitledBorder("📊 Your Status Distribution"));
        JFreeChart pieChart = createStudentStatusPieChart();
        ChartPanel pieChartPanelComponent = new ChartPanel(pieChart);
        pieChartPanelComponent.setPreferredSize(new Dimension(400, 300));
        pieChartPanel.add(pieChartPanelComponent, BorderLayout.CENTER);
        analyticsContent.add(pieChartPanel);

        // Bar chart for monthly trends
        JPanel barChartPanel = new JPanel(new BorderLayout());
        barChartPanel.setBackground(Color.WHITE);
        barChartPanel.setBorder(BorderFactory.createTitledBorder("📈 Your Monthly Trends"));
        JFreeChart barChart = createStudentMonthlyBarChart();
        ChartPanel barChartPanelComponent = new ChartPanel(barChart);
        barChartPanelComponent.setPreferredSize(new Dimension(400, 300));
        barChartPanel.add(barChartPanelComponent, BorderLayout.CENTER);
        analyticsContent.add(barChartPanel);

        analyticsPanel.add(analyticsContent, BorderLayout.CENTER);

        // Summary cards below charts
        JPanel summaryPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        JPanel totalPanel = createAnalyticsCard("📋 Total", "0", new Color(0, 102, 204));
        summaryPanel.add(totalPanel);

        JPanel resolvedPanel = createAnalyticsCard("✅ Resolved", "0", new Color(34, 139, 34));
        summaryPanel.add(resolvedPanel);

        JPanel pendingPanel = createAnalyticsCard("⏳ Pending", "0", new Color(255, 193, 7));
        summaryPanel.add(pendingPanel);

        JPanel ongoingPanel = createAnalyticsCard("🔄 Ongoing", "0", new Color(23, 162, 184));
        summaryPanel.add(ongoingPanel);

        JPanel escalatedPanel = createAnalyticsCard("🚨 Escalated", "0", new Color(220, 53, 69));
        summaryPanel.add(escalatedPanel);

        analyticsPanel.add(summaryPanel, BorderLayout.SOUTH);

        // Load analytics data
        loadStudentAnalyticsData(totalPanel, resolvedPanel, pendingPanel, ongoingPanel, escalatedPanel);
        tabbedPane.addTab("📊 Analytics", analyticsPanel);

        // Notifications tab
        JPanel notifPanel = new JPanel(new BorderLayout());
        notifPanel.setBackground(Color.WHITE);
        notifPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Notifications title
        JLabel notifTitle = new JLabel("🔔 Notifications Center", SwingConstants.CENTER);
        notifTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        notifTitle.setForeground(new Color(0, 102, 204));
        notifPanel.add(notifTitle, BorderLayout.NORTH);

        // Notifications controls
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlsPanel.setBackground(Color.WHITE);

        JComboBox<String> filterCombo = new JComboBox<>(new String[]{"All", "Unread", "Read"});
        filterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        controlsPanel.add(new JLabel("Filter:"));
        controlsPanel.add(filterCombo);

        JButton markAllReadButton = new JButton("Mark All as Read");
        markAllReadButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        markAllReadButton.setForeground(Color.WHITE);
        markAllReadButton.setBackground(new Color(34, 139, 34));
        markAllReadButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        markAllReadButton.setFocusPainted(false);
        markAllReadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        controlsPanel.add(markAllReadButton);

        JButton deleteReadButton = new JButton("Delete Read");
        deleteReadButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteReadButton.setForeground(Color.WHITE);
        deleteReadButton.setBackground(new Color(220, 53, 69));
        deleteReadButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        deleteReadButton.setFocusPainted(false);
        deleteReadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        controlsPanel.add(deleteReadButton);

        notifPanel.add(controlsPanel, BorderLayout.NORTH);

        // Notifications table
        DefaultTableModel notifTableModel = new DefaultTableModel(new String[]{"ID", "📝 Message", "📅 Date", "📊 Status", "⚙️ Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Actions column
            }
        };
        JTable notifTable = new JTable(notifTableModel);
        notifTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        notifTable.setBackground(Color.WHITE);
        notifTable.setForeground(Color.BLACK);
        notifTable.setGridColor(new Color(200, 200, 200));
        notifTable.setSelectionBackground(new Color(173, 216, 230));
        notifTable.setSelectionForeground(Color.BLACK);
        notifTable.getTableHeader().setBackground(new Color(0, 102, 204));
        notifTable.getTableHeader().setForeground(Color.WHITE);
        notifTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        JScrollPane notifScroll = new JScrollPane(notifTable);
        notifScroll.setPreferredSize(new Dimension(600, 400));
        notifPanel.add(notifScroll, BorderLayout.CENTER);

        // Add button column for notification actions
        Action notifAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int row = Integer.valueOf(e.getActionCommand());
                int id = (int) notifTableModel.getValueAt(row, 0);
                String status = (String) notifTableModel.getValueAt(row, 3);
                if ("Unread".equals(status)) {
                    // Mark as read
                    try {
                        if (staffController.markNotificationAsRead(id)) {
                            loadStudentNotifications(notifTableModel, filterCombo.getSelectedItem().toString());
                        } else {
                            JOptionPane.showMessageDialog(StudentDashboard.this, "Failed to mark as read.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(StudentDashboard.this, "Failed to mark as read.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Delete notification
                    try {
                        // Remove from list (in real implementation, delete from database)
                        notifTableModel.removeRow(row);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(StudentDashboard.this, "Failed to delete notification.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
        com.hostelgrievancehub.util.ButtonColumn buttonColumn = new com.hostelgrievancehub.util.ButtonColumn(notifTable, notifAction, 4);

        // Load notifications
        loadStudentNotifications(notifTableModel, "All");

        // Filter action
        filterCombo.addActionListener(e -> loadStudentNotifications(notifTableModel, filterCombo.getSelectedItem().toString()));

        // Mark all read action
        markAllReadButton.addActionListener(e -> {
            try {
                staffController.getNotificationsByUser(student.getId()).forEach(n -> n.setRead(true));
                loadStudentNotifications(notifTableModel, filterCombo.getSelectedItem().toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(StudentDashboard.this, "Failed to mark all as read.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Delete read action
        deleteReadButton.addActionListener(e -> {
            try {
                // In real implementation, delete from database
                for (int i = notifTableModel.getRowCount() - 1; i >= 0; i--) {
                    if ("Read".equals(notifTableModel.getValueAt(i, 3))) {
                        notifTableModel.removeRow(i);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(StudentDashboard.this, "Failed to delete read notifications.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        tabbedPane.addTab("🔔 Notifications", notifPanel);

        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createInputPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 255, 255));

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
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
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

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setGridColor(new Color(200, 200, 200));
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(0, 102, 204));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    }

    private void loadComplaints() {
        tableModel.setRowCount(0);
        try {
            List<Complaint> complaints = complaintDAO.getComplaintsByStudent(student.getId());
            for (Complaint c : complaints) {
                tableModel.addRow(new Object[]{c.getId(), c.getTitle(), c.getStatus(), c.getPriority(), c.getCreatedAt()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load complaints.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Commented out chart creation methods due to JFreeChart issues in headless environment
    private JFreeChart createStudentStatusPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        try {
            List<Complaint> complaints = complaintDAO.getComplaintsByStudent(student.getId());
            int pending = 0, ongoing = 0, resolved = 0, escalated = 0;
            for (Complaint c : complaints) {
                switch (c.getStatus()) {
                    case "Pending": pending++; break;
                    case "Ongoing": ongoing++; break;
                    case "Resolved": resolved++; break;
                    case "Escalated": escalated++; break;
                }
            }
            dataset.setValue("Pending", pending);
            dataset.setValue("Ongoing", ongoing);
            dataset.setValue("Resolved", resolved);
            dataset.setValue("Escalated", escalated);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFreeChart chart = ChartFactory.createPieChart("Your Complaint Status Distribution", dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Pending", new Color(255, 193, 7));
        plot.setSectionPaint("Ongoing", new Color(23, 162, 184));
        plot.setSectionPaint("Resolved", new Color(34, 139, 34));
        plot.setSectionPaint("Escalated", new Color(220, 53, 69));
        return chart;
    }

    private JFreeChart createStudentMonthlyBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            List<Complaint> complaints = complaintDAO.getComplaintsByStudent(student.getId());
            // Simple monthly aggregation (last 6 months)
            for (int i = 5; i >= 0; i--) {
                final int currentMonth = (java.time.LocalDate.now().getMonthValue() - i + 12) % 12;
                final int finalMonth = currentMonth == 0 ? 12 : currentMonth;
                String monthName = java.time.Month.of(finalMonth).name().substring(0, 3);
                long count = complaints.stream()
                    .filter(c -> c.getCreatedAt() != null &&
                        java.time.LocalDate.ofInstant(c.getCreatedAt().toInstant(), java.time.ZoneId.systemDefault()).getMonthValue() == finalMonth)
                    .count();
                dataset.addValue(count, "Complaints", monthName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ChartFactory.createBarChart("Your Monthly Complaint Trends", "Month", "Count", dataset, PlotOrientation.VERTICAL, false, true, false);
    }

    private void loadStudentAnalyticsData(JPanel totalPanel, JPanel resolvedPanel, JPanel pendingPanel, JPanel ongoingPanel, JPanel escalatedPanel) {
        try {
            List<Complaint> complaints = complaintDAO.getComplaintsByStudent(student.getId());

            int total = complaints.size();
            int resolved = 0;
            int pending = 0;
            int escalated = 0;
            int ongoing = 0;

            for (Complaint c : complaints) {
                switch (c.getStatus()) {
                    case "Resolved":
                        resolved++;
                        break;
                    case "Pending":
                        pending++;
                        break;
                    case "Escalated":
                        escalated++;
                        break;
                    case "Ongoing":
                        ongoing++;
                        break;
                }
            }

            // Update the labels
            ((JLabel) totalPanel.getComponent(1)).setText(String.valueOf(total));
            ((JLabel) resolvedPanel.getComponent(1)).setText(String.valueOf(resolved));
            ((JLabel) pendingPanel.getComponent(1)).setText(String.valueOf(pending));
            ((JLabel) escalatedPanel.getComponent(1)).setText(String.valueOf(escalated));
            ((JLabel) ongoingPanel.getComponent(1)).setText(String.valueOf(ongoing));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStudentNotifications(DefaultTableModel notifTableModel, String filter) {
        notifTableModel.setRowCount(0);
        try {
            List<Notification> notifications = staffController.getNotificationsByUser(student.getId());
            for (Notification n : notifications) {
                if ("All".equals(filter) ||
                    ("Unread".equals(filter) && !n.isRead()) ||
                    ("Read".equals(filter) && n.isRead())) {
                    String status = n.isRead() ? "Read" : "Unread";
                    String action = n.isRead() ? "Delete" : "Mark Read";
                    notifTableModel.addRow(new Object[]{n.getId(), n.getMessage(), n.getCreatedAt(), status, action});
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load notifications.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createAnalyticsCard(String title, String value, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        card.add(titleLabel, BorderLayout.NORTH);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }
}
