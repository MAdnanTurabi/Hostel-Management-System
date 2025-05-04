package classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SubmitComplaint extends JFrame {
    private JTextArea complaintTextArea;
    private JButton submitButton;
    private int studentID;

    public SubmitComplaint(int studentID) {
        this.studentID = studentID;
        setTitle("Submit Complaint");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Enter Your Complaint:");
        label.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(label, BorderLayout.NORTH);

        complaintTextArea = new JTextArea(10, 30);
        complaintTextArea.setFont(new Font("Tahoma", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(complaintTextArea);
        add(scrollPane, BorderLayout.CENTER);

        submitButton = new JButton("Submit Complaint");
        submitButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        submitButton.addActionListener(e -> submitComplaint());

        add(submitButton, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void submitComplaint() {
        String complaintText = complaintTextArea.getText().trim();

        if (complaintText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a complaint.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234")) {
            String query = "INSERT INTO complaints (student_id, complaint_text) VALUES (?, ?)";
            try (PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setInt(1, studentID);
                pst.setString(2, complaintText);

                int rowsInserted = pst.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Complaint submitted successfully!");
                    dispose(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Could not submit the complaint.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SubmitComplaint(1));
    }
}
