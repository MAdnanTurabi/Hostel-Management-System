package classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewRoomDetails extends JFrame {
    private static int studentID;
    private JComboBox<String> roomComboBox;
    private JTextArea reasonTextArea;
    private JButton submitBookingButton;
    private JPanel bookingPanel;
    private JScrollPane reasonScrollPane;

    public ViewRoomDetails(int studentID) {
        this.studentID = studentID;
        setTitle("View Room Details");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

       
        JLabel roomLabel = new JLabel("Available Rooms:");
        roomLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));

        roomComboBox = new JComboBox<>();
        roomComboBox.setFont(new Font("Tahoma", Font.PLAIN, 16));

      
        bookingPanel = new JPanel();
        bookingPanel.setLayout(new BorderLayout(10, 10));
        bookingPanel.add(roomLabel, BorderLayout.NORTH);
        bookingPanel.add(roomComboBox, BorderLayout.CENTER);

        submitBookingButton = new JButton("Submit Room Booking Application");
        submitBookingButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        bookingPanel.add(submitBookingButton, BorderLayout.SOUTH);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 1, 10, 10));  
        mainPanel.add(bookingPanel);

       
        add(mainPanel, BorderLayout.CENTER);

     
        bookingPanel.setVisible(true);

      
        loadRoomDetails();

       
        submitBookingButton.addActionListener(this::submitRoomBookingApplication);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadRoomDetails() {
        roomComboBox.removeAllItems();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234")) {
            String query = """
                SELECT r.room_number
                FROM rooms r
                LEFT JOIN students s ON r.room_number = s.room_number
                GROUP BY r.room_number, r.capacity
                HAVING COUNT(s.student_id) < r.capacity
                """;

            Statement stmt = conn.createStatement();
            try (ResultSet rs = stmt.executeQuery(query)) {
                boolean hasAvailableRooms = false;

                while (rs.next()) {
                    String roomNumber = rs.getString("room_number");
                    roomComboBox.addItem("Room " + roomNumber);
                    hasAvailableRooms = true;
                }

                if (!hasAvailableRooms) {
                    JOptionPane.showMessageDialog(this, "No rooms with available capacity at the moment.");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }



    private void submitRoomBookingApplication(ActionEvent e) {
        String selectedRoom = (String) roomComboBox.getSelectedItem();
        String roomNumber = selectedRoom.split(" ")[1]; 

        if (selectedRoom == null || selectedRoom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a room.");
            return;
        }

       
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234")) {
            String query = "INSERT INTO room_applications (student_id, room_number, application_type, reason) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, studentID);
            pst.setInt(2, Integer.parseInt(roomNumber));
            pst.setString(3, "book"); 
            pst.setString(4, "");  

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Room Booking Application submitted successfully!");
                dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Error submitting application.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewRoomDetails(studentID)); 
    }
}
