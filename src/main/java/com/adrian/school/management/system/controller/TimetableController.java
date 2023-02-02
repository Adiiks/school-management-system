package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.auth.AuthenticationFacade;
import com.adrian.school.management.system.dto.TimetableRequest;
import com.adrian.school.management.system.dto.TimetableResponse;
import com.adrian.school.management.system.entity.Day;
import com.adrian.school.management.system.service.TimetableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/timetables")
@RequiredArgsConstructor
public class TimetableController {

    private final TimetableService timetableService;
    private final AuthenticationFacade authFacade;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createNewTimetable(@Valid @RequestBody TimetableRequest timetableRequest) {
        timetableService.createNewTimetable(timetableRequest);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/weekly")
    public Map<Day, List<TimetableResponse>> getWeeklyTimetableForStudent() {
        String studentEmail = authFacade.getUserEmail();

        return timetableService.getWeeklyTimetableForStudent(studentEmail);
    }
}
