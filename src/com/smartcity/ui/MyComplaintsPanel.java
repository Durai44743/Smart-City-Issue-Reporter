package com.smartcity.ui;

import com.smartcity.model.Issue;
import com.smartcity.model.User;
import com.smartcity.service.IssueService;
import com.smartcity.util.Dates;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class MyComplaintsPanel extends PagePanel {
    public MyComplaintsPanel(User user) {
        super("My Complaints");
        DefaultTableModel model = Theme.model("ID", "Category", "Location", "Priority", "Status", "Created");
        List<Issue> list = IssueService.getInstance().mine(user);
        list.forEach(i -> model.addRow(new Object[]{i.getId(), i.getCategory(), i.getLocation(),
                i.getPriority(), i.getStatus(), Dates.format(i.getCreatedAt())}));
        add(new JScrollPane(Theme.table(model)), java.awt.BorderLayout.CENTER);
        if (list.isEmpty()) add(new JLabel("You have not reported any issues yet."), java.awt.BorderLayout.SOUTH);
    }
}
