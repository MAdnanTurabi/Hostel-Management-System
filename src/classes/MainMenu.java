package classes;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Hostel Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        // Buttons
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");

        // Button Actions
        loginButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginPage());
        });

        registerButton.addActionListener(e -> {
            String[] options = {"Student", "Admin", "Warden"};
            int choice = JOptionPane.showOptionDialog(
                this,
                "Register as:",
                "Registration Type",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );

            if (choice == 0) { // Student
                dispose();
                SwingUtilities.invokeLater(() -> new StudentRegister().setVisible(true));
            }
            // TODO: Implement Admin and Warden registration when needed
        });

        exitButton.addActionListener(e -> System.exit(0));

        // Add buttons to frame
        add(loginButton);
        add(registerButton);
        add(exitButton);

        // Show Frame
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}
