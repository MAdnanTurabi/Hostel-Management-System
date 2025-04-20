package classes;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewMisconduct extends JFrame {
    private JTextArea reportsArea;
    private int studentId;

    public ViewMisconduct(int studentId) {
        this.studentId = studentId;

        setTitle("View Misconduct Reports");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

       
        reportsArea = new JTextArea();
        reportsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportsArea);

       
        fetchReports();

        
        add(new JLabel("Your Misconduct Reports:"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void fetchReports() {
        StringBuilder reportsText = new StringBuilder();

      
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HostelManagement", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement("SELECT report_id, description, report_date FROM misconduct_reports WHERE student_id = ?")) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int reportId = rs.getInt("report_id");
                String description = rs.getString("description");
                String reportDate = rs.getString("report_date");

                reportsText.append("Report ID: ").append(reportId).append("\n")
                        .append("Date: ").append(reportDate).append("\n")
                        .append("Description: ").append(description).append("\n\n");
            }

            if (reportsText.length() == 0) {
                reportsText.append("No misconduct reports found.");
            }

            reportsArea.setText(reportsText.toString());

        } catch (SQLException ex) {
            ex.printStackTrace();
            reportsArea.setText("Error fetching reports.");
        }
    }
}
