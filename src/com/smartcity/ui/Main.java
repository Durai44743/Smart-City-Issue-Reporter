package com.smartcity.ui;

import com.smartcity.dao.Database;
import com.smartcity.exception.DataAccessException;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            Database.init();
        } catch (DataAccessException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Startup error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
