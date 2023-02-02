package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.auth.AuthenticationFacade;
import com.adrian.school.management.system.dto.AttendanceList;
import com.adrian.school.management.system.dto.AttendanceResponse;
import com.adrian.school.management.system.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    private final AuthenticationFacade authFacade;

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void checkAttendance(@Valid @RequestBody AttendanceList attendanceList) {
        attendanceService.checkAttendance(attendanceList);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/student")
    public Page<AttendanceResponse> getStudentAttendances(Pageable pageable) {
        String studentEmail = authFacade.getUserEmail();

        return attendanceService.getStudentAttendances(studentEmail, pageable);
    }
}
