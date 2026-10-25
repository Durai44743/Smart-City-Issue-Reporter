package com.smartcity.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Date and Time API helpers (Unit I). */
public final class Dates {
    private static final DateTimeFormatter UI = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
    private Dates() { }
    public static String format(LocalDateTime t) { return t == null ? "-" : t.format(UI); }
}
