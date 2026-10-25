package com.smartcity.model;

import java.io.Serializable;

/** Abstract superclass (Unit II: abstract classes, inheritance). */
public abstract class User implements Serializable {
    private final String name;      // private fields + public getters
    private final String email;     // (access specifiers / encapsulation)

    protected User(String name, String email) {   // constructor
        this.name = name;
        this.email = email;
    }

    public String getName()  { return name; }
    public String getEmail() { return email; }

    public abstract String getRole();
    public abstract String welcomeMessage();

    @Override public String toString() { return name + " (" + getRole() + ")"; }
}
