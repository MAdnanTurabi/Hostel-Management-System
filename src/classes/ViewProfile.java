package classes;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewProfile extends JFrame {
    private static int studentID;

    public ViewProfile(int studentID) {
        this.studentID = studentID;
        setTitle("View Profile");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(0, 1, 10, 10));

        JLabel nameLabel = new JLabel();
        JLabel emailLabel = new JLabel();
        JLabel roomLabel = new JLabel();
        JLabel phoneLabel = new JLabel();
        phoneLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(phoneLabel);


        nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        emailLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        roomLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));

        add(nameLabel);
        add(emailLabel);
        add(roomLabel);

        loadProfileData(nameLabel, emailLabel, roomLabel, phoneLabel);


        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadProfileData(JLabel nameLabel, JLabel emailLabel, JLabel roomLabel, JLabel phoneLabel) {
        String query = "SELECT first_name, last_name, email, mobile_number, room_number FROM students WHERE student_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234");
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, studentID);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String email = rs.getString("email");
                    String mobile = rs.getString("mobile_number");
                    String roomNumber = rs.getString("room_number");

                    nameLabel.setText("Name: " + firstName + " " + lastName);
                    emailLabel.setText("Email: " + email);
                    phoneLabel.setText("Phone: " + mobile);
                    roomLabel.setText("Room: " + (roomNumber != null ? roomNumber : "No Room Assigned"));
                } else {
                    JOptionPane.showMessageDialog(this, "Student not found.");
                    dispose();
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewProfile(studentID)); 
    }
}
