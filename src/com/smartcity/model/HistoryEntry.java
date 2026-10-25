package com.smartcity.model;

import java.time.LocalDateTime;

public class HistoryEntry {
    private final LocalDateTime at;
    private final String text;
    public HistoryEntry(LocalDateTime at, String text) { this.at = at; this.text = text; }
    public LocalDateTime getAt() { return at; }
    public String getText() { return text; }
}
