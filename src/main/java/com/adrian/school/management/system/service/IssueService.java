package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.IssueConverter;
import com.adrian.school.management.system.dto.IssueRequest;
import com.adrian.school.management.system.entity.Issue;
import com.adrian.school.management.system.entity.Student;
import com.adrian.school.management.system.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueConverter issueConverter;
    private final IssueRepository issueRepository;

    @Transactional
    public void raiseIssue(IssueRequest issueRequest, Student student) {
        Issue issue = issueConverter.convertIssueRequestToIssue(issueRequest);

        issue.setResolved(false);
        issue.setStudent(student);

        issueRepository.save(issue);
    }

    @Transactional
    public void resolveIssue(Integer issueId) {
        Issue issueDb = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Issue with id " + issueId + " not found."));

        issueDb.setResolved(true);

        issueRepository.save(issueDb);
    }
}
