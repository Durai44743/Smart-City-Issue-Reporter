package com.smartcity.ui;

import com.smartcity.model.Issue;
import com.smartcity.model.Priority;
import com.smartcity.model.Status;
import com.smartcity.service.IssueService;
import com.smartcity.util.Dates;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class AdminDashboardPanel extends PagePanel {
    public AdminDashboardPanel() {
        super("Admin Dashboard");
        IssueService svc = IssueService.getInstance();
        Map<Status, Long> counts = svc.countByStatus();
        long total = counts.values().stream().mapToLong(Long::longValue).sum();

        JPanel stats = new JPanel(new GridLayout(1, 4, 12, 0));
        stats.setOpaque(false);
        stats.add(Theme.statCard("Total", String.valueOf(total), Theme.BLUE));
        stats.add(Theme.statCard("Pending", String.valueOf(counts.get(Status.PENDING)), Theme.ORANGE));
        stats.add(Theme.statCard("In Progress", String.valueOf(counts.get(Status.IN_PROGRESS)), Theme.TEAL));
        stats.add(Theme.statCard("Resolved", String.valueOf(counts.get(Status.RESOLVED)), Theme.GREEN));

        DefaultTableModel model = Theme.model("ID", "Category", "Location", "Priority", "Status", "Created");
        for (Issue i : svc.recent(8))
            model.addRow(new Object[]{i.getId(), i.getCategory(), i.getLocation(), i.getPriority(),
                    i.getStatus(), Dates.format(i.getCreatedAt())});
        JTable table = Theme.table(model);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {   // anonymous inner class
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                                                                     boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                if (!sel) {
                    c.setForeground(Color.BLACK);
                    String text = String.valueOf(v);
                    if (col == 3 && (text.equals(Priority.CRITICAL.toString()) || text.equals(Priority.HIGH.toString())))
                        c.setForeground(Theme.RED);
                    if (col == 4 && text.equals(Status.RESOLVED.toString())) c.setForeground(Theme.GREEN);
                }
                return c;
            }
        });

        JPanel body = new JPanel(new BorderLayout(0, 12));
        body.setOpaque(false);
        body.add(stats, BorderLayout.NORTH);
        JPanel recent = new JPanel(new BorderLayout(0, 6));
        recent.setOpaque(false);
        recent.add(new JLabel("Recent Issues"), BorderLayout.NORTH);
        recent.add(new JScrollPane(table), BorderLayout.CENTER);
        body.add(recent, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);
    }
}
