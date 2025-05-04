package classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class WardenDashboard extends JFrame {

    private JButton viewRoomsButton, viewComplaintsButton, verifyCheckoutButton, viewVisitorsButton, logoutButton;

    public WardenDashboard() {
        setTitle("Warden Dashboard");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));  

      
        viewRoomsButton = new JButton("View Occupied and Vacant Rooms");
        viewComplaintsButton = new JButton("Monitor All Complaints");
        verifyCheckoutButton = new JButton("Room Allocations");
        JButton approvedVisitorsButton = new JButton("View Approved Visitors");
        logoutButton = new JButton("Logout");

    
        viewRoomsButton.addActionListener(e -> showRoomStatus());
        viewComplaintsButton.addActionListener(e -> showComplaints());
        verifyCheckoutButton.addActionListener(e -> showCheckoutList());
        approvedVisitorsButton.addActionListener(e -> new showApprovedVisitors().showApprovedVisitors(this));

       
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginPage(); 
        });

        
        add(viewRoomsButton);
        add(viewComplaintsButton);
        add(verifyCheckoutButton);
        add(approvedVisitorsButton);
        add(logoutButton);

        setVisible(true);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234");
    }

    private void showRoomStatus() {
        StringBuilder output = new StringBuilder();
        try (Connection conn = getConnection()) {
            String query = "SELECT r.room_number, r.capacity, COUNT(s.student_id) AS occupants " +
                           "FROM rooms r LEFT JOIN students s ON r.room_number = s.room_number " +
                           "GROUP BY r.room_number, r.capacity";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int roomNumber = rs.getInt("room_number");
                    int capacity = rs.getInt("capacity");
                    int occupants = rs.getInt("occupants");
                    int available = capacity - occupants;

                    String status = (available == 0) ? "Occupied" : "Vacant (" + available + "/" + capacity + " seats available)";

                    output.append("Room ").append(roomNumber)
                          .append(" | Capacity: ").append(capacity)
                          .append(" | Occupied: ").append(occupants)
                          .append(" | Status: ").append(status)
                          .append("\n");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching room data.");
        }
        showTextDialog("Room Occupancy Status", output.toString());
    }

    private void showComplaints() {
        StringBuilder output = new StringBuilder();

        try (Connection conn = getConnection()) {

            // --- Fetch Misconduct Reports ---
            String misconductQuery = "SELECT mr.report_id, mr.description, mr.report_date, " +
                                     "s.first_name, s.last_name " +
                                     "FROM misconduct_reports mr " +
                                     "JOIN students s ON mr.student_id = s.student_id";

            try (PreparedStatement stmt = conn.prepareStatement(misconductQuery);
                 ResultSet rs = stmt.executeQuery()) {

                output.append("=== Misconduct Reports ===\n");
                while (rs.next()) {
                    int reportId = rs.getInt("report_id");
                    String description = rs.getString("description");
                    Timestamp date = rs.getTimestamp("report_date");
                    String studentName = rs.getString("first_name") + " " + rs.getString("last_name");

                    output.append("Report ID: ").append(reportId)
                          .append(" | Student: ").append(studentName)
                          .append(" | Description: ").append(description)
                          .append(" | Date: ").append(date)
                          .append("\n");
                }
            }

         
            String maintenanceQuery = "SELECT mr.request_id, mr.issue_description, mr.status, mr.request_date, " +
                                      "s.first_name, s.last_name " +
                                      "FROM maintenance_requests mr " +
                                      "JOIN students s ON mr.student_id = s.student_id";

            try (PreparedStatement stmt = conn.prepareStatement(maintenanceQuery);
                 ResultSet rs = stmt.executeQuery()) {

                output.append("\n=== Maintenance Requests ===\n");
                while (rs.next()) {
                    int requestId = rs.getInt("request_id");
                    String issue = rs.getString("issue_description");
                    String status = rs.getString("status");
                    Timestamp date = rs.getTimestamp("request_date");
                    String studentName = rs.getString("first_name") + " " + rs.getString("last_name");

                    output.append("Request ID: ").append(requestId)
                          .append(" | Student: ").append(studentName)
                          .append(" | Issue: ").append(issue)
                          .append(" | Status: ").append(status)
                          .append(" | Date: ").append(date)
                          .append("\n");
                }
            }

            if (output.length() == 0) {
                output.append("No complaints found.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching complaints.");
        }

        showTextDialog("All Complaints", output.toString());
    }

    private void showCheckoutList() {
        StringBuilder output = new StringBuilder();
        try (Connection conn = getConnection()) {
            String query = "SELECT student_id, first_name, last_name, room_number FROM students WHERE room_number IS NOT NULL";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    output.append("Student ID: ").append(rs.getInt("student_id"))
                          .append(" | Name: ").append(rs.getString("first_name")).append(" ").append(rs.getString("last_name"))
                          .append(" | Room #: ").append(rs.getInt("room_number"))
                          .append("\n");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching student room data.");
        }
        showTextDialog("Check-Out Verification", output.toString());
    }

    private void showTextDialog(String title, String message) {
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
