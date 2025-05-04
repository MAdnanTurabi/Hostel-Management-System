package classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FacilityManagementPage extends JFrame {

    private JComboBox<String> facilityComboBox;
    private JTextField startTimeField, endTimeField;
    private DefaultTableModel tableModel;
    private JTable slotTable;
    private JPanel cardPanel; 
    private JPanel setTimeSlotPanel, viewTimeSlotPanel;

   
    public FacilityManagementPage() {
        setTitle("Facility Management");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

 
        JLabel titleLabel = new JLabel("Facility Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

       
        JPanel buttonPanel = new JPanel();


        add(buttonPanel, BorderLayout.CENTER);

       
        cardPanel = new JPanel();
        add(cardPanel, BorderLayout.SOUTH);

      
        setTimeSlotPanel = createSetTimeSlotPanel();
     
        viewTimeSlotPanel = createViewTimeSlotPanel();

        cardPanel.add(setTimeSlotPanel, "Set Time Slot");
        cardPanel.add(viewTimeSlotPanel, "View Time Slot");

        setLocationRelativeTo(null);
        setVisible(true);

        showSetTimeSlotPanel();  
    }

   
    private JPanel createSetTimeSlotPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

       
        panel.add(new JLabel("Select Facility:"));
        facilityComboBox = new JComboBox<>(new String[]{"Gym", "TV Room", "Study Room"});
        panel.add(facilityComboBox);

        
        panel.add(new JLabel("Start Time (HH:mm):"));
        startTimeField = new JTextField();
        panel.add(startTimeField);

        panel.add(new JLabel("End Time (HH:mm):"));
        endTimeField = new JTextField();
        panel.add(endTimeField);

     
        JButton addSlotButton = new JButton("Add Time Slot");
        addSlotButton.addActionListener(e -> addTimeSlot());
        panel.add(addSlotButton);

        return panel;
    }

    
    private JPanel createViewTimeSlotPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

       
        String[] columns = {"Facility", "Start Time", "End Time"};
        tableModel = new DefaultTableModel(columns, 0);
        slotTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(slotTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        
        JButton deleteSlotButton = new JButton("Delete Selected Slot");
        deleteSlotButton.addActionListener(e -> deleteSelectedSlot());
        panel.add(deleteSlotButton, BorderLayout.PAGE_END);

        loadTimeSlotsFromDatabase();
        return panel;
    }

    
    private void showSetTimeSlotPanel() {
        CardLayout cl = (CardLayout)(cardPanel.getLayout());
        cl.show(cardPanel, "Set Time Slot");
    }

    
    private void showViewTimeSlotPanel() {
        CardLayout cl = (CardLayout)(cardPanel.getLayout());
        cl.show(cardPanel, "View Time Slot");
    }

    
    private void addTimeSlot() {
        String selectedFacility = (String) facilityComboBox.getSelectedItem();
        String startTime = startTimeField.getText();
        String endTime = endTimeField.getText();

        if (startTime.isEmpty() || endTime.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both start and end time.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

       
        if (!isValidTimeFormat(startTime) || !isValidTimeFormat(endTime)) {
            JOptionPane.showMessageDialog(this, "Please enter time in HH:mm format.", "Invalid Time Format", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        if (isSlotExists(selectedFacility)) {
            JOptionPane.showMessageDialog(this, "This facility already has a time slot assigned.", "Duplicate Slot", JOptionPane.WARNING_MESSAGE);
            return;
        }

     
        saveTimeSlotToDatabase(selectedFacility, startTime, endTime);

        
        if (cardPanel.isAncestorOf(viewTimeSlotPanel)) {
            tableModel.addRow(new Object[]{selectedFacility, startTime, endTime});
        }

        
        startTimeField.setText("");
        endTimeField.setText("");
    }

    
    private boolean isValidTimeFormat(String time) {
        try {
            new SimpleDateFormat("HH:mm").parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

   
    private boolean isSlotExists(String facility) {
        try (Connection conn = getConnection()) {
            String query = "SELECT COUNT(*) FROM facility_slots WHERE facility = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, facility);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; 
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    
    private void saveTimeSlotToDatabase(String facility, String startTime, String endTime) {
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO facility_slots (facility, start_time, end_time) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, facility);
                stmt.setString(2, startTime);
                stmt.setString(3, endTime);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving time slot to database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

 
    private void deleteSelectedSlot() {
        int selectedRow = slotTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String facility = (String) tableModel.getValueAt(selectedRow, 0);
        String startTime = (String) tableModel.getValueAt(selectedRow, 1);
        String endTime = (String) tableModel.getValueAt(selectedRow, 2);

        
        tableModel.removeRow(selectedRow);

       
        deleteTimeSlotFromDatabase(facility, startTime, endTime);

        JOptionPane.showMessageDialog(this, "Time slot deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }


    private void deleteTimeSlotFromDatabase(String facility, String startTime, String endTime) {
        try (Connection conn = getConnection()) {
            String query = "DELETE FROM facility_slots WHERE facility = ? AND start_time = ? AND end_time = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, facility);
                stmt.setString(2, startTime);
                stmt.setString(3, endTime);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting time slot from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void loadTimeSlotsFromDatabase() {
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM facility_slots ORDER BY facility, start_time";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String facility = rs.getString("facility");
                    String startTime = rs.getString("start_time");
                    String endTime = rs.getString("end_time");

                    
                    tableModel.addRow(new Object[]{facility, startTime, endTime});
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading time slots from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            throw ex;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FacilityManagementPage::new);
    }
}
