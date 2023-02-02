package com.adrian.school.management.system.dto;

import jakarta.validation.constraints.NotNull;

public record ClassroomRequest(
        @NotNull(message = "Number of classroom is required")
        Integer number,
        @NotNull(message = "Id of teacher is required")
        Integer teacherId
) {
}
