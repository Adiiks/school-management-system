package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.IssueConverter;
import com.adrian.school.management.system.dto.IssueRequest;
import com.adrian.school.management.system.entity.Issue;
import com.adrian.school.management.system.entity.Student;
import com.adrian.school.management.system.repository.IssueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    IssueService issueService;

    IssueConverter issueConverter = new IssueConverter();

    @Captor
    ArgumentCaptor<Issue> acIssue;

    @Mock
    IssueRepository issueRepository;

    @BeforeEach
    void setUp() {
        issueService = new IssueService(issueConverter, issueRepository);
    }

    @DisplayName("Raise issue")
    @Test
    void raiseIssue() {
        IssueRequest issueRequest = buildIssueRequest();

        issueService.raiseIssue(issueRequest, new Student());

        verify(issueRepository).save(acIssue.capture());

        Issue issue = acIssue.getValue();

        assertNotNull(issue);
        assertFalse(issue.isResolved());
        assertNotNull(issue.getStudent());
    }

    private IssueRequest buildIssueRequest() {
        return new IssueRequest("Stipend", "Some details");
    }

    @DisplayName("Resolve issue - not found")
    @Test
    void resolveIssueNotFound() {
        when(issueRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> issueService.resolveIssue(1));
    }

    @DisplayName("Resolve issue")
    @Test
    void resolveIssue() {
        Issue issueDb = buildIssue();

        when(issueRepository.findById(anyInt())).thenReturn(Optional.of(issueDb));

        issueService.resolveIssue(1);

        verify(issueRepository).save(acIssue.capture());

        Issue issueUpdated = acIssue.getValue();

        assertTrue(issueUpdated.isResolved());
    }

    private Issue buildIssue() {
        return Issue.builder()
                .details("some details")
                .type("Scholarship")
                .id(1)
                .resolved(false)
                .student(new Student())
                .build();
    }
}