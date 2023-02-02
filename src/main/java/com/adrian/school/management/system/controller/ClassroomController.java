package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.dto.ClassroomRequest;
import com.adrian.school.management.system.dto.ClassroomResponse;
import com.adrian.school.management.system.service.ClassroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classrooms")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createClassroom(@Valid @RequestBody ClassroomRequest classroomRequest) {
        classroomService.createClassroom(classroomRequest);
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{classroomId}")
    public ClassroomResponse getClassroom(@PathVariable Integer classroomId) {
        return classroomService.getClassroom(classroomId);
    }
}
