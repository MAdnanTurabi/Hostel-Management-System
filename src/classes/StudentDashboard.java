package classes;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {
    private static int studentID;

    public StudentDashboard(int studentID) {
        this.studentID = studentID;
        setTitle("Student Dashboard");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 1)); 

        
        JButton profileButton = new JButton("View Profile");
        JButton roomDetailsButton = new JButton("View Room Details");
        JButton viewComplaintsButton = new JButton("View Complaints");
        JButton submitComplaintButton = new JButton("Submit Complaint");
        JButton misconductReportButton = new JButton("Report Misconduct");
        JButton viewMisconductButton = new JButton("View Misconduct Reports");
        JButton maintenanceRequestButton = new JButton("Submit Maintenance Request");
        JButton viewMaintenanceRequestsButton = new JButton("View Maintenance Requests");
        JButton logoutButton = new JButton("Logout");

       
        profileButton.addActionListener(e -> openProfilePage());
        roomDetailsButton.addActionListener(e -> openRoomDetailsPage());
        viewComplaintsButton.addActionListener(e -> openViewComplaintsPage());
        submitComplaintButton.addActionListener(e -> openSubmitComplaintPage());
        misconductReportButton.addActionListener(e -> openMisconductReportPage());
        viewMisconductButton.addActionListener(e -> openViewMisconductPage()); 
        maintenanceRequestButton.addActionListener(e -> openMaintenanceRequestPage());
        viewMaintenanceRequestsButton.addActionListener(e -> openViewMaintenanceRequestsPage());
        logoutButton.addActionListener(e -> logout());

       
        add(profileButton);
        add(roomDetailsButton);
        add(viewComplaintsButton);
        add(submitComplaintButton);
        add(misconductReportButton);
        add(viewMisconductButton); 
        add(maintenanceRequestButton);
        add(viewMaintenanceRequestsButton);
        add(logoutButton);

        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void openProfilePage() {
        JOptionPane.showMessageDialog(this, "Student Profile Page (To be implemented)");
    }

    private void openRoomDetailsPage() {
        JOptionPane.showMessageDialog(this, "Room Details Page (To be implemented)");
    }

    private void openViewComplaintsPage() {
        JOptionPane.showMessageDialog(this, "View Complaints Page (To be implemented)");
    }

    private void openSubmitComplaintPage() {
        JOptionPane.showMessageDialog(this, "Submit Complaint Page (To be implemented)");
    }

    private void openMisconductReportPage() {
        SwingUtilities.invokeLater(() -> new SubmitMisconduct(studentID));
    }

    private void openViewMisconductPage() {
        SwingUtilities.invokeLater(() -> new ViewMisconduct(studentID));
    }

    private void openMaintenanceRequestPage() {
        SwingUtilities.invokeLater(() -> new SubmitMaintenance(studentID));
    }

    private void openViewMaintenanceRequestsPage() {
        SwingUtilities.invokeLater(() -> new ViewMaintenance(studentID));
    }

    private void logout() {
        dispose();
        SwingUtilities.invokeLater(() -> new LoginPage());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDashboard(studentID));
    }
}
