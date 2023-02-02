package com.adrian.school.management.system.dto;

import jakarta.validation.constraints.NotBlank;

public record CourseRequest(
        @NotBlank(message = "Course name is required")
        String name,
        @NotBlank(message = "Course description is required")
        String description
) {
}
