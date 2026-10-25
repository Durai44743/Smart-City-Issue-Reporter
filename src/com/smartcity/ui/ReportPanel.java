package com.smartcity.ui;

import com.smartcity.service.ReportService;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.YearMonth;

public class ReportPanel extends PagePanel {
    private final ReportService reports = new ReportService();
    private final JLabel status = new JLabel(" ");
    private final JTextArea preview = new JTextArea();
    private final JButton download = Theme.button("Download", Theme.GREEN);
    private File lastReport;

    public ReportPanel() {
        super("Generate Report");
        YearMonth now = YearMonth.now();
        YearMonth[] months = new YearMonth[12];
        for (int k = 0; k < 12; k++) months[k] = now.minusMonths(k);
        JComboBox<YearMonth> month = new JComboBox<>(months);
        JButton generate = Theme.button("Generate Report", Theme.BLUE);
        JButton backup = Theme.button("Backup (serialize)", Theme.PURPLE);
        download.setEnabled(false);
        preview.setEditable(false);
        preview.setFont(new Font("Monospaced", Font.PLAIN, 12));

        generate.addActionListener(e -> {
            generate.setEnabled(false);
            status.setText("Generating report in background thread...");
            reports.generateAsync((YearMonth) month.getSelectedItem(),
                file -> SwingUtilities.invokeLater(() -> {          // back on the Swing thread
                    lastReport = file;
                    status.setText("Report Generated! " + file.getName());
                    try { preview.setText(reports.readPreview(file, 2000)); }
                    catch (IOException ex) { preview.setText("Could not preview: " + ex.getMessage()); }
                    download.setEnabled(true);
                    generate.setEnabled(true);
                }),
                err -> SwingUtilities.invokeLater(() -> {
                    status.setText("Failed: " + err.getMessage());
                    generate.setEnabled(true);
                }));
        });

        download.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File(lastReport.getName()));
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    Files.copy(lastReport.toPath(), fc.getSelectedFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
                    info("Saved to " + fc.getSelectedFile());
                } catch (IOException ex) { error(ex.getMessage()); }
            }
        });

        backup.addActionListener(e -> {
            try {
                File f = reports.backup(new File("issues_backup.ser"));
                info("Backup saved: " + f.getAbsolutePath() + "\nIssues stored: " + reports.readBackup(f).size());
            } catch (IOException | ClassNotFoundException ex) { error(ex.getMessage()); }
        });

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bar.setOpaque(false);
        bar.add(new JLabel("Select Month"));
        bar.add(month);
        bar.add(generate);
        bar.add(download);
        bar.add(backup);
        JPanel top = new JPanel(new BorderLayout(0, 6));
        top.setOpaque(false);
        top.add(bar, BorderLayout.NORTH);
        top.add(status, BorderLayout.SOUTH);
        JPanel body = new JPanel(new BorderLayout(0, 8));
        body.setOpaque(false);
        body.add(top, BorderLayout.NORTH);
        body.add(new JScrollPane(preview), BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);
    }
}
