package com.smartcity.ui;

import com.smartcity.model.User;
import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        super("Smart City Issue Reporter");
        setSize(1050, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        showLogin();
    }
    public void showLogin() { setContentPane(new LoginPanel(this)); revalidate(); repaint(); }
    public void showHome(User user) { setContentPane(new Shell(this, user)); revalidate(); repaint(); }
}
