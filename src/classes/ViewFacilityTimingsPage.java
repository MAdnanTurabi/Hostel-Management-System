package classes;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class ViewFacilityTimingsPage extends JFrame {
    private static int studentID;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hostel_management";
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = "1234"; 

    public ViewFacilityTimingsPage(int studentID) {
        this.studentID = studentID;
        setTitle("Facility Timings");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        
        String[] columnNames = {"Facility", "Opening Time", "Closing Time"};
        Object[][] data = fetchFacilityTimingsFromDatabase();

        JTable timingsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(timingsTable);

        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());
        add(backButton, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private Object[][] fetchFacilityTimingsFromDatabase() {
        ArrayList<Object[]> rows = new ArrayList<>();

        String query = "SELECT facility, start_time, end_time FROM facility_slots ORDER BY facility, start_time";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String facility = rs.getString("facility");
                Time startTime = rs.getTime("start_time");
                Time endTime = rs.getTime("end_time");

                rows.add(new Object[]{
                    facility,
                    startTime.toString(),
                    endTime.toString()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error fetching facility timings: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }

        return rows.toArray(new Object[0][]);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewFacilityTimingsPage(studentID));
    }
}
