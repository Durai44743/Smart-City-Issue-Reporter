package com.smartcity.ui;

import com.smartcity.exception.IssueNotFoundException;
import com.smartcity.model.Issue;
import com.smartcity.model.Priority;
import com.smartcity.model.Status;
import com.smartcity.model.User;
import com.smartcity.service.IssueService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageIssuePanel extends PagePanel {
    private final IssueService svc = IssueService.getInstance();
    private final DefaultTableModel model = Theme.model("ID", "Category", "Location", "Priority", "Status");
    private final JTable table = Theme.table(model);
    private final JLabel caption = new JLabel("Select an issue from the table");
    private final JComboBox<String> department = new JComboBox<>(IssueService.departments().toArray(new String[0]));
    private final JComboBox<Priority> priority = new JComboBox<>(Priority.values());
    private final JComboBox<Status> status = new JComboBox<>(Status.values());
    private final JTextArea remarks = new JTextArea(3, 30);
    private List<Issue> current;
    private int selectedId = -1;

    public ManageIssuePanel(User admin) {
        super("Manage Issues");
        reload();
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (e.getValueIsAdjusting() || row < 0) return;
            Issue i = current.get(row);
            selectedId = i.getId();
            caption.setText("Manage Issue - #" + i.getId() + "   (" + i.getCategory() + ", " + i.getLocation() + ")");
            department.setSelectedItem(i.getDepartment());
            priority.setSelectedItem(i.getPriority());
            status.setSelectedItem(i.getStatus());
            remarks.setText("");
        });

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        caption.setFont(caption.getFont().deriveFont(Font.BOLD));
        Theme.addRow(form, 0, "Issue", caption);
        Theme.addRow(form, 1, "Department", department);
        Theme.addRow(form, 2, "Priority", priority);
        Theme.addRow(form, 3, "Status", status);
        Theme.addRow(form, 4, "Remarks", new JScrollPane(remarks));

        JButton update = Theme.button("Update", Theme.BLUE);
        JButton cancel = Theme.button("Cancel", Color.GRAY);
        update.addActionListener(e -> {
            if (selectedId < 0) { error("Please select an issue first."); return; }
            try {
                svc.update(selectedId, (Status) status.getSelectedItem(), (String) department.getSelectedItem(),
                        (Priority) priority.getSelectedItem(), remarks.getText(), admin);
                info("Issue #" + selectedId + " updated.");
                reload();
            } catch (IssueNotFoundException ex) {
                error(ex.getMessage());
            }
        });
        cancel.addActionListener(e -> { table.clearSelection(); selectedId = -1; remarks.setText("");
            caption.setText("Select an issue from the table"); });
        JPanel buttons = new JPanel(new GridLayout(1, 2, 8, 0));
        buttons.add(update);
        buttons.add(cancel);

        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.setOpaque(false);
        bottom.add(form, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(table), bottom);
        split.setResizeWeight(0.45);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private void reload() {
        current = svc.all();
        model.setRowCount(0);
        current.forEach(i -> model.addRow(new Object[]{i.getId(), i.getCategory(), i.getLocation(),
                i.getPriority(), i.getStatus()}));
    }
}
