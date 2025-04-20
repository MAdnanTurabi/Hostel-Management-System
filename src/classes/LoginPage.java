package classes;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeComboBox;
    private JButton loginButton;

    public LoginPage() {
        setTitle("Login");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        String[] userTypes = {"Student", "Warden", "Admin"};
        userTypeComboBox = new JComboBox<>(userTypes);
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        usernameField.setVisible(true);
        passwordField.setVisible(true);

        userTypeComboBox.addActionListener(e -> toggleFields());
        loginButton.addActionListener(e -> loginUser());

        add(userTypeComboBox);
        add(new JLabel("Username:", SwingConstants.CENTER));
        add(usernameField);
        add(new JLabel("Password:", SwingConstants.CENTER));
        add(passwordField);
        add(loginButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void toggleFields() {
        String selected = (String) userTypeComboBox.getSelectedItem();
        boolean show = selected.equals("Student") || selected.equals("Admin");
        usernameField.setVisible(show);
        passwordField.setVisible(show);
        revalidate();
        repaint();
    }

    private void loginUser() {
        String userType = (String) userTypeComboBox.getSelectedItem();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter both Username and Password!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userType.equals("Student")) {
            Integer studentId = validateStudentLogin(username, password);
            if (studentId != null) {
                JOptionPane.showMessageDialog(this, "Student Login Successful!");
                dispose();
                SwingUtilities.invokeLater(() -> new StudentDashboard(studentId).setVisible(true));
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Student Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (userType.equals("Admin")) {
            boolean valid = validateAdminLogin(username, password);
            if (valid) {
                JOptionPane.showMessageDialog(this, "Admin Login Successful!");
                dispose();
                SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Admin Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Warden login is not implemented yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private Integer validateStudentLogin(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/hostel_management";
        String user = "root";
        String pass = "1234";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            System.out.println("Connected to the database...");

            String query = "SELECT student_id, password FROM students WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Student found in database.");

                        String storedPassword = rs.getString("password");

                        // Directly compare the entered password with the stored password
                        if (storedPassword.equals(password)) {
                            System.out.println("Passwords match.");
                            return rs.getInt("student_id");
                        } else {
                            System.out.println("Passwords don't match.");
                        }
                    } else {
                        System.out.println("No student found with that username.");
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }


    private boolean validateAdminLogin(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/hostel_management";
        String user = "root";
        String pass = "1234";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "SELECT password FROM admins WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String storedPassword = rs.getString("password");
                        return storedPassword.equals(password); // Compare plain text passwords
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }
}
