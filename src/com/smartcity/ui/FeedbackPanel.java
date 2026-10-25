package com.smartcity.ui;

import com.smartcity.exception.InvalidInputException;
import com.smartcity.model.Issue;
import com.smartcity.model.Status;
import com.smartcity.model.User;
import com.smartcity.service.IssueService;
import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;

public class FeedbackPanel extends PagePanel {
    public FeedbackPanel(User user) {
        super("Provide Feedback");
        JComboBox<Issue> issues = new JComboBox<>(IssueService.getInstance().mine(user).stream()
                .filter(i -> i.getStatus() == Status.RESOLVED)
                .collect(Collectors.toList()).toArray(new Issue[0]));
        JComboBox<Integer> rating = new JComboBox<>(new Integer[]{5, 4, 3, 2, 1});   // autoboxing
        JLabel stars = new JLabel("\u2605\u2605\u2605\u2605\u2605");
        stars.setForeground(Theme.ORANGE);
        stars.setFont(stars.getFont().deriveFont(22f));
        rating.addActionListener(e -> {
            int r = (Integer) rating.getSelectedItem();         // unboxing
            stars.setText("\u2605".repeat(r) + "\u2606".repeat(5 - r));
        });
        JTextArea comments = new JTextArea(4, 30);
        comments.setLineWrap(true);

        JPanel ratingRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        ratingRow.setOpaque(false);
        ratingRow.add(rating);
        ratingRow.add(stars);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        Theme.addRow(form, 0, "Resolved issue", issues);
        Theme.addRow(form, 1, "Rate your experience", ratingRow);
        Theme.addRow(form, 2, "Comments", new JScrollPane(comments));

        JButton submit = Theme.button("Submit", Theme.BLUE);
        submit.addActionListener(e -> {
            Issue chosen = (Issue) issues.getSelectedItem();
            if (chosen == null) { error("You have no resolved issues to review yet."); return; }
            try {
                IssueService.getInstance().feedback(chosen.getId(), (Integer) rating.getSelectedItem(), comments.getText());
                info("Thank you for your feedback!");
                comments.setText("");
            } catch (InvalidInputException ex) {
                error(ex.getMessage());
            }
        });
        JPanel wrap = new JPanel(new BorderLayout(0, 10));
        wrap.setOpaque(false);
        wrap.add(form, BorderLayout.NORTH);
        wrap.add(submit, BorderLayout.SOUTH);
        add(wrap, BorderLayout.CENTER);
    }
}
