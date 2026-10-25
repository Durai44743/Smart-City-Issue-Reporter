package com.smartcity.ui;

import com.smartcity.dao.UserDao;
import com.smartcity.exception.AuthenticationException;
import com.smartcity.exception.InvalidInputException;
import com.smartcity.model.User;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private final JTextField email = new JTextField(20);
    private final JPasswordField password = new JPasswordField(20);
    private final UserDao users = new UserDao();

    public LoginPanel(MainFrame frame) {
        super(new GridBagLayout());                                  // centres the card
        setBackground(Theme.NAVY);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));
        JLabel title = new JLabel("Smart City Issue Reporter", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Theme.NAVY);
        JLabel tag = new JLabel("A Cleaner  \u2022  Safer  \u2022  Better Tomorrow", SwingConstants.CENTER);
        tag.setForeground(Theme.BLUE);

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridwidth = 2; g.insets = new Insets(6, 0, 6, 0); g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 0; card.add(title, g);
        g.gridy = 1; card.add(tag, g);
        g.gridwidth = 1; g.gridy = 2; g.gridx = 0; card.add(new JLabel("E-mail"), g);
        g.gridx = 1; card.add(email, g);
        g.gridy = 3; g.gridx = 0; card.add(new JLabel("Password"), g);
        g.gridx = 1; card.add(password, g);
        JButton login = Theme.button("Login", Theme.BLUE);
        JButton register = new JButton("Don't have an account? Register");
        register.setBorderPainted(false);
        register.setContentAreaFilled(false);
        register.setForeground(Theme.BLUE);
        g.gridx = 0; g.gridwidth = 2; g.gridy = 4; card.add(login, g);
        g.gridy = 5; card.add(register, g);
        JLabel hint = new JLabel("Demo: admin@smartcity.com / admin123   |   rahul@smartcity.com / rahul123",
                SwingConstants.CENTER);
        hint.setFont(hint.getFont().deriveFont(10f));
        g.gridy = 6; card.add(hint, g);
        add(card);

        // Event handling with a lambda
        login.addActionListener(e -> {
            try {
                User u = users.login(email.getText(), new String(password.getPassword()));
                frame.showHome(u);
            } catch (AuthenticationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Login failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        password.addActionListener(e -> login.doClick());   // Enter key

        register.addActionListener(e -> openRegisterDialog());
    }

    private void openRegisterDialog() {
        JTextField n = new JTextField(), m = new JTextField();
        JPasswordField p = new JPasswordField();
        Object[] fields = {"Full name", n, "E-mail", m, "Password (min 6)", p};
        if (JOptionPane.showConfirmDialog(this, fields, "Register as Citizen",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                users.register(n.getText(), m.getText(), new String(p.getPassword()));
                JOptionPane.showMessageDialog(this, "Registered! You can log in now.");
                email.setText(m.getText());
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid input", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
