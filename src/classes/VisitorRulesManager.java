package classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VisitorRulesManager extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField ruleField, timeField;
    private JLabel timeLabel;

    private final String DB_URL = "jdbc:mysql://localhost:3306/hostel_management";
    private final String DB_USER = "root";
    private final String DB_PASS = "1234";

    public VisitorRulesManager() {
        setTitle("Visitor Rules & Timings");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top panel for current visiting hours
        timeLabel = new JLabel("Visiting Hours: ");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.add(timeLabel);

        // Table for rules
        model = new DefaultTableModel(new String[]{"ID", "Rule Description"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Input form
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        ruleField = new JTextField();
        timeField = new JTextField();
        JButton addButton = new JButton("Add Rule");
        JButton deleteButton = new JButton("Delete Selected");
        JButton setTimingsButton = new JButton("Set Visiting Hours");

        inputPanel.add(new JLabel("Rule Description:"));
        inputPanel.add(ruleField);
        inputPanel.add(new JLabel("Visiting Hours (e.g. 9AM - 5PM):"));
        inputPanel.add(timeField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        // Bottom panel to combine inputs and set time button
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(setTimingsButton, BorderLayout.EAST);

        add(timePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load existing data
        loadRules();
        loadVisitingHours();

        // Button actions
        addButton.addActionListener(e -> addRule());
        deleteButton.addActionListener(e -> deleteSelectedRule());
        setTimingsButton.addActionListener(e -> updateVisitingHours());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadRules() {
        model.setRowCount(0); // Clear existing rows
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT rule_id, rule_description FROM visitor_rules")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("rule_id"),
                        rs.getString("rule_description")
                });
            }

        } catch (SQLException e) {
            showError("Error loading rules", e);
        }
    }

    private void loadVisitingHours() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT visiting_hours FROM visitor_timings LIMIT 1")) {

            if (rs.next()) {
                timeLabel.setText("Visiting Hours: " + rs.getString("visiting_hours"));
            } else {
                timeLabel.setText("Visiting Hours: Not Set");
            }

        } catch (SQLException e) {
            showError("Error loading visiting hours", e);
        }
    }

    private void addRule() {
        String rule = ruleField.getText().trim();
        if (rule.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a rule description.");
            return;
        }

        String query = "INSERT INTO visitor_rules (rule_description) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, rule);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Rule added successfully.");
            ruleField.setText("");
            loadRules();

        } catch (SQLException e) {
            showError("Error adding rule", e);
        }
    }

    private void deleteSelectedRule() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a rule to delete.");
            return;
        }

        int ruleId = (int) model.getValueAt(selectedRow, 0);
        String query = "DELETE FROM visitor_rules WHERE rule_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, ruleId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Rule deleted successfully.");
            loadRules();

        } catch (SQLException e) {
            showError("Error deleting rule", e);
        }
    }

    private void updateVisitingHours() {
        String newHours = timeField.getText().trim();
        if (newHours.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter visiting hours.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM visitor_timings LIMIT 1")) {

            if (rs.next()) {
                // Update existing record
                String updateQuery = "UPDATE visitor_timings SET visiting_hours = ? WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                    pstmt.setString(1, newHours);
                    pstmt.setInt(2, rs.getInt("id"));
                    pstmt.executeUpdate();
                }
            } else {
                // Insert new record
                String insertQuery = "INSERT INTO visitor_timings (visiting_hours) VALUES (?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    pstmt.setString(1, newHours);
                    pstmt.executeUpdate();
                }
            }

            JOptionPane.showMessageDialog(this, "Visiting hours saved successfully.");
            timeField.setText("");
            loadVisitingHours();

        } catch (SQLException e) {
            showError("Error updating visiting hours", e);
        }
    }

    private void showError(String message, SQLException e) {
        JOptionPane.showMessageDialog(this, message + ": " + e.getMessage());
        e.printStackTrace();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VisitorRulesManager::new);
    }
}
