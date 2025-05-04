package classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewRoomsPage extends JFrame {

    private DefaultTableModel tableModel;
    private JTable roomTable;

    public ViewRoomsPage() {
        setTitle("View All Rooms");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Rooms and Their Capacities", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

       
        String[] columns = {"Room ID", "Room Number", "Capacity"};
        tableModel = new DefaultTableModel(columns, 0);
        roomTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(roomTable);
        add(tableScrollPane, BorderLayout.CENTER);

       
        loadRoomsDataFromDatabase();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    
    private void loadRoomsDataFromDatabase() {
        try (Connection conn = getConnection()) {
            String query = "SELECT room_id, room_number, capacity FROM rooms";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int roomId = rs.getInt("room_id");
                    String roomNumber = rs.getString("room_number");
                    int capacity = rs.getInt("capacity");

                   
                    tableModel.addRow(new Object[]{roomId, roomNumber, capacity});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading room data from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
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
        SwingUtilities.invokeLater(ViewRoomsPage::new);
    }
}
