package classes;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class showApprovedVisitors {

    public void showApprovedVisitors(JFrame parentFrame) {
        StringBuilder output = new StringBuilder();

        try (Connection conn = getConnection()) {
            String query = "SELECT vr.request_id, vr.visitor_name, vr.visitor_contact, vr.reason_for_visit, vr.request_date, " +
                           "s.first_name, s.last_name " +
                           "FROM visitor_requests vr " +
                           "JOIN students s ON vr.student_id = s.student_id " +
                           "WHERE vr.status = 'Approved'";

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                output.append("=== Approved Visitors ===\n\n");
                while (rs.next()) {
                    int requestId = rs.getInt("request_id");
                    String visitorName = rs.getString("visitor_name");
                    String visitorContact = rs.getString("visitor_contact");
                    String reason = rs.getString("reason_for_visit");
                    Timestamp requestDate = rs.getTimestamp("request_date");
                    String studentName = rs.getString("first_name") + " " + rs.getString("last_name");

                    output.append("Request ID: ").append(requestId)
                          .append(" | Visitor: ").append(visitorName)
                          .append(" | Contact: ").append(visitorContact)
                          .append(" | Reason: ").append(reason)
                          .append(" | Date: ").append(requestDate)
                          .append(" | Student: ").append(studentName)
                          .append("\n\n");
                }

                if (output.toString().equals("=== Approved Visitors ===\n\n")) {
                    output.append("No approved visitor requests found.");
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame, "Error fetching visitor data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        showTextDialog(parentFrame, "Approved Visitor Requests", output.toString());
    }

    private void showTextDialog(JFrame parentFrame, String title, String message) {
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        JOptionPane.showMessageDialog(parentFrame, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234");
    }
}
