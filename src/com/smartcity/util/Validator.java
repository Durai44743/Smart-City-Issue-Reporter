package com.smartcity.util;

import com.smartcity.exception.InvalidInputException;
import java.util.regex.Pattern;

/** Regular expressions (Unit IV) + wrapper classes / autoboxing (Unit III). */
public final class Validator {
    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private Validator() { }

    public static void requireEmail(String email) throws InvalidInputException {
        if (email == null || !EMAIL.matcher(email.trim()).matches())
            throw new InvalidInputException("Please enter a valid e-mail address.");
    }

    public static void requireText(String value, String field, int minLength) throws InvalidInputException {
        if (value == null || value.trim().length() < minLength)
            throw new InvalidInputException(field + " must have at least " + minLength + " characters.");
    }

    /** String -> Integer (wrapper class) with friendly error. */
    public static int parseId(String text) throws InvalidInputException {
        try {
            Integer id = Integer.valueOf(text.trim());   // wrapper + autoboxing
            return id;                                     // auto-unboxing
        } catch (NumberFormatException | NullPointerException e) {
            throw new InvalidInputException("Issue ID must be a number.");
        }
    }
}
