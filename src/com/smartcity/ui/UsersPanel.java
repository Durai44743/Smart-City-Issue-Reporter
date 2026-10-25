package com.smartcity.ui;

import com.smartcity.dao.UserDao;
import com.smartcity.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UsersPanel extends PagePanel {
    public UsersPanel() {
        super("Users");
        DefaultTableModel model = Theme.model("Name", "E-mail", "Role");
        for (User u : new UserDao().findAll())
            model.addRow(new Object[]{u.getName(), u.getEmail(), u.getRole()});   // polymorphism
        add(new JScrollPane(Theme.table(model)), java.awt.BorderLayout.CENTER);
    }
}
