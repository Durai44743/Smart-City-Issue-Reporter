package com.smartcity.dao;

import com.smartcity.exception.DataAccessException;
import java.sql.*;

/** JDBC connection + schema (Unit IV: Databases with JDBC). */
public final class Database {
    private static final String URL = "jdbc:sqlite:smartcity.db";
    private Database() { }

    public static Connection get() throws SQLException { return DriverManager.getConnection(URL); }

    public static void init() {
        try (Connection c = get(); Statement s = c.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS users(email TEXT PRIMARY KEY, name TEXT NOT NULL, "
                    + "password TEXT NOT NULL, role TEXT NOT NULL)");
            s.execute("CREATE TABLE IF NOT EXISTS issues(id INTEGER PRIMARY KEY, category TEXT, location TEXT, "
                    + "description TEXT, priority TEXT, department TEXT, status TEXT, photo TEXT, "
                    + "reporter_email TEXT, reporter_name TEXT, created_at TEXT, updated_at TEXT)");
            s.execute("CREATE TABLE IF NOT EXISTS history(id INTEGER PRIMARY KEY AUTOINCREMENT, issue_id INTEGER, "
                    + "at TEXT, text TEXT)");
            s.execute("CREATE TABLE IF NOT EXISTS feedback(issue_id INTEGER PRIMARY KEY, rating INTEGER, comments TEXT)");
        } catch (SQLException e) {
            throw new DataAccessException("Cannot initialise database (is the sqlite-jdbc jar in lib/?): "
                    + e.getMessage(), e);
        }
        new UserDao().seedDefaults();
    }
}
