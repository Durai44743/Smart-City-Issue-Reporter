package com.smartcity.exception;

public class IssueNotFoundException extends Exception {
    private final int issueId;
    public IssueNotFoundException(int issueId) {
        super("No issue found with ID " + issueId);
        this.issueId = issueId;
    }
    public int getIssueId() { return issueId; }
}
