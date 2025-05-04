package classes;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewVisitorRulesPage extends JFrame {

    public ViewVisitorRulesPage(int studentID) {
        setTitle("Visitor Rules");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

       
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        JLabel headingLabel = new JLabel("Visitor Rules", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 22));
        topPanel.add(headingLabel);

        JLabel timingLabel = new JLabel("", SwingConstants.CENTER);
        timingLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        topPanel.add(timingLabel);
        add(topPanel, BorderLayout.NORTH);

      
        JTextArea rulesArea = new JTextArea();
        rulesArea.setEditable(false);
        rulesArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(rulesArea);
        add(scrollPane, BorderLayout.CENTER);

        
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostel_management", "root", "1234");

          
            Statement timingStmt = conn.createStatement();
            ResultSet timingRs = timingStmt.executeQuery("SELECT visiting_hours FROM visitor_timings LIMIT 1");

            if (timingRs.next()) {
                String visitingHours = timingRs.getString("visiting_hours");
                timingLabel.setText("Visiting Hours: " + visitingHours);
            } else {
                timingLabel.setText("Visiting Hours: Not Available");
            }

          
            Statement rulesStmt = conn.createStatement();
            ResultSet rulesRs = rulesStmt.executeQuery("SELECT rule_description FROM visitor_rules");

            StringBuilder rulesText = new StringBuilder();
            int count = 1;
            while (rulesRs.next()) {
                rulesText.append(count++).append(". ").append(rulesRs.getString("rule_description")).append("\n\n");
            }

            rulesArea.setText(rulesText.length() > 0 ? rulesText.toString() : "No rules found.");

            conn.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            rulesArea.setText("Error loading rules from the database.\n" + ex.getMessage());
        }

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
