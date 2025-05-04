package classes;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {
    private static int studentID;

    public StudentDashboard(int studentID) {
        this.studentID = studentID;
        setTitle("Student Dashboard");
        setSize(400, 650); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(13, 1)); 

      
        JButton profileButton = new JButton("View Profile");
        JButton roomDetailsButton = new JButton("View Room Details");
        JButton viewComplaintsButton = new JButton("View Complaints");
        JButton submitComplaintButton = new JButton("Submit Complaint");
        JButton misconductReportButton = new JButton("Report Misconduct");
        JButton viewMisconductButton = new JButton("View Misconduct Reports");
        JButton maintenanceRequestButton = new JButton("Submit Maintenance Request");
        JButton viewMaintenanceRequestsButton = new JButton("View Maintenance Requests");
        JButton submitVisitorRequestButton = new JButton("Submit Visitor Request");
        JButton viewVisitorRequestStatusButton = new JButton("View Visitor Request Status");
        JButton submitFeedbackButton = new JButton("Submit Feedback");
        JButton viewVisitorRulesButton = new JButton("View Visitor Rules");
        JButton viewFacilityTimingsButton = new JButton("View Facility Timings"); 
        JButton logoutButton = new JButton("Logout");

       
        profileButton.addActionListener(e -> openProfilePage());
        roomDetailsButton.addActionListener(e -> openRoomDetailsPage());
        viewComplaintsButton.addActionListener(e -> openViewComplaintsPage());
        submitComplaintButton.addActionListener(e -> openSubmitComplaintPage());
        misconductReportButton.addActionListener(e -> openMisconductReportPage());
        viewMisconductButton.addActionListener(e -> openViewMisconductPage());
        maintenanceRequestButton.addActionListener(e -> openMaintenanceRequestPage());
        viewMaintenanceRequestsButton.addActionListener(e -> openViewMaintenanceRequestsPage());
        submitVisitorRequestButton.addActionListener(e -> openSubmitVisitorRequestPage());
        viewVisitorRequestStatusButton.addActionListener(e -> openViewVisitorRequestStatusPage());
        submitFeedbackButton.addActionListener(e -> openSubmitFeedbackPage());
        viewVisitorRulesButton.addActionListener(e -> new ViewVisitorRulesPage(studentID));
        viewFacilityTimingsButton.addActionListener(e -> openFacilityTimingsPage()); // NEW
        logoutButton.addActionListener(e -> logout());

        
        add(profileButton);
        add(roomDetailsButton);
        add(viewComplaintsButton);
        add(submitComplaintButton);
        add(misconductReportButton);
        add(viewMisconductButton);
        add(maintenanceRequestButton);
        add(viewMaintenanceRequestsButton);
        add(submitVisitorRequestButton);
        add(viewVisitorRequestStatusButton);
        add(submitFeedbackButton);
        add(viewVisitorRulesButton);
        add(viewFacilityTimingsButton); 
        add(logoutButton);

       
        setLocationRelativeTo(null);
        setVisible(true);
    }

    
    private void openProfilePage() {
        SwingUtilities.invokeLater(() -> new ViewProfile(studentID));
    }

    private void openRoomDetailsPage() {
        SwingUtilities.invokeLater(() -> new ViewRoomDetails(studentID));
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

    private void openFacilityTimingsPage() {
        SwingUtilities.invokeLater(() -> new ViewFacilityTimingsPage(studentID));
    }

    private void logout() {
        dispose(); 
        SwingUtilities.invokeLater(() -> new LoginPage());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDashboard(studentID));
    }
}
