package classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MaintenancePage extends JFrame {
    private JTable table;
    private DefaultTableModel model;

 
    public static class DB {
        private static final String URL = "jdbc:mysql://localhost:3306/hostel_management"; 
        private static final String USER = "root"; 
        private static final String PASSWORD = "1234"; 

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }

    public MaintenancePage() {
        setTitle("Maintenance Requests");
        setSize(850, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

     
        String[] columns = {"Request ID", "Student", "Issue", "Status", "Assigned Staff"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

       
        JPanel buttonPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        JButton assignBtn = new JButton("Assign Staff");
        JButton completeBtn = new JButton("Mark as Done");

        buttonPanel.add(refreshBtn);
        buttonPanel.add(assignBtn);
        buttonPanel.add(completeBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadRequests());
        assignBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int requestId = (int) model.getValueAt(row, 0);
                assignStaff(requestId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a request to assign.");
            }
        });
        completeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int requestId = (int) model.getValueAt(row, 0);
                markRequestAsDone(requestId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a request to mark as done.");
            }
        });

        loadRequests();
        setVisible(true);
    }

    private void loadRequests() {
        model.setRowCount(0);

        try (Connection con = DB.getConnection();
             Statement stmt = con.createStatement()) {

            String query = """
                SELECT mr.request_id, s.first_name, s.last_name, mr.issue_description, mr.status, ms.name AS staff_name
                FROM maintenance_requests mr
                JOIN students s ON mr.student_id = s.student_id
                LEFT JOIN maintenance_staff ms ON mr.assigned_staff_id = ms.staff_id
            """;

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int requestId = rs.getInt("request_id");
                String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
                String issue = rs.getString("issue_description");
                String status = rs.getString("status");
                String staff = rs.getString("staff_name") != null ? rs.getString("staff_name") : "Unassigned";

                model.addRow(new Object[]{requestId, studentName, issue, status, staff});
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading maintenance requests.");
        }
    }

    private void assignStaff(int requestId) {
        try (Connection con = DB.getConnection()) {
            
            PreparedStatement ps = con.prepareStatement("SELECT staff_id, name FROM maintenance_staff WHERE is_available = TRUE");
            ResultSet rs = ps.executeQuery();

            DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
            Map<String, Integer> staffMap = new HashMap<>();

            while (rs.next()) {
                String name = rs.getString("name");
                int id = rs.getInt("staff_id");
                staffMap.put(name, id);
                comboModel.addElement(name);
            }

            if (comboModel.getSize() == 0) {
                JOptionPane.showMessageDialog(this, "No available staff.");
                return;
            }

            JComboBox<String> comboBox = new JComboBox<>(comboModel);
            int result = JOptionPane.showConfirmDialog(this, comboBox, "Assign Staff", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String selected = (String) comboBox.getSelectedItem();
                int staffId = staffMap.get(selected);

               
                PreparedStatement update = con.prepareStatement("""
                    UPDATE maintenance_requests SET assigned_staff_id = ?, status = 'In Progress' WHERE request_id = ?
                """);
                update.setInt(1, staffId);
                update.setInt(2, requestId);
                update.executeUpdate();

                
                PreparedStatement markBusy = con.prepareStatement("UPDATE maintenance_staff SET is_available = FALSE WHERE staff_id = ?");
                markBusy.setInt(1, staffId);
                markBusy.executeUpdate();

                JOptionPane.showMessageDialog(this, "Staff assigned.");
                loadRequests();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error assigning staff.");
        }
    }

    private void markRequestAsDone(int requestId) {
        try (Connection con = DB.getConnection()) {
            
            PreparedStatement getStaff = con.prepareStatement("SELECT assigned_staff_id FROM maintenance_requests WHERE request_id = ?");
            getStaff.setInt(1, requestId);
            ResultSet rs = getStaff.executeQuery();

            int staffId = -1;
            if (rs.next()) {
                staffId = rs.getInt("assigned_staff_id");
            }

            if (staffId == -1) {
                JOptionPane.showMessageDialog(this, "No staff assigned to this request.");
                return;
            }

            
            PreparedStatement updateRequest = con.prepareStatement("""
                UPDATE maintenance_requests SET status = 'Resolved', assigned_staff_id = NULL WHERE request_id = ?
            """);
            updateRequest.setInt(1, requestId);
            updateRequest.executeUpdate();

           
            PreparedStatement markAvailable = con.prepareStatement("UPDATE maintenance_staff SET is_available = TRUE WHERE staff_id = ?");
            markAvailable.setInt(1, staffId);
            markAvailable.executeUpdate();

            JOptionPane.showMessageDialog(this, "Request marked as resolved and staff unassigned.");
            loadRequests();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating request.");
        }
    }

}
