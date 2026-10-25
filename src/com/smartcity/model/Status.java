package com.smartcity.model;

public enum Status {
    PENDING("Pending"), ASSIGNED("Assigned"), IN_PROGRESS("In Progress"), RESOLVED("Resolved");
    private final String label;
    Status(String label) { this.label = label; }
    @Override public String toString() { return label; }
}
