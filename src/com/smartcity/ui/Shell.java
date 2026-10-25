package com.smartcity.ui;

import com.smartcity.model.Admin;
import com.smartcity.model.User;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/** Sidebar + content area. Menu depends on the user's role (instanceof / downcasting). */
public class Shell extends JPanel {
    private static final String[][] CITIZEN_MENU = {
        {"dashboard", "Dashboard"}, {"report", "Report New Issue"}, {"complaints", "My Complaints"},
        {"track", "Track Complaint"}, {"feedback", "Feedback"}};
    private static final String[][] ADMIN_MENU = {
        {"dashboard", "Dashboard"}, {"manage", "Manage Issues"}, {"users", "Users"},
        {"departments", "Departments"}, {"reports", "Reports"}, {"history", "Issue History"}};

    private final MainFrame frame;
    private final User user;
    private final JPanel content = new JPanel(new BorderLayout());
    private final Map<String, JButton> buttons = new LinkedHashMap<>();

    public Shell(MainFrame frame, User user) {
        super(new BorderLayout());
        this.frame = frame;
        this.user = user;
        content.setBackground(Theme.BG);
        add(buildSidebar(), BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
        navigate("dashboard");
    }

    public User getUser() { return user; }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(Theme.NAVY);
        side.setPreferredSize(new Dimension(200, 0));
        side.setBorder(BorderFactory.createEmptyBorder(14, 10, 14, 10));

        String role = user.getRole();
        if (user instanceof Admin) {
            Admin a = (Admin) user;                    // downcasting
            role = a.getRole();
        }
        JLabel welcome = new JLabel("<html><div style='color:white'>Welcome,<br><b>" + user.getName()
                + "</b><br><span style='color:#9fc4ff'>" + role + "</span></div></html>");
        welcome.setBorder(BorderFactory.createEmptyBorder(0, 6, 16, 0));
        side.add(welcome);

        String[][] items = (user instanceof Admin) ? ADMIN_MENU : CITIZEN_MENU;
        for (String[] item : items) {
            final String key = item[0];
            JButton b = menuButton(item[1]);
            b.addActionListener(e -> navigate(key));
            buttons.put(key, b);
            side.add(b);
            side.add(Box.createVerticalStrut(4));
        }
        side.add(Box.createVerticalGlue());
        JButton logout = menuButton("Logout");
        logout.setForeground(new Color(0xFF8A8A));
        logout.addActionListener(e -> frame.showLogin());
        side.add(logout);
        return side;
    }

    private JButton menuButton(String text) {
        JButton b = new JButton(text);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setForeground(Color.WHITE);
        b.setBackground(Theme.NAVY);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        return b;
    }

    /** Builds a fresh panel each time so the data is always up to date. */
    public void navigate(String key) {
        JPanel panel;
        switch (key) {
            case "report":      panel = new ReportIssuePanel(this); break;
            case "complaints":  panel = new MyComplaintsPanel(user); break;
            case "track":       panel = new TrackPanel(); break;
            case "feedback":    panel = new FeedbackPanel(user); break;
            case "manage":      panel = new ManageIssuePanel(user); break;
            case "users":       panel = new UsersPanel(); break;
            case "departments": panel = new DepartmentsPanel(); break;
            case "reports":     panel = new ReportPanel(); break;
            case "history":     panel = new HistoryPanel(); break;
            default:
                panel = (user instanceof Admin) ? new AdminDashboardPanel() : new CitizenDashboardPanel(this);
        }
        content.removeAll();
        content.add(panel, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
        buttons.forEach((k, b) -> b.setBackground(k.equals(key) ? Theme.BLUE : Theme.NAVY));
    }
}
