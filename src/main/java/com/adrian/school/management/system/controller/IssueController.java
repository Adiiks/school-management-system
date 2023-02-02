package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.auth.AuthenticationFacade;
import com.adrian.school.management.system.dto.IssueRequest;
import com.adrian.school.management.system.entity.Student;
import com.adrian.school.management.system.service.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final AuthenticationFacade authFacade;
    private final IssueService issueService;

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void raiseIssue(@Valid @RequestBody IssueRequest issueRequest) {
        Student student = authFacade.getStudent();

        issueService.raiseIssue(issueRequest, student);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/resolve/{issueId}")
    public void resolveIssue(@PathVariable Integer issueId) {
        issueService.resolveIssue(issueId);
    }
}
