package classes;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class SubmitMaintenance extends JFrame {
    private JTextArea issueDescriptionArea;
    private JButton submitButton;
    private int studentId;

    public SubmitMaintenance(int studentId) {
        this.studentId = studentId;

        setTitle("Submit Maintenance Request");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        issueDescriptionArea = new JTextArea(5, 30);
        JScrollPane scrollPane = new JScrollPane(issueDescriptionArea);

        
        issueDescriptionArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                limitTextLength();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                limitTextLength();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                limitTextLength();
            }

            private void limitTextLength() {
                if (issueDescriptionArea.getText().length() > 150) {
                    issueDescriptionArea.setText(issueDescriptionArea.getText().substring(0, 150));
                }
            }
        });

        submitButton = new JButton("Submit Request");
        submitButton.addActionListener(this::submitRequest);

        add(new JLabel("Describe the issue:"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void submitRequest(ActionEvent e) {
        String issueDescription = issueDescriptionArea.getText().trim();

        if (issueDescription.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid issue description.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO maintenance_requests (student_id, issue_description) VALUES (?, ?)")) {

            stmt.setInt(1, studentId);
            stmt.setString(2, issueDescription);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Maintenance request submitted successfully.");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting request.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
