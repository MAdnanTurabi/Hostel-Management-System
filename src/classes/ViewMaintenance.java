package classes;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewMaintenance extends JFrame {
    private JTextArea maintenanceRequestsArea;
    private int studentId;

    public ViewMaintenance(int studentId) {
        this.studentId = studentId;

        setTitle("View Maintenance Requests");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        maintenanceRequestsArea = new JTextArea();
        maintenanceRequestsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(maintenanceRequestsArea);

        add(new JLabel("Your Maintenance Requests:"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadMaintenanceRequests();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadMaintenanceRequests() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HostelManagement", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement("SELECT issue_description, status, request_date FROM maintenance_requests WHERE student_id = ?")) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Date: ").append(rs.getTimestamp("request_date"))
                        .append("\nIssue: ").append(rs.getString("issue_description"))
                        .append("\nStatus: ").append(rs.getString("status"))
                        .append("\n--------------------------\n");
            }

            maintenanceRequestsArea.setText(sb.length() > 0 ? sb.toString() : "No maintenance requests found.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching maintenance requests.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
