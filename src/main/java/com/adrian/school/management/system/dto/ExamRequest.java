package com.adrian.school.management.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ExamRequest(
        @NotNull(message = "Date of exam is required")
        LocalDate date,
        @NotBlank(message = "Name is required")
        String name
) {
}
