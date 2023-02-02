package com.adrian.school.management.system.converter;

import com.adrian.school.management.system.dto.IssueRequest;
import com.adrian.school.management.system.entity.Issue;
import org.springframework.stereotype.Component;

@Component
public class IssueConverter {

    public Issue convertIssueRequestToIssue(IssueRequest issueRequest) {
        return Issue.builder()
                .type(issueRequest.type())
                .details(issueRequest.details())
                .build();
    }
}
