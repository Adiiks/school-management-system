package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.dto.UserRequest;
import com.adrian.school.management.system.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add-teacher")
    public void addTeacher(@Valid @RequestBody UserRequest teacherRequest) {
        teacherService.createTeacher(teacherRequest);
    }
}
