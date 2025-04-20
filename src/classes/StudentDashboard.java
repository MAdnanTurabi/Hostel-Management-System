package classes;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {
    private static int studentID;

    public StudentDashboard(int studentID) {
        this.studentID = studentID;
        setTitle("Student Dashboard");
        setSize(400, 600); // Increased height to accommodate new buttons
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(12, 1)); // Increased row count for additional buttons

        // Create buttons for various functionalities
        JButton profileButton = new JButton("View Profile");
        JButton roomDetailsButton = new JButton("View Room Details");
        JButton viewComplaintsButton = new JButton("View Complaints");
        JButton submitComplaintButton = new JButton("Submit Complaint");
        JButton misconductReportButton = new JButton("Report Misconduct");
        JButton viewMisconductButton = new JButton("View Misconduct Reports");
        JButton maintenanceRequestButton = new JButton("Submit Maintenance Request");
        JButton viewMaintenanceRequestsButton = new JButton("View Maintenance Requests");
        JButton submitVisitorRequestButton = new JButton("Submit Visitor Request"); // NEW
        JButton viewVisitorRequestStatusButton = new JButton("View Visitor Request Status"); // NEW
        JButton submitFeedbackButton = new JButton("Submit Feedback");
        JButton logoutButton = new JButton("Logout");

        // Add action listeners for button clicks
        profileButton.addActionListener(e -> openProfilePage());
        roomDetailsButton.addActionListener(e -> openRoomDetailsPage());
        viewComplaintsButton.addActionListener(e -> openViewComplaintsPage());
        submitComplaintButton.addActionListener(e -> openSubmitComplaintPage());
        misconductReportButton.addActionListener(e -> openMisconductReportPage());
        viewMisconductButton.addActionListener(e -> openViewMisconductPage());
        maintenanceRequestButton.addActionListener(e -> openMaintenanceRequestPage());
        viewMaintenanceRequestsButton.addActionListener(e -> openViewMaintenanceRequestsPage());
        submitVisitorRequestButton.addActionListener(e -> openSubmitVisitorRequestPage()); // NEW
        viewVisitorRequestStatusButton.addActionListener(e -> openViewVisitorRequestStatusPage()); // NEW
        submitFeedbackButton.addActionListener(e -> openSubmitFeedbackPage());
        logoutButton.addActionListener(e -> logout());

        // Add buttons to the layout
        add(profileButton);
        add(roomDetailsButton);
        add(viewComplaintsButton);
        add(submitComplaintButton);
        add(misconductReportButton);
        add(viewMisconductButton);
        add(maintenanceRequestButton);
        add(viewMaintenanceRequestsButton);
        add(submitVisitorRequestButton); // NEW
        add(viewVisitorRequestStatusButton); // NEW
        add(submitFeedbackButton);
        add(logoutButton);

        // Center the window
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Methods to open the corresponding pages
    private void openProfilePage() {
        JOptionPane.showMessageDialog(this, "Student Profile Page (To be implemented)");
    }

    private void openRoomDetailsPage() {
        JOptionPane.showMessageDialog(this, "Room Details Page (To be implemented)");
    }

    private void openViewComplaintsPage() {
        SwingUtilities.invokeLater(() -> new ViewComplaints(studentID));
    }

    private void openSubmitComplaintPage() {
        SwingUtilities.invokeLater(() -> new SubmitComplaint(studentID));
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

    private void openSubmitVisitorRequestPage() {
        SwingUtilities.invokeLater(() -> new VisitorRequestPage(studentID));
    }

    private void openViewVisitorRequestStatusPage() {
        SwingUtilities.invokeLater(() -> new ViewVisitorRequestStatusPage(studentID));
    }

    private void openSubmitFeedbackPage() {
        SwingUtilities.invokeLater(() -> new SubmitFeedback(studentID));
    }

    private void logout() {
        dispose(); // Close the current window
        SwingUtilities.invokeLater(() -> new LoginPage());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDashboard(studentID));
    }
}
