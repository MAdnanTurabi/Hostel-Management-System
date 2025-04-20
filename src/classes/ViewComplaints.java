package classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class ViewComplaints extends JFrame {
    private JList<String> complaintsList;
    private DefaultListModel<String> listModel;
    private int studentID;

    public ViewComplaints(int studentID) {
        this.studentID = studentID;
        setTitle("View Complaints");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Your Complaints:");
        label.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(label, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        complaintsList = new JList<>(listModel);
        complaintsList.setFont(new Font("Tahoma", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(complaintsList);
        add(scrollPane, BorderLayout.CENTER);

        loadComplaints();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadComplaints() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234")) {
            String query = "SELECT complaint_text, complaint_date, status FROM complaints WHERE student_id = ?";
            try (PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setInt(1, studentID);

                try (ResultSet rs = pst.executeQuery()) {
                    listModel.clear();  // Clear the list model before adding data

                    while (rs.next()) {
                        String complaintText = rs.getString("complaint_text");
                        String complaintDate = rs.getString("complaint_date");
                        String status = rs.getString("status");

                        listModel.addElement(complaintText + " | Date: " + complaintDate + " | Status: " + status);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewComplaints(1)); // Pass a valid studentID
    }
}
