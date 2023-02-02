package com.adrian.school.management.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ExemptionRequest(
        @NotBlank(message = "Reason is required")
        String reason,
        @NotNull(message = "Start date is required")
        LocalDate startDate,
        @NotNull(message = "End date is required")
        LocalDate endDate,
        @NotNull(message = "Student id is required")
        Integer studentId
) {
}
