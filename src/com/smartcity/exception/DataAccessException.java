package com.smartcity.exception;

/** Unchecked wrapper around SQLException so UI code stays clean. */
public class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Throwable cause) { super(message, cause); }
}
