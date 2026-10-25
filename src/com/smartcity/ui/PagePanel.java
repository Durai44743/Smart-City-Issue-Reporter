package com.smartcity.ui;

import javax.swing.*;
import java.awt.*;

/** Base class for every screen: title on top, padded body (inheritance, Unit II). */
public abstract class PagePanel extends JPanel {
    protected PagePanel(String title) {
        super(new BorderLayout(0, 12));
        setBackground(Theme.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        add(Theme.heading(title), BorderLayout.NORTH);
    }
    protected void info(String msg)  { JOptionPane.showMessageDialog(this, msg, "Smart City", JOptionPane.INFORMATION_MESSAGE); }
    protected void error(String msg) { JOptionPane.showMessageDialog(this, msg, "Smart City", JOptionPane.ERROR_MESSAGE); }
}
