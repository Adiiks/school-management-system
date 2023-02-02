package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.dto.CourseRequest;
import com.adrian.school.management.system.dto.CourseResponse;
import com.adrian.school.management.system.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add-course")
    public void addCourse(@Valid @RequestBody CourseRequest courseRequest) {
        courseService.createCourse(courseRequest);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/remove-course/{courseId}")
    public void removeCourse(@PathVariable Integer courseId) {
        courseService.removeCourse(courseId);
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{courseId}")
    public CourseResponse getCourse(@PathVariable Integer courseId) {
        return courseService.getCourse(courseId);
    }
}
