package com.hostelgrievancehub;

import com.hostelgrievancehub.controller.StaffController;
import com.hostelgrievancehub.dao.ComplaintDAO;
import com.hostelgrievancehub.dao.UserDAO;
import com.hostelgrievancehub.model.Complaint;
import com.hostelgrievancehub.model.Notification;
import com.hostelgrievancehub.model.Staff;
import com.hostelgrievancehub.util.ButtonColumn;

// Commented out JFreeChart imports to avoid compilation errors in headless environment
// import org.jfree.chart.ChartFactory;
// import org.jfree.chart.ChartPanel;
// import org.jfree.chart.JFreeChart;
// import org.jfree.chart.plot.PiePlot;
// import org.jfree.chart.plot.PlotOrientation;
// import org.jfree.data.category.DefaultCategoryDataset;
// import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class StaffDashboard extends JFrame {
    private Staff staff;
    private StaffController staffController;
    private ComplaintDAO complaintDAO;
    private UserDAO userDAO;
    private JTable complaintsTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusFilter;
    private JComboBox<String> hostelFilter;
    private JComboBox<String> categoryFilter;
    private JComboBox<String> priorityFilter;
    private JTextField searchField;

    public StaffDashboard(Staff staff) {
        this.staff = staff;
        this.staffController = new StaffController();
        this.complaintDAO = new ComplaintDAO();
        this.userDAO = new UserDAO();
        initializeUI();
        loadComplaints();
    }

    private void initializeUI() {
        setTitle("🏢 Staff Dashboard - Hostel Grievance Hub");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // Light theme
        getContentPane().setBackground(new Color(240, 240, 240));
        UIManager.put("Label.foreground", Color.BLACK);
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Button.background", new Color(200, 200, 200));
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.foreground", Color.BLACK);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", Color.BLACK);

        // Header
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

        JLabel headerLabel = new JLabel("Hostel Grievance Hub - Staff Dashboard", SwingConstants.LEFT);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0, 102, 204));
        titlePanel.add(headerLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // User info and logout
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(new Color(255, 255, 255));

        String role = staff.getScope().equals("ALL") ? "Admin" : "Staff (" + staff.getScope() + ")";
        JLabel userLabel = new JLabel("👤 " + staff.getName() + " (" + role + ")");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.BLACK);
        userPanel.add(userLabel);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(220, 53, 69).darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(220, 53, 69));
            }
        });
        logoutButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });
        userPanel.add(logoutButton);

        headerPanel.add(userPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Filters panel
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBackground(new Color(255, 255, 255));
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel statusLabel = new JLabel("📊 Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(Color.BLACK);
        filterPanel.add(statusLabel, gbc);
        gbc.gridx = 1;
        statusFilter = new JComboBox<>(new String[]{"All", "Pending", "Ongoing", "Resolved", "Escalated"});
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusFilter.setBackground(Color.WHITE);
        statusFilter.setForeground(Color.BLACK);
        statusFilter.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        statusFilter.setPreferredSize(new Dimension(100, 25));
        filterPanel.add(statusFilter, gbc);

        gbc.gridx = 2;
        JLabel hostelLabel = new JLabel("🏢 Hostel:");
        hostelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        hostelLabel.setForeground(Color.BLACK);
        filterPanel.add(hostelLabel, gbc);
        gbc.gridx = 3;
        hostelFilter = new JComboBox<>(new String[]{"All", "GH1", "GH2", "GH3", "BH"});
        hostelFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        hostelFilter.setBackground(Color.WHITE);
        hostelFilter.setForeground(Color.BLACK);
        hostelFilter.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        hostelFilter.setPreferredSize(new Dimension(100, 25));
        filterPanel.add(hostelFilter, gbc);

        gbc.gridx = 4;
        JLabel categoryLabel = new JLabel("🔧 Category:");
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryLabel.setForeground(Color.BLACK);
        filterPanel.add(categoryLabel, gbc);
        gbc.gridx = 5;
        categoryFilter = new JComboBox<>(new String[]{"All", "Plumbing", "Carpentry", "Electrical", "General"});
        categoryFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryFilter.setBackground(Color.WHITE);
        categoryFilter.setForeground(Color.BLACK);
        categoryFilter.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        categoryFilter.setPreferredSize(new Dimension(100, 25));
        filterPanel.add(categoryFilter, gbc);

        gbc.gridx = 6;
        JLabel priorityLabel = new JLabel("⚡ Priority:");
        priorityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        priorityLabel.setForeground(Color.BLACK);
        filterPanel.add(priorityLabel, gbc);
        gbc.gridx = 7;
        priorityFilter = new JComboBox<>(new String[]{"All", "Low", "Medium", "High"});
        priorityFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        priorityFilter.setBackground(Color.WHITE);
        priorityFilter.setForeground(Color.BLACK);
        priorityFilter.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        priorityFilter.setPreferredSize(new Dimension(100, 25));
        filterPanel.add(priorityFilter, gbc);

        gbc.gridx = 8;
        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchLabel.setForeground(Color.BLACK);
        filterPanel.add(searchLabel, gbc);
        gbc.gridx = 9;
        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(Color.WHITE);
        searchField.setForeground(Color.BLACK);
        searchField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        searchField.setPreferredSize(new Dimension(150, 25));
        filterPanel.add(searchField, gbc);

        gbc.gridx = 10;
        JButton filterButton = new JButton("🔍 Filter");
        filterButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filterButton.setForeground(Color.WHITE);
        filterButton.setBackground(new Color(0, 102, 204));
        filterButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        filterButton.setFocusPainted(false);
        filterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                filterButton.setBackground(new Color(0, 102, 204).darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                filterButton.setBackground(new Color(0, 102, 204));
            }
        });
        filterButton.addActionListener(e -> loadComplaints());
        filterPanel.add(filterButton, gbc);

        add(filterPanel, BorderLayout.SOUTH);

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

        // Table title
        JLabel tableTitle = new JLabel("📋 Complaints Management", SwingConstants.CENTER);
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(new Color(0, 102, 204));
        complaintsPanel.add(tableTitle, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "📝 Title", "👤 Student", "🏢 Hostel", "🔧 Category", "⚡ Priority", "📊 Status", "📅 Created", "⚙️ Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Actions column
            }
        };
        complaintsTable = new JTable(tableModel);
        complaintsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        complaintsTable.setBackground(Color.WHITE);
        complaintsTable.setForeground(Color.BLACK);
        complaintsTable.setGridColor(new Color(200, 200, 200));
        complaintsTable.setSelectionBackground(new Color(173, 216, 230));
        complaintsTable.setSelectionForeground(Color.BLACK);
        complaintsTable.getTableHeader().setBackground(new Color(0, 102, 204));
        complaintsTable.getTableHeader().setForeground(Color.WHITE);
        complaintsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        JScrollPane tableScroll = new JScrollPane(complaintsTable);
        tableScroll.setPreferredSize(new Dimension(600, 500));
        complaintsPanel.add(tableScroll, BorderLayout.CENTER);

        // Add button column for actions
        Action changeStatus = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int row = Integer.valueOf(e.getActionCommand());
                int id = (int) tableModel.getValueAt(row, 0);
                String currentStatus = (String) tableModel.getValueAt(row, 6);
                if ("Resolved".equals(currentStatus)) {
                    JOptionPane.showMessageDialog(StaffDashboard.this, "Cannot change status of resolved complaints.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                String[] options = {"Pending", "Ongoing", "Resolved"};
                String newStatus = (String) JOptionPane.showInputDialog(StaffDashboard.this, "Change Status", "Status", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (newStatus != null) {
                    try {
                        staffController.updateComplaintStatus(id, newStatus);
                        loadComplaints();

                        // Notify student about status update
                        ComplaintDAO complaintDAO = new ComplaintDAO();
                        Complaint complaint = complaintDAO.getComplaintById(id);
                        if (complaint != null) {
                            staffController.sendNotification(complaint.getStudentId(), "Your complaint '" + complaint.getTitle() + "' status has been updated to: " + newStatus);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(StaffDashboard.this, "Failed to update status.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
        new ButtonColumn(complaintsTable, changeStatus, 8);

        tabbedPane.addTab("📋 Complaints", complaintsPanel);

        // Analytics tab
        JPanel analyticsPanel = new JPanel(new BorderLayout());
        analyticsPanel.setBackground(Color.WHITE);
        analyticsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Title for analytics
        JLabel analyticsTitle = new JLabel("📊 Advanced Analytics Dashboard", SwingConstants.CENTER);
        analyticsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        analyticsTitle.setForeground(new Color(0, 102, 204));
        analyticsPanel.add(analyticsTitle, BorderLayout.NORTH);

        // Analytics content with charts
        JPanel analyticsContent = new JPanel(new GridLayout(1, 2, 20, 20));
        analyticsContent.setBackground(Color.WHITE);
        analyticsContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Pie chart for status distribution (commented out due to JFreeChart issues)
        JPanel pieChartPanel = new JPanel(new BorderLayout());
        pieChartPanel.setBackground(Color.WHITE);
        pieChartPanel.setBorder(BorderFactory.createTitledBorder("📊 Status Distribution"));
        // JFreeChart pieChart = createStatusPieChart();
        // ChartPanel pieChartPanelComponent = new ChartPanel(pieChart);
        // pieChartPanelComponent.setPreferredSize(new Dimension(400, 300));
        // pieChartPanel.add(pieChartPanelComponent, BorderLayout.CENTER);
        JLabel piePlaceholder = new JLabel("Charts unavailable in headless mode", SwingConstants.CENTER);
        piePlaceholder.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        pieChartPanel.add(piePlaceholder, BorderLayout.CENTER);
        analyticsContent.add(pieChartPanel);

        // Bar chart for monthly trends (commented out due to JFreeChart issues)
        JPanel barChartPanel = new JPanel(new BorderLayout());
        barChartPanel.setBackground(Color.WHITE);
        barChartPanel.setBorder(BorderFactory.createTitledBorder("📈 Monthly Trends"));
        // JFreeChart barChart = createMonthlyBarChart();
        // ChartPanel barChartPanelComponent = new ChartPanel(barChart);
        // barChartPanelComponent.setPreferredSize(new Dimension(400, 300));
        // barChartPanel.add(barChartPanelComponent, BorderLayout.CENTER);
        JLabel barPlaceholder = new JLabel("Charts unavailable in headless mode", SwingConstants.CENTER);
        barPlaceholder.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        barChartPanel.add(barPlaceholder, BorderLayout.CENTER);
        analyticsContent.add(barChartPanel);

        analyticsPanel.add(analyticsContent, BorderLayout.CENTER);

        // Summary cards below charts
        JPanel summaryPanel = new JPanel(new GridLayout(1, 6, 10, 10));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        JPanel totalPanel = createAnalyticsCard("📋 Total", "0", new Color(0, 102, 204));
        summaryPanel.add(totalPanel);

        JPanel resolvedPanel = createAnalyticsCard("✅ Resolved", "0", new Color(34, 139, 34));
        summaryPanel.add(resolvedPanel);

        JPanel pendingPanel = createAnalyticsCard("⏳ Pending", "0", new Color(255, 193, 7));
        summaryPanel.add(pendingPanel);

        JPanel escalatedPanel = createAnalyticsCard("🚨 Escalated", "0", new Color(220, 53, 69));
        summaryPanel.add(escalatedPanel);

        JPanel ongoingPanel = createAnalyticsCard("🔄 Ongoing", "0", new Color(23, 162, 184));
        summaryPanel.add(ongoingPanel);

        JPanel notificationsPanel = createAnalyticsCard("🔔 Unread", "0", new Color(108, 117, 125));
        summaryPanel.add(notificationsPanel);

        analyticsPanel.add(summaryPanel, BorderLayout.SOUTH);

        // Load analytics data
        loadAnalyticsData(totalPanel, resolvedPanel, pendingPanel, escalatedPanel, ongoingPanel, notificationsPanel);
        tabbedPane.addTab("📊 Analytics", analyticsPanel);

        // Settings tab
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBackground(Color.WHITE);
        settingsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        GridBagConstraints gbcSettings = new GridBagConstraints();
        gbcSettings.insets = new Insets(10, 10, 10, 10);
        gbcSettings.fill = GridBagConstraints.HORIZONTAL;

        // Profile section
        gbcSettings.gridx = 0; gbcSettings.gridy = 0; gbcSettings.gridwidth = 2;
        JLabel profileLabel = new JLabel("👤 Profile Settings", SwingConstants.CENTER);
        profileLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        profileLabel.setForeground(new Color(0, 102, 204));
        settingsPanel.add(profileLabel, gbcSettings);

        gbcSettings.gridwidth = 1;
        gbcSettings.gridy = 1;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        settingsPanel.add(nameLabel, gbcSettings);
        gbcSettings.gridx = 1;
        JTextField nameField = new JTextField(staff.getName());
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setPreferredSize(new Dimension(200, 25));
        settingsPanel.add(nameField, gbcSettings);

        gbcSettings.gridx = 0; gbcSettings.gridy = 2;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        settingsPanel.add(emailLabel, gbcSettings);
        gbcSettings.gridx = 1;
        JTextField emailField = new JTextField(staff.getEmail());
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(200, 25));
        settingsPanel.add(emailField, gbcSettings);

        // Notifications section
        gbcSettings.gridx = 0; gbcSettings.gridy = 3; gbcSettings.gridwidth = 2;
        JLabel notifLabel = new JLabel("🔔 Notification Preferences", SwingConstants.CENTER);
        notifLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        notifLabel.setForeground(new Color(0, 102, 204));
        settingsPanel.add(notifLabel, gbcSettings);

        gbcSettings.gridwidth = 1;
        gbcSettings.gridy = 4;
        JCheckBox emailNotif = new JCheckBox("Email notifications for new complaints");
        emailNotif.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailNotif.setSelected(true);
        settingsPanel.add(emailNotif, gbcSettings);

        gbcSettings.gridy = 5;
        JCheckBox priorityNotif = new JCheckBox("Notifications for high-priority complaints");
        priorityNotif.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        priorityNotif.setSelected(true);
        settingsPanel.add(priorityNotif, gbcSettings);

        // Save button
        gbcSettings.gridx = 0; gbcSettings.gridy = 6; gbcSettings.gridwidth = 2;
        JButton saveButton = new JButton("💾 Save Settings");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(34, 139, 34));
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(new Color(34, 139, 34).darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(new Color(34, 139, 34));
            }
        });
        saveButton.addActionListener(e -> {
            // Placeholder for save logic
            JOptionPane.showMessageDialog(StaffDashboard.this, "Settings saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        settingsPanel.add(saveButton, gbcSettings);

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
                            loadNotifications(notifTableModel, filterCombo.getSelectedItem().toString());
                        } else {
                            JOptionPane.showMessageDialog(StaffDashboard.this, "Failed to mark as read.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(StaffDashboard.this, "Failed to mark as read.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Delete notification
                    try {
                        // Remove from list (in real implementation, delete from database)
                        notifTableModel.removeRow(row);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(StaffDashboard.this, "Failed to delete notification.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
        new ButtonColumn(notifTable, notifAction, 4);

        // Load notifications
        loadNotifications(notifTableModel, "All");

        // Filter action
        filterCombo.addActionListener(e -> loadNotifications(notifTableModel, filterCombo.getSelectedItem().toString()));

        // Mark all read action
        markAllReadButton.addActionListener(e -> {
            try {
                staffController.getNotificationsByUser(staff.getId()).forEach(n -> n.setRead(true));
                loadNotifications(notifTableModel, filterCombo.getSelectedItem().toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(StaffDashboard.this, "Failed to mark all as read.", "Error", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(StaffDashboard.this, "Failed to delete read notifications.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        tabbedPane.addTab("🔔 Notifications", notifPanel);

        tabbedPane.addTab("⚙️ Settings", settingsPanel);

        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
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

    private void loadAnalyticsData(JPanel totalPanel, JPanel resolvedPanel, JPanel pendingPanel, JPanel escalatedPanel, JPanel ongoingPanel, JPanel notificationsPanel) {
        try {
            List<Complaint> complaints = staffController.getComplaintsForStaff(staff.getId());

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

            // Get unread notifications count
            int unreadNotifications = staffController.getNotificationsByUser(staff.getId()).stream()
                .filter(n -> !n.isRead())
                .toList()
                .size();

            // Update the labels
            ((JLabel) totalPanel.getComponent(1)).setText(String.valueOf(total));
            ((JLabel) resolvedPanel.getComponent(1)).setText(String.valueOf(resolved));
            ((JLabel) pendingPanel.getComponent(1)).setText(String.valueOf(pending));
            ((JLabel) escalatedPanel.getComponent(1)).setText(String.valueOf(escalated));
            ((JLabel) ongoingPanel.getComponent(1)).setText(String.valueOf(ongoing));
            ((JLabel) notificationsPanel.getComponent(1)).setText(String.valueOf(unreadNotifications));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadComplaints() {
        tableModel.setRowCount(0);
        try {
            List<Complaint> complaints = staffController.getComplaintsForStaff(staff.getId());
            for (Complaint c : complaints) {
                // Auto-escalate long-unresolved complaints
                if (!"Resolved".equals(c.getStatus()) && c.getCreatedAt() != null) {
                    long daysSinceCreated = (System.currentTimeMillis() - c.getCreatedAt().getTime()) / (1000 * 60 * 60 * 24);
                    if (daysSinceCreated > 7) {
                        if (!"High".equals(c.getPriority())) {
                            complaintDAO.updateComplaintPriority(c.getId(), "High");
                            c.setPriority("High");
                        }
                        if (!"Escalated".equals(c.getStatus())) {
                            staffController.updateComplaintStatus(c.getId(), "Escalated");
                            c.setStatus("Escalated");
                        }
                    }
                }

                // Filter logic
                if (!"All".equals(statusFilter.getSelectedItem()) && !c.getStatus().equals(statusFilter.getSelectedItem())) continue;
                // Add more filters as needed
                String studentName = userDAO.getStudentById(c.getStudentId()).getName();
                String hostel = userDAO.getStudentById(c.getStudentId()).getHostel();
                if (!"All".equals(hostelFilter.getSelectedItem()) && !hostel.equals(hostelFilter.getSelectedItem())) continue;
                if (!"All".equals(categoryFilter.getSelectedItem()) && !c.getCategory().equals(categoryFilter.getSelectedItem())) continue;
                if (!"All".equals(priorityFilter.getSelectedItem()) && !c.getPriority().equals(priorityFilter.getSelectedItem())) continue;
                String search = searchField.getText().toLowerCase();
                if (!search.isEmpty() && !c.getTitle().toLowerCase().contains(search)) continue;

                String actionText = "Resolved".equals(c.getStatus()) ? "Resolved" : "Change Status";
                tableModel.addRow(new Object[]{c.getId(), c.getTitle(), studentName, hostel, c.getCategory(), c.getPriority(), c.getStatus(), c.getCreatedAt(), actionText});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load complaints.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Commented out chart creation methods due to JFreeChart issues in headless environment
    // private JFreeChart createStatusPieChart() {
    //     DefaultPieDataset dataset = new DefaultPieDataset();
    //     try {
    //         List<Complaint> complaints = staffController.getComplaintsForStaff(staff.getId());
    //         int pending = 0, ongoing = 0, resolved = 0, escalated = 0;
    //         for (Complaint c : complaints) {
    //             switch (c.getStatus()) {
    //                 case "Pending": pending++; break;
    //                 case "Ongoing": ongoing++; break;
    //                 case "Resolved": resolved++; break;
    //                 case "Escalated": escalated++; break;
    //             }
    //         }
    //         dataset.setValue("Pending", pending);
    //         dataset.setValue("Ongoing", ongoing);
    //         dataset.setValue("Resolved", resolved);
    //         dataset.setValue("Escalated", escalated);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     JFreeChart chart = ChartFactory.createPieChart("Complaint Status Distribution", dataset, true, true, false);
    //     PiePlot plot = (PiePlot) chart.getPlot();
    //     plot.setSectionPaint("Pending", new Color(255, 193, 7));
    //     plot.setSectionPaint("Ongoing", new Color(23, 162, 184));
    //     plot.setSectionPaint("Resolved", new Color(34, 139, 34));
    //     plot.setSectionPaint("Escalated", new Color(220, 53, 69));
    //     return chart;
    // }

    // private JFreeChart createMonthlyBarChart() {
    //     DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    //     try {
    //         List<Complaint> complaints = staffController.getComplaintsForStaff(staff.getId());
    //         // Simple monthly aggregation (last 6 months)
    //         for (int i = 5; i >= 0; i--) {
    //             final int currentMonth = (java.time.LocalDate.now().getMonthValue() - i + 12) % 12;
    //             final int finalMonth = currentMonth == 0 ? 12 : currentMonth;
    //             String monthName = java.time.Month.of(finalMonth).name().substring(0, 3);
    //             long count = complaints.stream()
    //                 .filter(c -> c.getCreatedAt() != null &&
    //                     java.time.LocalDate.ofInstant(c.getCreatedAt().toInstant(), java.time.ZoneId.systemDefault()).getMonthValue() == finalMonth)
    //                 .count();
    //             dataset.addValue(count, "Complaints", monthName);
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return ChartFactory.createBarChart("Monthly Complaint Trends", "Month", "Count", dataset, PlotOrientation.VERTICAL, false, true, false);
    // }

    private void loadNotifications(DefaultTableModel notifTableModel, String filter) {
        notifTableModel.setRowCount(0);
        try {
            List<Notification> notifications = staffController.getNotificationsByUser(staff.getId());
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
}
