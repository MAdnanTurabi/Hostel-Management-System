package classes;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.*;

public class StudentRegister extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField firstname, lastname, email, username, mob;
    private JPasswordField passwordField;
    private JButton btnRegister, btnExit;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                StudentRegister frame = new StudentRegister();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public StudentRegister() {
        setTitle("Student Registration");
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\User\\Desktop\\STDM.jpg"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewUserRegister = new JLabel("New Student Register");
        lblNewUserRegister.setFont(new Font("Times New Roman", Font.PLAIN, 42));
        lblNewUserRegister.setBounds(362, 52, 325, 50);
        contentPane.add(lblNewUserRegister);

        addLabelAndField("First Name", 58, 152, firstname = new JTextField());
        addLabelAndField("Last Name", 58, 243, lastname = new JTextField());
        addLabelAndField("Email", 58, 324, email = new JTextField());
        addLabelAndField("Username", 542, 159, username = new JTextField());
        addLabelAndField("Password", 542, 245, passwordField = new JPasswordField());
        addLabelAndField("Mobile Number", 542, 329, mob = new JTextField());

       
        btnRegister = new JButton("Register");
        btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 22));
        btnRegister.setBounds(350, 447, 259, 50);
        contentPane.add(btnRegister);
        btnRegister.addActionListener(this::registerStudent);

        
        btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnExit.setBounds(750, 500, 100, 40);
        contentPane.add(btnExit);
        btnExit.addActionListener(this::exitToMainMenu);
    }

    private void addLabelAndField(String labelText, int x, int y, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Tahoma", Font.PLAIN, 20));
        label.setBounds(x, y, 160, 30);
        contentPane.add(label);

        field.setFont(new Font("Tahoma", Font.PLAIN, 20));
        field.setBounds(x + 160, y, 228, 40);
        contentPane.add(field);
    }

    private void registerStudent(ActionEvent e) {
        String firstName = firstname.getText();
        String lastName = lastname.getText();
        String emailId = email.getText();
        String userName = username.getText();
        String mobileNumber = mob.getText();
        String password = new String(passwordField.getPassword());

        if (firstName.isEmpty() || lastName.isEmpty() || emailId.isEmpty() ||
            userName.isEmpty() || mobileNumber.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        if (mobileNumber.length() != 10) {
            JOptionPane.showMessageDialog(this, "Enter a valid 10-digit mobile number");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/HostelManagement", "root", "1234")) {
            String hashedPassword = hashPassword(password);
            String query = "INSERT INTO students (first_name, last_name, username, password, email, mobile_number) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, userName);
            pst.setString(4, hashedPassword);
            pst.setString(5, emailId);
            pst.setString(6, mobileNumber);

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Registration Successful!");
                exitToMainMenu(null); // Redirect to main menu after success
            } else {
                JOptionPane.showMessageDialog(this, "Error: Could not register.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }

    private void exitToMainMenu(ActionEvent e) {
        System.out.println("Attempting to open MainMenu...");
        
        SwingUtilities.invokeLater(() -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
        });

        System.out.println("Closing StudentRegister window...");
        this.dispose(); 
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
}
