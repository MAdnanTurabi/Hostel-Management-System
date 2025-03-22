package classes;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {
    
    public StudentDashboard() {
        setTitle("Student Dashboard");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 1));

        // Buttons for student functionalities
        JButton profileButton = new JButton("View Profile");
        JButton roomDetailsButton = new JButton("View Room Details");
        JButton viewComplaintsButton = new JButton("View Complaints");
        JButton submitComplaintButton = new JButton("Submit Complaint");
        JButton misconductReportButton = new JButton("Report Misconduct");
        JButton maintenanceRequestButton = new JButton("Submit Maintenance Request");
        JButton logoutButton = new JButton("Logout");

        // Actions
        profileButton.addActionListener(e -> openProfilePage());
        roomDetailsButton.addActionListener(e -> openRoomDetailsPage());
        viewComplaintsButton.addActionListener(e -> openViewComplaintsPage());
        submitComplaintButton.addActionListener(e -> openSubmitComplaintPage());
        misconductReportButton.addActionListener(e -> openMisconductReportPage());
        maintenanceRequestButton.addActionListener(e -> openMaintenanceRequestPage());
        logoutButton.addActionListener(e -> logout());

        // Add buttons to frame
        add(profileButton);
        add(roomDetailsButton);
        add(viewComplaintsButton);
        add(submitComplaintButton);
        add(misconductReportButton);
        add(maintenanceRequestButton);
        add(logoutButton);

        // Show Frame
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void openProfilePage() {
        JOptionPane.showMessageDialog(this, "Student Profile Page (To be implemented)");
        // Future: SwingUtilities.invokeLater(() -> new StudentProfilePage());
    }

    private void openRoomDetailsPage() {
        JOptionPane.showMessageDialog(this, "Room Details Page (To be implemented)");
        // Future: SwingUtilities.invokeLater(() -> new RoomDetailsPage());
    }

    private void openViewComplaintsPage() {
        JOptionPane.showMessageDialog(this, "View Complaints Page (To be implemented)");
        // Future: SwingUtilities.invokeLater(() -> new ViewComplaintsPage());
    }

    private void openSubmitComplaintPage() {
        JOptionPane.showMessageDialog(this, "Submit Complaint Page (To be implemented)");
        // Future: SwingUtilities.invokeLater(() -> new SubmitComplaintPage());
    }

    private void openMisconductReportPage() {
        JOptionPane.showMessageDialog(this, "Report Misconduct Page (To be implemented)");
        // Future: SwingUtilities.invokeLater(() -> new MisconductReportPage());
    }

    private void openMaintenanceRequestPage() {
        JOptionPane.showMessageDialog(this, "Submit Maintenance Request Page (To be implemented)");
        // Future: SwingUtilities.invokeLater(() -> new MaintenanceRequestPage());
    }

    private void logout() {
        dispose();
        SwingUtilities.invokeLater(() -> new LoginPage());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDashboard());
    }
}
