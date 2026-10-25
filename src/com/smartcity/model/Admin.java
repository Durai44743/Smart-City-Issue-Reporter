package com.smartcity.model;

public class Admin extends User {
    public Admin(String name, String email) { super(name, email); }
    @Override public String getRole() { return "Administrator"; }
    @Override public String welcomeMessage() { return "Manage and resolve city issues"; }
}
