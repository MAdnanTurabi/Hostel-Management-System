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

        String[] userTypes = {"Student", "Warden", "Admin"};
        userTypeComboBox = new JComboBox<>(userTypes);
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        usernameField.setVisible(false);
        passwordField.setVisible(false);

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

            Integer studentId = validateStudentLogin(username, password);

            if (studentId != null) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose();
                SwingUtilities.invokeLater(() -> new StudentDashboard(studentId).setVisible(true)); 
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Only Student Login is implemented!", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private Integer validateStudentLogin(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/HostelManagement"; 
        String user = "root";
        String pass = "1234";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            System.out.println("Connected to database successfully!"); 

            String query = "SELECT student_id, password FROM students WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String storedHashedPassword = rs.getString("password");
                        String inputHashedPassword = hashPassword(password);

                        System.out.println("Stored Hash: " + storedHashedPassword); 
                        System.out.println("Input Hash: " + inputHashedPassword); 
                        if (storedHashedPassword.equals(inputHashedPassword)) {
                            return rs.getInt("student_id"); 
                        }
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null; 
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
            return password; 
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage());
    }
}
