package com.smartcity.ui;

import com.smartcity.exception.InvalidInputException;
import com.smartcity.exception.IssueNotFoundException;
import com.smartcity.model.HistoryEntry;
import com.smartcity.model.Issue;
import com.smartcity.model.Status;
import com.smartcity.service.IssueService;
import com.smartcity.service.Trackable;
import com.smartcity.util.Dates;
import com.smartcity.util.Validator;
import javax.swing.*;
import java.awt.*;

public class TrackPanel extends PagePanel {
    private final JTextField idField = new JTextField(10);
    private final JTextArea details = new JTextArea();
    private final JLabel progress = new JLabel(" ");

    public TrackPanel() {
        super("Track Complaint");
        details.setEditable(false);
        details.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JButton search = Theme.button("Search", Theme.BLUE);
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.setOpaque(false);
        bar.add(new JLabel("Enter Issue ID"));
        bar.add(idField);
        bar.add(search);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(bar, BorderLayout.NORTH);
        top.add(progress, BorderLayout.SOUTH);
        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setOpaque(false);
        center.add(top, BorderLayout.NORTH);
        center.add(new JScrollPane(details), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        Trackable tracker = IssueService.getInstance();       // programming to the interface
        search.addActionListener(e -> {
            try {
                Issue i = tracker.track(Validator.parseId(idField.getText()));
                show(i);
            } catch (InvalidInputException | IssueNotFoundException ex) {   // multi-catch
                details.setText("");
                progress.setText(" ");
                error(ex.getMessage());
            }
        });
    }

    private void show(Issue i) {
        StringBuilder sb = new StringBuilder();
        sb.append("Issue ID    : ").append(i.getId()).append('\n')
          .append("Category    : ").append(i.getCategory()).append('\n')
          .append("Location    : ").append(i.getLocation()).append('\n')
          .append("Priority    : ").append(i.getPriority()).append('\n')
          .append("Department  : ").append(i.getDepartment()).append('\n')
          .append("Status      : ").append(i.getStatus()).append('\n')
          .append("Created At  : ").append(Dates.format(i.getCreatedAt())).append('\n')
          .append("Updated At  : ").append(Dates.format(i.getUpdatedAt())).append("\n\n")
          .append("History\n-------\n");
        for (HistoryEntry h : IssueService.getInstance().history(i.getId()))
            sb.append(Dates.format(h.getAt())).append("  ").append(h.getText()).append('\n');
        details.setText(sb.toString());
        details.setCaretPosition(0);

        StringBuilder bar = new StringBuilder("<html>");
        for (Status s : Status.values()) {
            boolean reached = s.ordinal() <= i.getStatus().ordinal();
            bar.append(reached ? "<b style='color:#1E6FD9'>\u25CF " : "<span style='color:gray'>\u25CB ")
               .append(s).append(reached ? "</b>" : "</span>");
            if (s != Status.RESOLVED) bar.append(" &nbsp;\u2500\u2500\u2500&nbsp; ");
        }
        progress.setText(bar.append("</html>").toString());
    }
}
