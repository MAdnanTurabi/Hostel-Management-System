package classes;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

        // Components
        String[] userTypes = {"Student", "Warden", "Admin"};
        userTypeComboBox = new JComboBox<>(userTypes);
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        // Initially hide username & password fields
        usernameField.setVisible(false);
        passwordField.setVisible(false);

        // Show fields only if "Student" is selected
        userTypeComboBox.addActionListener(e -> toggleFields());

        // Login Button Action
        loginButton.addActionListener(e -> loginUser());

        // Layout
        add(userTypeComboBox);
        add(new JLabel("Username:", SwingConstants.CENTER));
        add(usernameField);
        add(new JLabel("Password:", SwingConstants.CENTER));
        add(passwordField);
        add(loginButton);

        // Show Frame
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void toggleFields() {
        boolean isStudent = userTypeComboBox.getSelectedItem().equals("Student");
        usernameField.setVisible(isStudent);
        passwordField.setVisible(isStudent);
        revalidate();
        repaint();
    }

    private void loginUser() {
        String userType = (String) userTypeComboBox.getSelectedItem();

        if (userType.equals("Student")) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter both Username and Password!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (validateStudentLogin(username, password)) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose();
                SwingUtilities.invokeLater(() -> new StudentDashboard().setVisible(true)); // Redirect to Student Dashboard
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Only Student Login is implemented!", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean validateStudentLogin(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/HostelManagement"; // Check DB Name
        String user = "root";
        String pass = "1234"; // Ensure this is correct

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            System.out.println("Connected to database successfully!"); // Debugging Log

            String query = "SELECT password FROM students WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String storedHashedPassword = rs.getString("password");
                        String inputHashedPassword = hashPassword(password);

                        System.out.println("Stored Hash: " + storedHashedPassword); // Debugging Log
                        System.out.println("Input Hash: " + inputHashedPassword); // Debugging Log

                        return storedHashedPassword.equals(inputHashedPassword);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password; // Return plaintext in case of error
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage());
    }
}
