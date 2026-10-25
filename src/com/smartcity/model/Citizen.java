package com.smartcity.model;

public class Citizen extends User {
    public Citizen(String name, String email) { super(name, email); }
    @Override public String getRole() { return "Citizen"; }
    @Override public String welcomeMessage() { return "Together for a Cleaner & Greener City"; }
}
