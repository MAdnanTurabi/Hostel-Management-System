package classes;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Welcome, Admin!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Buttons
        JButton roomAppButton = new JButton("Approve/Reject Room Applications");
        JButton facilityButton = new JButton("Facility Management");
        JButton maintenanceButton = new JButton("Maintenance");
        JButton paymentsButton = new JButton("Payments");
        JButton checkInOutButton = new JButton("Check-In / Check-Out");
        JButton visitorRulesButton = new JButton("Visitor Rules & Timings");

        // Button Actions
        roomAppButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Room Application Management - To Be Implemented"));
        facilityButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Facility Management - To Be Implemented"));
        maintenanceButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Maintenance Requests - To Be Implemented"));
        paymentsButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Payments Module - To Be Implemented"));
        checkInOutButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Check-In / Check-Out - To Be Implemented"));

        // ✅ Actual redirection to VisitorRulesManager
        visitorRulesButton.addActionListener(e -> new VisitorRulesManager());

        // Add buttons to panel
        buttonPanel.add(roomAppButton);
        buttonPanel.add(facilityButton);
        buttonPanel.add(maintenanceButton);
        buttonPanel.add(paymentsButton);
        buttonPanel.add(checkInOutButton);
        buttonPanel.add(visitorRulesButton);

        // Add panel to frame
        add(buttonPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
