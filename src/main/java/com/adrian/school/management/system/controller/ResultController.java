package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.auth.AuthenticationFacade;
import com.adrian.school.management.system.dto.ExamResults;
import com.adrian.school.management.system.dto.ResultReport;
import com.adrian.school.management.system.dto.ResultResponse;
import com.adrian.school.management.system.service.ResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    private final AuthenticationFacade authFacade;

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{courseName}")
    public List<ResultResponse> getStudentResultsForCourse(@PathVariable String courseName) {
        String userEmail = authFacade.getUserEmail();

        return resultService.getStudentResultsForCourse(userEmail, courseName);
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void addMarks(@Valid @RequestBody ExamResults examResults) {
        resultService.addMarks(examResults);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/generate-report")
    public List<ResultReport> generateReport() {
        String studentEmail = authFacade.getUserEmail();

        return resultService.generateReport(studentEmail);
    }
}
