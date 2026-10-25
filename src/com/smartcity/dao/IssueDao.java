package com.smartcity.dao;

import com.smartcity.exception.DataAccessException;
import com.smartcity.model.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/** JDBC implementation of the generic Repository (Unit IV). */
public class IssueDao implements Repository<Issue, Integer> {

    public int nextId() {
        try (Connection c = Database.get(); Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT MAX(id) FROM issues")) {
            int max = rs.next() ? rs.getInt(1) : 0;
            return Math.max(max + 1, 1001);          // IDs start at 1001 like the wireframe
        } catch (SQLException e) {
            throw new DataAccessException("Could not generate ID", e);
        }
    }

    @Override public void save(Issue i) {
        String sql = "INSERT INTO issues VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, i.getId());
            ps.setString(2, i.getCategory());
            ps.setString(3, i.getLocation());
            ps.setString(4, i.getDescription());
            ps.setString(5, i.getPriority().name());
            ps.setString(6, i.getDepartment());
            ps.setString(7, i.getStatus().name());
            ps.setString(8, i.getPhoto());
            ps.setString(9, i.getReporterEmail());
            ps.setString(10, i.getReporterName());
            ps.setString(11, i.getCreatedAt().toString());
            ps.setString(12, i.getUpdatedAt().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not save issue", e);
        }
    }

    @Override public void update(Issue i) {
        String sql = "UPDATE issues SET department=?, priority=?, status=?, updated_at=? WHERE id=?";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, i.getDepartment());
            ps.setString(2, i.getPriority().name());
            ps.setString(3, i.getStatus().name());
            ps.setString(4, i.getUpdatedAt().toString());
            ps.setInt(5, i.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not update issue", e);
        }
    }

    @Override public Optional<Issue> findById(Integer id) {
        List<Issue> list = query("SELECT * FROM issues WHERE id=?", String.valueOf(id));
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override public List<Issue> findAll() { return query("SELECT * FROM issues ORDER BY id DESC"); }

    public List<Issue> findByReporter(String email) {
        return query("SELECT * FROM issues WHERE reporter_email=? ORDER BY id DESC", email);
    }

    private List<Issue> query(String sql, String... params) {
        List<Issue> list = new ArrayList<>();
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int k = 0; k < params.length; k++) ps.setString(k + 1, params[k]);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Query failed", e);
        }
        return list;
    }

    private Issue map(ResultSet rs) throws SQLException {
        Issue i = new Issue();
        i.setId(rs.getInt("id"));
        i.setCategory(rs.getString("category"));
        i.setLocation(rs.getString("location"));
        i.setDescription(rs.getString("description"));
        i.setPriority(Priority.valueOf(rs.getString("priority")));
        i.setDepartment(rs.getString("department"));
        i.setStatus(Status.valueOf(rs.getString("status")));
        i.setPhoto(rs.getString("photo"));
        i.setReporterEmail(rs.getString("reporter_email"));
        i.setReporterName(rs.getString("reporter_name"));
        i.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
        i.setUpdatedAt(LocalDateTime.parse(rs.getString("updated_at")));
        return i;
    }

    // ---- history & feedback ----
    public void addHistory(int issueId, String text) {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("INSERT INTO history(issue_id,at,text) VALUES(?,?,?)")) {
            ps.setInt(1, issueId);
            ps.setString(2, LocalDateTime.now().toString());
            ps.setString(3, text);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not write history", e);
        }
    }

    public List<HistoryEntry> history(int issueId) {
        List<HistoryEntry> list = new ArrayList<>();
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("SELECT at,text FROM history WHERE issue_id=? ORDER BY id")) {
            ps.setInt(1, issueId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(new HistoryEntry(LocalDateTime.parse(rs.getString(1)), rs.getString(2)));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not read history", e);
        }
        return list;
    }

    public void saveFeedback(int issueId, int rating, String comments) {
        String sql = "INSERT OR REPLACE INTO feedback(issue_id,rating,comments) VALUES(?,?,?)";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, issueId);
            ps.setInt(2, rating);
            ps.setString(3, comments);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not save feedback", e);
        }
    }
}
