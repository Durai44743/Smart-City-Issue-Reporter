package com.smartcity.ui;

import com.smartcity.model.User;
import javax.swing.*;
import java.awt.*;

public class CitizenDashboardPanel extends PagePanel {
    public CitizenDashboardPanel(Shell shell) {
        super("Citizen Dashboard");
        User u = shell.getUser();
        JLabel msg = new JLabel(u.welcomeMessage());          // polymorphic call
        msg.setFont(new Font("SansSerif", Font.ITALIC, 15));
        msg.setForeground(Theme.GREEN);

        JPanel grid = new JPanel(new GridLayout(2, 2, 16, 16));
        grid.setOpaque(false);
        grid.add(Theme.actionCard("Report New Issue", "Submit a new complaint", Theme.BLUE, () -> shell.navigate("report")));
        grid.add(Theme.actionCard("My Complaints", "View your reported issues", Theme.GREEN, () -> shell.navigate("complaints")));
        grid.add(Theme.actionCard("Track Complaint", "Check status by ID", Theme.ORANGE, () -> shell.navigate("track")));
        grid.add(Theme.actionCard("Feedback", "Rate our service", Theme.PURPLE, () -> shell.navigate("feedback")));

        JPanel body = new JPanel(new BorderLayout(0, 14));
        body.setOpaque(false);
        body.add(msg, BorderLayout.NORTH);
        body.add(grid, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);
    }
}
