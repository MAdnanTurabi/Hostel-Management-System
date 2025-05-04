package classes;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewMaintenance extends JFrame {
    private JTextArea maintenanceRequestsArea;
    private static int studentID;

    public ViewMaintenance(int studentId) {
        this.studentID = studentId;

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
        String url = "jdbc:mysql://localhost:3306/hostel_management";
        String user = "root";
        String password = "1234";

        String query = "SELECT issue_description, status FROM maintenance_requests WHERE student_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, studentID);
            ResultSet rs = stmt.executeQuery();

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                String issue = rs.getString("issue_description");
                String status = rs.getString("status");

                sb.append("Issue: ").append(issue)
                        .append("\nStatus: ").append(status)
                        .append("\n--------------------------\n");
            }

            maintenanceRequestsArea.setText(sb.length() > 0 ? sb.toString() : "No maintenance requests found.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   
    public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> new ViewMaintenance(studentID)); 
    }
}
