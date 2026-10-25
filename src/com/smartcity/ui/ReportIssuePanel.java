package com.smartcity.ui;

import com.smartcity.exception.InvalidInputException;
import com.smartcity.model.Issue;
import com.smartcity.model.Priority;
import com.smartcity.service.IssueService;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ReportIssuePanel extends PagePanel {
    private final JComboBox<String> category = new JComboBox<>(IssueService.categories().toArray(new String[0]));
    private final JTextField location = new JTextField();
    private final JTextArea description = new JTextArea(4, 30);
    private final JComboBox<Priority> priority = new JComboBox<>(Priority.values());
    private final JComboBox<String> department = new JComboBox<>(IssueService.departments().toArray(new String[0]));
    private final JLabel photoLabel = new JLabel("No file chosen");
    private String photoPath;

    public ReportIssuePanel(Shell shell) {
        super("Report New Issue");
        priority.setSelectedItem(Priority.MEDIUM);
        department.setEnabled(false);                       // chosen automatically from the category
        description.setLineWrap(true);
        description.setWrapStyleWord(true);

        category.addActionListener(e ->
                department.setSelectedItem(IssueService.departmentFor((String) category.getSelectedItem())));
        category.setSelectedIndex(0);

        JButton choose = new JButton("Choose File");
        choose.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                photoPath = f.getAbsolutePath();
                photoLabel.setText(f.getName());
            }
        });
        JPanel photo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        photo.setOpaque(false);
        photo.add(choose);
        photo.add(Box.createHorizontalStrut(8));
        photo.add(photoLabel);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        Theme.addRow(form, 0, "Category", category);
        Theme.addRow(form, 1, "Location", location);
        Theme.addRow(form, 2, "Description", new JScrollPane(description));
        Theme.addRow(form, 3, "Priority", priority);
        Theme.addRow(form, 4, "Department", department);
        Theme.addRow(form, 5, "Upload Photo", photo);

        JButton submit = Theme.button("Submit", Theme.BLUE);
        submit.addActionListener(e -> {
            try {
                Issue issue = IssueService.getInstance().submit((String) category.getSelectedItem(),
                        location.getText(), description.getText(), (Priority) priority.getSelectedItem(),
                        photoPath, shell.getUser());
                info("Issue Submitted Successfully!\n\nIssue ID: " + issue.getId()
                        + "\nCategory: " + issue.getCategory()
                        + "\nLocation: " + issue.getLocation()
                        + "\nPriority: " + issue.getPriority()
                        + "\nDepartment: " + issue.getDepartment()
                        + "\nStatus: " + issue.getStatus());
                shell.navigate("complaints");
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid input", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel wrap = new JPanel(new BorderLayout(0, 10));
        wrap.setOpaque(false);
        wrap.add(form, BorderLayout.NORTH);
        wrap.add(submit, BorderLayout.SOUTH);
        add(wrap, BorderLayout.CENTER);
    }
}
