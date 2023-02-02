package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.dto.ExamRequest;
import com.adrian.school.management.system.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createExam(@Valid @RequestBody ExamRequest examRequest) {
        examService.createExam(examRequest);
    }
}
