package com.smartcity.service;

import com.smartcity.dao.IssueDao;
import com.smartcity.exception.InvalidInputException;
import com.smartcity.exception.IssueNotFoundException;
import com.smartcity.model.*;
import com.smartcity.util.Validator;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Business logic. Uses collections, generics, lambdas and the Streams API (Unit IV). */
public class IssueService implements Trackable {
    private static final IssueService INSTANCE = new IssueService();   // static member
    public static IssueService getInstance() { return INSTANCE; }

    private static final Map<String, String> DEPARTMENTS = new LinkedHashMap<>();
    static {
        DEPARTMENTS.put("Pothole", "Road & Transport");
        DEPARTMENTS.put("Garbage", "Sanitation");
        DEPARTMENTS.put("Water", "Water Supply");
        DEPARTMENTS.put("Electricity", "Electricity Board");
        DEPARTMENTS.put("Drainage", "Drainage Board");
    }

    private final IssueDao dao = new IssueDao();
    private IssueService() { }

    public static List<String> categories()  { return new ArrayList<>(DEPARTMENTS.keySet()); }
    public static List<String> departments() { return new ArrayList<>(new LinkedHashSet<>(DEPARTMENTS.values())); } // Set removes duplicates
    public static String departmentFor(String category) { return DEPARTMENTS.getOrDefault(category, "General Administration"); }

    /** synchronized (Unit III): two threads must never receive the same issue ID. */
    public synchronized Issue submit(String category, String location, String description,
                                     Priority priority, String photo, User reporter) throws InvalidInputException {
        Validator.requireText(location, "Location", 3);
        Validator.requireText(description, "Description", 10);
        assert priority != null : "priority must be chosen";      // Assertions (Unit III), run with -ea

        Issue issue = new Issue(category, location.trim(), description.trim(), priority, photo, reporter);
        issue.setId(dao.nextId());
        issue.setDepartment(departmentFor(category));
        dao.save(issue);
        dao.addHistory(issue.getId(), "Complaint submitted by " + reporter.getName());
        return issue;
    }

    @Override public Issue track(int issueId) throws IssueNotFoundException {
        return dao.findById(issueId).orElseThrow(() -> new IssueNotFoundException(issueId));   // lambda
    }

    public List<Issue> all() { return dao.findAll(); }
    public List<Issue> mine(User u) { return dao.findByReporter(u.getEmail()); }
    public List<HistoryEntry> history(int id) { return dao.history(id); }

    public List<Issue> recent(int n) {
        return all().stream()
                .sorted(Comparator.comparing(Issue::getCreatedAt).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public void update(int id, Status status, String department, Priority priority, String remarks, User admin)
            throws IssueNotFoundException {
        Issue issue = track(id);
        if (issue.getDepartment() != null && !issue.getDepartment().equals(department))
            dao.addHistory(id, "Assigned to " + department + " Department by " + admin.getName());
        if (issue.getStatus() != status)
            dao.addHistory(id, "Status changed from " + issue.getStatus().name().replace('_', ' ')
                    + " to " + status.name().replace('_', ' ') + " by " + admin.getName());
        if (remarks != null && !remarks.trim().isEmpty())
            dao.addHistory(id, "Remarks: " + remarks.trim());
        issue.setDepartment(department);
        issue.setPriority(priority);
        issue.setStatus(status);
        issue.setUpdatedAt(LocalDateTime.now());
        dao.update(issue);
    }

    public void feedback(int issueId, int rating, String comments) throws InvalidInputException {
        if (rating < 1 || rating > 5) throw new InvalidInputException("Rating must be 1 to 5.");
        dao.saveFeedback(issueId, rating, comments);
    }

    /** Generic method (Unit IV): works for any element type and grouping key. */
    public static <T, K> Map<K, Long> groupCount(Collection<T> items, Function<T, K> keyFn) {
        return items.stream().collect(Collectors.groupingBy(keyFn, Collectors.counting()));
    }

    public Map<Status, Long> countByStatus() {
        Map<Status, Long> counts = groupCount(all(), Issue::getStatus);
        for (Status s : Status.values()) counts.putIfAbsent(s, 0L);
        return counts;
    }
}
