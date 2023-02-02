package com.adrian.school.management.system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Map;

public record AttendanceList(
        @NotNull(message = "Date is required")
        LocalDate date,
        @NotNull(message = "Student's list is required")
        @Size(min = 1, message = "Student's list hast to have at least one student")
        Map<Integer, Boolean> studentList
) {
}
