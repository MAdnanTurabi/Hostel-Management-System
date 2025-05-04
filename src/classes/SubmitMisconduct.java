package classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class SubmitMisconduct extends JFrame {
    private JTextArea descriptionArea;
    private JButton submitButton;
    private int studentId;

    public SubmitMisconduct(int studentId) {
        this.studentId = studentId;

        setTitle("Report Misconduct");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

       
        descriptionArea = new JTextArea(5, 30);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);

        
        submitButton = new JButton("Submit Report");
        submitButton.addActionListener(this::submitReport);

        
        add(new JLabel("Describe the misconduct:"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void submitReport(ActionEvent e) {
        String description = descriptionArea.getText().trim();

        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid description.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

       
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO misconduct_reports (student_id, description) VALUES (?, ?)")) {

            stmt.setInt(1, studentId);
            stmt.setString(2, description);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Misconduct report submitted successfully.");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting report.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
