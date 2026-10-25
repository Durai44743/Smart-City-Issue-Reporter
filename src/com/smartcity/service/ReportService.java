package com.smartcity.service;

import com.smartcity.model.Issue;
import com.smartcity.util.Dates;
import java.io.*;
import java.nio.file.Files;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/** File I/O, serialization, random access and multithreading (Units III & IV). */
public class ReportService implements Reportable {
    private final IssueService issues = IssueService.getInstance();

    public static String fileName(YearMonth ym) {
        return "SmartCity_Report_" + ym.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                + "_" + ym.getYear() + ".txt";
    }

    /** Sequential write using Streams API + BufferedWriter. */
    @Override public File generateReport(YearMonth ym) throws IOException {
        List<Issue> list = issues.all().stream()
                .filter(i -> YearMonth.from(i.getCreatedAt()).equals(ym))
                .collect(Collectors.toList());
        File file = new File("reports", fileName(ym));
        file.getParentFile().mkdirs();
        try (BufferedWriter w = Files.newBufferedWriter(file.toPath())) {
            w.write("SMART CITY ISSUE REPORT - " + ym.getMonth() + " " + ym.getYear());
            w.newLine();
            w.write("Total issues: " + list.size());
            w.newLine();
            w.write("By status   : " + IssueService.groupCount(list, Issue::getStatus));
            w.newLine();
            w.write("By category : " + IssueService.groupCount(list, Issue::getCategory));
            w.newLine();
            w.write("------------------------------------------------------------");
            w.newLine();
            for (Issue i : list) {
                w.write(String.format("%d | %-11s | %-22s | %-8s | %-11s | %s",
                        i.getId(), i.getCategory(), i.getLocation(), i.getPriority(), i.getStatus(),
                        Dates.format(i.getCreatedAt())));
                w.newLine();
            }
        }
        return file;
    }

    /** Runs on a background thread so the Swing window never freezes (Unit III). */
    public void generateAsync(final YearMonth ym, final Consumer<File> onDone, final Consumer<Exception> onError) {
        Thread worker = new Thread(new Runnable() {          // anonymous inner class
            @Override public void run() {
                try {
                    Thread.sleep(600);                       // simulate heavy work
                    onDone.accept(generateReport(ym));
                } catch (Exception e) {
                    onError.accept(e);
                }
            }
        }, "report-worker");
        worker.setPriority(Thread.NORM_PRIORITY);
        worker.start();
    }

    /** Random-order read (Unit IV): jump straight to the first bytes of the file. */
    public String readPreview(File file, int bytes) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            byte[] buf = new byte[(int) Math.min(bytes, raf.length())];
            raf.seek(0);
            raf.readFully(buf);
            return new String(buf);
        }
    }

    /** Object serialization (Unit IV): binary backup of every issue. */
    public File backup(File target) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(target))) {
            out.writeObject(new ArrayList<>(issues.all()));
        }
        return target;
    }

    @SuppressWarnings("unchecked")
    public List<Issue> readBackup(File source) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(source))) {
            return (List<Issue>) in.readObject();
        }
    }
}
