package classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class ViewVisitorRequestStatusPage extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hostel_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";
    
    private JTable requestStatusTable;
    private static int studentID;

    public ViewVisitorRequestStatusPage(int studentID) {
        this.studentID = studentID;
        
        setTitle("View Visitor Request Status");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

    
        requestStatusTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(requestStatusTable);
        add(scrollPane, BorderLayout.CENTER);

  
        loadRequestStatus();

       
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadRequestStatus() {
    
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Request ID", "Visitor Name", "Reason for Visit", "Request Date", "Status"}, 0);
        
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM visitor_requests WHERE student_id = ? ORDER BY request_date DESC";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, studentID);
            ResultSet resultSet = pst.executeQuery();

            while (resultSet.next()) {
                int requestId = resultSet.getInt("request_id");
                String visitorName = resultSet.getString("visitor_name");
                String reasonForVisit = resultSet.getString("reason_for_visit");
                String requestDate = resultSet.getString("request_date");
                String status = resultSet.getString("status");

                model.addRow(new Object[]{requestId, visitorName, reasonForVisit, requestDate, status});
            }

            requestStatusTable.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading visitor request status: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> new ViewVisitorRequestStatusPage(studentID));
    }
}
