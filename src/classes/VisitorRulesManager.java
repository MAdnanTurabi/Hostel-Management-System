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
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Time Display Setup
        timeLabel = new JLabel("Visiting Hours: ");
        JPanel timePanel = new JPanel();
        timePanel.add(timeLabel);

        // Table Setup
        model = new DefaultTableModel(new String[]{"ID", "Rule Description"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Form Inputs
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        ruleField = new JTextField();
        timeField = new JTextField();
        JButton addButton = new JButton("Add Rule");
        JButton deleteButton = new JButton("Delete Selected");
        JButton setTimingsButton = new JButton("Set Visiting Hours");

        inputPanel.add(new JLabel("Rule Description:"));
        inputPanel.add(ruleField);
        inputPanel.add(new JLabel("Visiting Hours:"));
        inputPanel.add(timeField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        add(timePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        add(setTimingsButton, BorderLayout.EAST);  // Add the button to the EAST position

        // Load initial data
        loadRules();
        loadVisitingHours();

        // Add Rule Action
        addButton.addActionListener(e -> addRule());

        // Delete Rule Action
        deleteButton.addActionListener(e -> deleteSelectedRule());

        // Set Visiting Hours Action
        setTimingsButton.addActionListener(e -> updateVisitingHours());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadRules() {
        model.setRowCount(0); // Clear existing data
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT rule_id, rule_description FROM visitor_rules")) {

            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("rule_id"),
                        rs.getString("rule_description")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading rules: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadVisitingHours() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT visiting_hours FROM visitor_timings LIMIT 1")) {

            if (rs.next()) {
                timeLabel.setText("Visiting Hours: " + rs.getString("visiting_hours"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading visiting hours: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addRule() {
        String rule = ruleField.getText().trim();

        if (rule.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in the rule description.");
            return;
        }

        String query = "INSERT INTO visitor_rules (rule_description) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, rule);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Rule added successfully!");
            ruleField.setText("");
            loadRules();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding rule: " + e.getMessage());
            e.printStackTrace();
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
            JOptionPane.showMessageDialog(this, "Rule deleted successfully!");
            loadRules();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting rule: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateVisitingHours() {
        String newHours = timeField.getText().trim();

        if (newHours.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in the visiting hours.");
            return;
        }

        String query = "SELECT visiting_hours FROM visitor_timings LIMIT 1";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                // If a record exists, update it
                String updateQuery = "UPDATE visitor_timings SET visiting_hours = ? WHERE id = 1";
                try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                    pstmt.setString(1, newHours);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Visiting hours updated successfully!");
                    timeField.setText("");
                    loadVisitingHours(); // Refresh the label with the updated time
                }
            } else {
                // If no record exists, insert a new one
                String insertQuery = "INSERT INTO visitor_timings (visiting_hours) VALUES (?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    pstmt.setString(1, newHours);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Visiting hours set successfully!");
                    timeField.setText("");
                    loadVisitingHours(); // Refresh the label with the newly set time
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating visiting hours: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
