package classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class RoomApplicationsPage extends JFrame {
    private JTable applicationsTable;
    private DefaultTableModel tableModel;

    public RoomApplicationsPage() {
        setTitle("Approve/Reject Room Applications");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        
        JLabel titleLabel = new JLabel("Room Applications", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

     
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Application ID");
        columnNames.add("Student Name");
        columnNames.add("Room Number");
        columnNames.add("Application Type");
        columnNames.add("Status");

        tableModel = new DefaultTableModel(columnNames, 0);
        applicationsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(applicationsTable);
        add(scrollPane, BorderLayout.CENTER);

        
        loadApplicationsData();

       
        JPanel buttonPanel = new JPanel();
        JButton approveButton = new JButton("Approve");
        JButton rejectButton = new JButton("Reject");

        
        approveButton.addActionListener(e -> approveApplication());

       
        rejectButton.addActionListener(e -> rejectApplication());

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadApplicationsData() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234")) {
            String query = "SELECT ra.application_id, s.first_name, s.last_name, ra.room_number, ra.application_type, ra.status "
                         + "FROM room_applications ra "
                         + "JOIN students s ON ra.student_id = s.student_id "
                         + "WHERE ra.status = 'pending'";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(String.valueOf(rs.getInt("application_id")));
                row.add(rs.getString("first_name") + " " + rs.getString("last_name"));
                row.add(String.valueOf(rs.getInt("room_number")));
                row.add(rs.getString("application_type"));
                row.add(rs.getString("status"));
                tableModel.addRow(row);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading applications.");
        }
    }

    private void approveApplication() {
        int selectedRow = applicationsTable.getSelectedRow(); 

        if (selectedRow >= 0) {
            String roomNumberString = (String) tableModel.getValueAt(selectedRow, 2);  
            String studentName = (String) tableModel.getValueAt(selectedRow, 1);  

            
            String[] nameParts = studentName.split(" ");  
            String firstName = nameParts[0];
            String lastName = nameParts[1];

          
            int studentId = getStudentIdByName(firstName, lastName);

            int roomNumber = Integer.parseInt(roomNumberString);  

           
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234")) {
              
                String updateStudentQuery = "UPDATE students SET room_number = ? WHERE student_id = ?";
                try (PreparedStatement pst = conn.prepareStatement(updateStudentQuery)) {
                    pst.setInt(1, roomNumber); 
                    pst.setInt(2, studentId);  
                    int rowsUpdated = pst.executeUpdate();

                    if (rowsUpdated > 0) {
                     
                        String updateApplicationQuery = "UPDATE room_applications SET status = 'approved' WHERE student_id = ? AND room_number = ?";
                        try (PreparedStatement pstApp = conn.prepareStatement(updateApplicationQuery)) {
                            pstApp.setInt(1, studentId);  
                            pstApp.setInt(2, roomNumber);  
                            pstApp.executeUpdate();
                            JOptionPane.showMessageDialog(this, "Room assigned successfully!");
                            tableModel.removeRow(selectedRow); 
                        }
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row.");
        }
    }

    private int getStudentIdByName(String firstName, String lastName) {
        int studentId = -1;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234")) {
            String query = "SELECT student_id FROM students WHERE first_name = ? AND last_name = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, firstName);
            pst.setString(2, lastName);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                studentId = rs.getInt("student_id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return studentId;
    }

    private void rejectApplication() {
        int selectedRow = applicationsTable.getSelectedRow();
        if (selectedRow != -1) {
        	int applicationId = Integer.parseInt((String) tableModel.getValueAt(selectedRow, 0));


          
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234")) {
                String updateQuery = "UPDATE room_applications SET status = 'rejected' WHERE application_id = ?";
                PreparedStatement pst = conn.prepareStatement(updateQuery);
                pst.setInt(1, applicationId);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Room application rejected.");
                tableModel.removeRow(selectedRow);  
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error rejecting application.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an application to reject.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RoomApplicationsPage::new);
    }
}
