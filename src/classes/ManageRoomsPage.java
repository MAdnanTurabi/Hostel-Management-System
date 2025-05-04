package classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageRoomsPage extends JFrame {
    private JTextField roomNumberField;
    private JTextField capacityField;
    private JCheckBox availabilityCheckBox;
    private JButton addRoomButton;
    private JButton removeRoomButton;

    public ManageRoomsPage() {
        setTitle("Manage Rooms");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 10, 10));


        JLabel titleLabel = new JLabel("Manage Rooms", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel);


        JPanel roomNumberPanel = new JPanel();
        roomNumberPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel roomNumberLabel = new JLabel("Room Number: ");
        roomNumberField = new JTextField(10);
        roomNumberPanel.add(roomNumberLabel);
        roomNumberPanel.add(roomNumberField);
        add(roomNumberPanel);

  
        JPanel capacityPanel = new JPanel();
        capacityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel capacityLabel = new JLabel("Capacity: ");
        capacityField = new JTextField(10);
        capacityPanel.add(capacityLabel);
        capacityPanel.add(capacityField);
        add(capacityPanel);

    
        JPanel availabilityPanel = new JPanel();
        availabilityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel availabilityLabel = new JLabel("Available: ");
        availabilityCheckBox = new JCheckBox();
        availabilityPanel.add(availabilityLabel);
        availabilityPanel.add(availabilityCheckBox);
        add(availabilityPanel);

     
        addRoomButton = new JButton("Add Room");
        removeRoomButton = new JButton("Remove Room");

     
        addRoomButton.addActionListener(this::addRoom);
        removeRoomButton.addActionListener(this::removeRoom);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addRoomButton);
        buttonPanel.add(removeRoomButton);
        add(buttonPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addRoom(ActionEvent e) {
        String roomNumber = roomNumberField.getText();
        String capacity = capacityField.getText();
        boolean isAvailable = availabilityCheckBox.isSelected();

        if (roomNumber.isEmpty() || capacity.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

       
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234")) {
            String query = "INSERT INTO rooms (room_number, capacity, is_available) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(roomNumber));
            pst.setInt(2, Integer.parseInt(capacity));
            pst.setBoolean(3, isAvailable);

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Room added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Error adding room.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void removeRoom(ActionEvent e) {
        String roomNumber = roomNumberField.getText();

        if (roomNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the room number.");
            return;
        }

        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234")) {
            String query = "DELETE FROM rooms WHERE room_number = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(roomNumber));

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Room removed successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Room not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManageRoomsPage::new);
    }
}
