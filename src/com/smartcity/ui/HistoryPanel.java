package com.smartcity.ui;

import com.smartcity.exception.InvalidInputException;
import com.smartcity.exception.IssueNotFoundException;
import com.smartcity.model.HistoryEntry;
import com.smartcity.model.Issue;
import com.smartcity.service.IssueService;
import com.smartcity.util.Dates;
import com.smartcity.util.Validator;
import javax.swing.*;
import java.awt.*;

public class HistoryPanel extends PagePanel {
    public HistoryPanel() {
        super("Issue History");
        JTextField id = new JTextField(10);
        JButton search = Theme.button("Show", Theme.BLUE);
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.setOpaque(false);
        bar.add(new JLabel("Issue ID"));
        bar.add(id);
        bar.add(search);
        JTextArea out = new JTextArea();
        out.setEditable(false);
        out.setFont(new Font("Monospaced", Font.PLAIN, 13));

        search.addActionListener(e -> {
            try {
                Issue i = IssueService.getInstance().track(Validator.parseId(id.getText()));
                StringBuilder sb = new StringBuilder("Issue History - #" + i.getId() + "\n\n");
                for (HistoryEntry h : IssueService.getInstance().history(i.getId()))
                    sb.append(Dates.format(h.getAt())).append("   ").append(h.getText()).append('\n');
                out.setText(sb.toString());
            } catch (InvalidInputException | IssueNotFoundException ex) {
                error(ex.getMessage());
            }
        });
        JPanel body = new JPanel(new BorderLayout(0, 8));
        body.setOpaque(false);
        body.add(bar, BorderLayout.NORTH);
        body.add(new JScrollPane(out), BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);
    }
}
