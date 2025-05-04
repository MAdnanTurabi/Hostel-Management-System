package classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SubmitFeedback extends JFrame {
    private static int studentID;

    private JComboBox<String> facilityComboBox;
    private JTextArea feedbackTextArea;
    private JButton submitButton;

    public SubmitFeedback(int studentID) {
        this.studentID = studentID;
        setTitle("Submit Feedback");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 1)); 

        
        JLabel facilityLabel = new JLabel("Select Facility:");
        facilityLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        add(facilityLabel);

        
        String[] facilities = {"Gym", "Washrooms", "Mess"};
        facilityComboBox = new JComboBox<>(facilities);
        add(facilityComboBox);

       
        JLabel feedbackLabel = new JLabel("Enter Your Feedback:");
        feedbackLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        add(feedbackLabel);

        
        feedbackTextArea = new JTextArea();
        feedbackTextArea.setFont(new Font("Tahoma", Font.PLAIN, 16));
        feedbackTextArea.setRows(5);
        feedbackTextArea.setWrapStyleWord(true);
        feedbackTextArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(feedbackTextArea);
        add(scrollPane);

        
        submitButton = new JButton("Submit Feedback");
        submitButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        add(submitButton);
        
       
        submitButton.addActionListener(this::submitFeedback);

      
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void submitFeedback(ActionEvent e) {
        String selectedFacility = (String) facilityComboBox.getSelectedItem();
        String feedbackText = feedbackTextArea.getText();

      
        if (feedbackText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your feedback.");
            return;
        }

        
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234")) {
            String query = "INSERT INTO feedback (student_id, facility, feedback_text) VALUES (?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, studentID);
            pst.setString(2, selectedFacility);
            pst.setString(3, feedbackText);

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Feedback submitted successfully!");
                this.dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Error submitting feedback.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SubmitFeedback(studentID));
    }
}
