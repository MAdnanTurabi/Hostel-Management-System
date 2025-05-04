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

        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(9, 1, 10, 10)); 
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //Buttons
        JButton roomAppButton = new JButton("Approve/Reject Room Applications");
        JButton facilityButton = new JButton("Facility Management");
        JButton maintenanceButton = new JButton("Maintenance");
        JButton visitorRulesButton = new JButton("Visitor Rules & Timings");
        JButton manageRoomsButton = new JButton("Manage Rooms");
        JButton visitorRequestButton = new JButton("Approve/Reject Visitor Requests");
        JButton viewRoomsButton = new JButton("View All Rooms");  

        
        JButton logoutButton = new JButton("Logout");

        //Button Actions
        roomAppButton.addActionListener(e -> new RoomApplicationsPage());
        facilityButton.addActionListener(e -> new FacilityManagementPage());
        maintenanceButton.addActionListener(e -> new MaintenancePage());
        visitorRulesButton.addActionListener(e -> new VisitorRulesManager());
        visitorRequestButton.addActionListener(e -> new VisitorRequestApprovalPage());
        manageRoomsButton.addActionListener(e -> new ManageRoomsPage());
        viewRoomsButton.addActionListener(e -> new ViewRoomsPage()); 

        
        logoutButton.addActionListener(e -> {
            dispose(); 
            new LoginPage(); 
        });

       
        buttonPanel.add(roomAppButton);
        buttonPanel.add(facilityButton);
        buttonPanel.add(maintenanceButton);
        buttonPanel.add(visitorRulesButton);
        buttonPanel.add(manageRoomsButton);
        buttonPanel.add(visitorRequestButton);
        buttonPanel.add(viewRoomsButton); 

        
        buttonPanel.add(logoutButton);

        
        add(buttonPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminDashboard::new);
    }
}
