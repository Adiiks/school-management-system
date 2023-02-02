package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.dto.StudentRequest;
import com.adrian.school.management.system.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add-student")
    public void addStudent(@Valid @RequestBody StudentRequest studentRequest) {
        studentService.createStudent(studentRequest);
    }
}
