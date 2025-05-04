package classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VisitorRequestApprovalPage extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private Connection conn;

    public VisitorRequestApprovalPage() {
        setTitle("Visitor Request Approvals");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

       
        connectToDB();

        
        model = new DefaultTableModel(new String[]{"ID", "Student ID", "Visitor Name", "Contact", "Reason", "Date", "Status"}, 0);
        table = new JTable(model);
        loadRequests();

        add(new JScrollPane(table), BorderLayout.CENTER);

        
        JPanel actionPanel = new JPanel();

        JButton approveButton = new JButton("Approve");
        approveButton.addActionListener(e -> updateStatus("Approved"));

        JButton rejectButton = new JButton("Reject");
        rejectButton.addActionListener(e -> updateStatus("Rejected"));

        actionPanel.add(approveButton);
        actionPanel.add(rejectButton);

        add(actionPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void connectToDB() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed: " + e.getMessage());
        }
    }

    private void loadRequests() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM visitor_requests WHERE status='Pending'");
            model.setRowCount(0); 
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("request_id"),
                        rs.getInt("student_id"),
                        rs.getString("visitor_name"),
                        rs.getString("visitor_contact"),
                        rs.getString("reason_for_visit"),
                        rs.getTimestamp("request_date"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to Load Requests: " + e.getMessage());
        }
    }

    private void updateStatus(String status) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request first.");
            return;
        }

        int requestId = (int) model.getValueAt(selectedRow, 0);

        try {
            PreparedStatement pstmt = conn.prepareStatement("UPDATE visitor_requests SET status=? WHERE request_id=?");
            pstmt.setString(1, status);
            pstmt.setInt(2, requestId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Request " + status.toLowerCase() + " successfully.");
            loadRequests(); 
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to update status: " + e.getMessage());
        }
    }
}
