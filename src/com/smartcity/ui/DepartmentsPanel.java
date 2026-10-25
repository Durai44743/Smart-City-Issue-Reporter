package com.smartcity.ui;

import com.smartcity.model.Issue;
import com.smartcity.service.IssueService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map;

public class DepartmentsPanel extends PagePanel {
    public DepartmentsPanel() {
        super("Departments");
        Map<String, Long> counts = IssueService.groupCount(IssueService.getInstance().all(), Issue::getDepartment);
        DefaultTableModel model = Theme.model("Department", "Total issues");
        for (String d : IssueService.departments())
            model.addRow(new Object[]{d, counts.getOrDefault(d, 0L)});
        add(new JScrollPane(Theme.table(model)), java.awt.BorderLayout.CENTER);
    }
}
