package com.smartcity.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Shared colours and small UI factory methods. */
public final class Theme {
    public static final Color NAVY = new Color(0x0B2A55), BLUE = new Color(0x1E6FD9), BG = new Color(0xF2F6FB);
    public static final Color GREEN = new Color(0x2E9E5B), ORANGE = new Color(0xF5A623),
            PURPLE = new Color(0x7B4FC9), RED = new Color(0xD64545), TEAL = new Color(0x1E88E5);
    private Theme() { }

    public static JButton button(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static JLabel heading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 20));
        l.setForeground(NAVY);
        return l;
    }

    public static DefaultTableModel model(String... columns) {
        return new DefaultTableModel(columns, 0) {              // anonymous inner class: read-only cells
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    public static JTable table(DefaultTableModel m) {
        JTable t = new JTable(m);
        t.setRowHeight(26);
        t.getTableHeader().setBackground(NAVY);
        t.getTableHeader().setForeground(Color.WHITE);
        t.setSelectionBackground(new Color(0xCFE3FF));
        t.setSelectionForeground(Color.BLACK);
        return t;
    }

    public static void addRow(JPanel form, int row, String label, JComponent field) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridy = row; g.insets = new Insets(5, 5, 5, 5); g.anchor = GridBagConstraints.NORTHWEST;
        g.gridx = 0; g.weightx = 0;
        form.add(new JLabel(label), g);
        g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        form.add(field, g);
    }

    /** Coloured dashboard tile that reacts to mouse clicks (Unit V: event handling). */
    public static JPanel actionCard(String title, String sub, Color color, final Runnable onClick) {
        final JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBackground(color);
        p.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        JLabel t = new JLabel(title);
        t.setForeground(Color.WHITE);
        t.setFont(new Font("SansSerif", Font.BOLD, 17));
        JLabel s = new JLabel(sub);
        s.setForeground(new Color(255, 255, 255, 220));
        p.add(t); p.add(s);
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        p.addMouseListener(new MouseAdapter() {                  // adapter class + anonymous inner class
            @Override public void mouseClicked(MouseEvent e) { onClick.run(); }
        });
        return p;
    }

    public static JPanel statCard(String label, String value, Color color) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, color),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        JLabel v = new JLabel(value);
        v.setFont(new Font("SansSerif", Font.BOLD, 28));
        v.setForeground(color);
        p.add(v);
        p.add(new JLabel(label));
        return p;
    }
}
