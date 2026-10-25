package com.smartcity.model;

public enum Priority {
    LOW("Low"), MEDIUM("Medium"), HIGH("High"), CRITICAL("Critical");
    private final String label;
    Priority(String label) { this.label = label; }
    @Override public String toString() { return label; }
}
