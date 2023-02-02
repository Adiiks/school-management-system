package com.adrian.school.management.system.dto;

import com.adrian.school.management.system.entity.AttendanceStatus;

import java.time.LocalDate;

public record AttendanceResponse(
        Integer id,
        LocalDate date,
        AttendanceStatus status
) {
}
