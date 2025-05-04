package classes;

import javax.swing.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Hostel Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new java.awt.GridLayout(3, 1));

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");

        loginButton.addActionListener(e -> {            dispose();
            SwingUtilities.invokeLater(LoginPage::new);
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

            if (choice == 0) { 
                dispose();
                SwingUtilities.invokeLater(() -> new StudentRegister().setVisible(true));
            } else {
                JOptionPane.showMessageDialog(this, "Only student registration is available right now.");
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        add(loginButton);
        add(registerButton);
        add(exitButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new);
    }
}
