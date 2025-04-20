package classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class VisitorRequestPage extends JFrame {
    private static int studentID;

    private JTextField visitorNameField;
    private JTextField visitorContactField;
    private JTextArea visitorReasonArea;
    private JButton submitRequestButton;

    public VisitorRequestPage(int studentID) {
        this.studentID = studentID;
        setTitle("Visitor Request");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 1)); // Grid with 6 rows for components

        // Label and text field for visitor name
        JLabel visitorNameLabel = new JLabel("Visitor's Name:");
        visitorNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        visitorNameField = new JTextField();
        add(visitorNameLabel);
        add(visitorNameField);

        // Label and text field for visitor contact
        JLabel visitorContactLabel = new JLabel("Visitor's Contact Number:");
        visitorContactLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        visitorContactField = new JTextField();
        add(visitorContactLabel);
        add(visitorContactField);

        // Label and text area for visitor reason
        JLabel visitorReasonLabel = new JLabel("Reason for Visit:");
        visitorReasonLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        visitorReasonArea = new JTextArea();
        visitorReasonArea.setFont(new Font("Tahoma", Font.PLAIN, 16));
        visitorReasonArea.setRows(4);
        visitorReasonArea.setWrapStyleWord(true);
        visitorReasonArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(visitorReasonArea);
        add(visitorReasonLabel);
        add(scrollPane);

        // Submit request button
        submitRequestButton = new JButton("Submit Visitor Request");
        submitRequestButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        submitRequestButton.addActionListener(this::submitVisitorRequest);
        add(submitRequestButton);

        // Set window location and visibility
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void submitVisitorRequest(ActionEvent e) {
        String visitorName = visitorNameField.getText();
        String visitorContact = visitorContactField.getText();
        String visitorReason = visitorReasonArea.getText();

        // Validate input
        if (visitorName.isEmpty() || visitorContact.isEmpty() || visitorReason.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        // Save request to the database
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234")) {
            String query = "INSERT INTO visitor_requests (student_id, visitor_name, visitor_contact, reason_for_visit, request_date) VALUES (?, ?, ?, ?, NOW())";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, studentID);
            pst.setString(2, visitorName);
            pst.setString(3, visitorContact);
            pst.setString(4, visitorReason);

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Visitor request submitted successfully!");
                this.dispose(); // Close the request page
            } else {
                JOptionPane.showMessageDialog(this, "Error submitting request.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VisitorRequestPage(studentID));
    }
}
