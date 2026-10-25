package com.smartcity.dao;

import com.smartcity.exception.AuthenticationException;
import com.smartcity.exception.DataAccessException;
import com.smartcity.exception.InvalidInputException;
import com.smartcity.model.*;
import com.smartcity.util.Passwords;
import com.smartcity.util.Validator;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    
public void seedDefaults() {
    int userCount = 0;

    // Read the user count and close the database connection first.
    try (Connection c = Database.get();
         Statement s = c.createStatement();
         ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM users")) {

        if (rs.next()) {
            userCount = rs.getInt(1);
        }

    } catch (SQLException e) {
        throw new DataAccessException("Seeding failed", e);
    }

    // Insert default users only after the read connection is closed.
    if (userCount == 0) {
        insert(new Admin("Admin", "admin@smartcity.com"), "admin123");
        insert(new Citizen("Rahul Kumar", "rahul@smartcity.com"), "rahul123");
    }
}


    public void register(String name, String email, String password) throws InvalidInputException {
        Validator.requireText(name, "Name", 3);
        Validator.requireEmail(email);
        Validator.requireText(password, "Password", 6);
        if (exists(email)) throw new InvalidInputException("This e-mail is already registered.");
        insert(new Citizen(name.trim(), email.trim().toLowerCase()), password);
    }

    private void insert(User u, String password) {
        String sql = "INSERT INTO users(email,name,password,role) VALUES(?,?,?,?)";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getName());
            ps.setString(3, Passwords.hash(password));
            ps.setString(4, u instanceof Admin ? "ADMIN" : "CITIZEN");   // instanceof operator
            ps.executeUpdate();
        } catch (SQLException e) {
    e.printStackTrace();
    throw new DataAccessException("Could not save user", e);
}
    }

    public boolean exists(String email) {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM users WHERE email=?")) {
            ps.setString(1, email.trim().toLowerCase());
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            throw new DataAccessException("Lookup failed", e);
        }
    }

    public User login(String email, String password) throws AuthenticationException {
        String sql = "SELECT name, password, role FROM users WHERE email=?";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email.trim().toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next() || !rs.getString("password").equals(Passwords.hash(password)))
                    throw new AuthenticationException("Invalid e-mail or password.");
                String name = rs.getString("name");
                User u = rs.getString("role").equals("ADMIN")
                        ? new Admin(name, email.trim().toLowerCase())
                        : new Citizen(name, email.trim().toLowerCase());
                return u;                                   // polymorphism: returned as User
            }
        } catch (SQLException e) {
            throw new DataAccessException("Login failed", e);
        }
    }

    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        try (Connection c = Database.get(); Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT email,name,role FROM users ORDER BY name")) {
            while (rs.next())
                list.add(rs.getString("role").equals("ADMIN")
                        ? new Admin(rs.getString("name"), rs.getString("email"))
                        : new Citizen(rs.getString("name"), rs.getString("email")));
        } catch (SQLException e) {
            throw new DataAccessException("Could not load users", e);
        }
        return list;
    }
}
