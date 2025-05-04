package classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class GuardDashboard extends JFrame {

    private JTable studentTable;
    private DefaultTableModel model;

    public GuardDashboard() {
        setTitle("Guard Dashboard - Student Room Allocation");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

 
        String[] columnNames = {"Student ID", "First Name", "Last Name", "Room Number"};
        model = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

    
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton viewAllocationsButton = new JButton("View Room Allocations");
        viewAllocationsButton.addActionListener(e -> {
            model.setRowCount(0); 
            setStudentTableHeaders();
            loadStudentData();
        });

        
        JButton viewApprovedVisitorsButton = new JButton("View Approved Visitors");
        viewApprovedVisitorsButton.addActionListener(e -> {
            model.setRowCount(0); 
            setVisitorTableHeaders();
            loadApprovedVisitors();
        });

        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose();  
            new MainMenu();
        });

        buttonPanel.add(viewAllocationsButton);
        buttonPanel.add(viewApprovedVisitorsButton);
        buttonPanel.add(logoutButton);  
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void setStudentTableHeaders() {
        model.setColumnIdentifiers(new String[]{"Student ID", "First Name", "Last Name", "Room Number"});
    }

    private void setVisitorTableHeaders() {
        model.setColumnIdentifiers(new String[]{"Visitor Name", "Contact", "Reason", "Student ID", "Request Date"});
    }

    private void loadStudentData() {
        String url = "jdbc:mysql://localhost:3306/hostel_management";
        String user = "root";
        String pass = "1234";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "SELECT student_id, first_name, last_name, room_number FROM students";
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("student_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    int roomNumber = rs.getInt("room_number");
                    String roomDisplay = rs.wasNull() ? "Unallocated" : String.valueOf(roomNumber);
                    model.addRow(new Object[]{id, firstName, lastName, roomDisplay});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadApprovedVisitors() {
        String url = "jdbc:mysql://localhost:3306/hostel_management";
        String user = "root";
        String pass = "1234";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "SELECT visitor_name, visitor_contact, reason_for_visit, student_id, request_date " +
                           "FROM visitor_requests WHERE status = 'approved'";
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    String name = rs.getString("visitor_name");
                    String contact = rs.getString("visitor_contact");
                    String reason = rs.getString("reason_for_visit");
                    int studentId = rs.getInt("student_id");
                    Timestamp date = rs.getTimestamp("request_date");

                    model.addRow(new Object[]{name, contact, reason, studentId, date.toString()});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
