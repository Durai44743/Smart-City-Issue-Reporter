package com.smartcity.service;

import com.smartcity.exception.IssueNotFoundException;
import com.smartcity.model.Issue;

public interface Trackable {
    Issue track(int issueId) throws IssueNotFoundException;
}
