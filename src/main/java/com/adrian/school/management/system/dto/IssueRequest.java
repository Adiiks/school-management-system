package com.adrian.school.management.system.dto;

import jakarta.validation.constraints.NotBlank;

public record IssueRequest(
        @NotBlank(message = "You have to pass type of issue")
        String type,
        @NotBlank(message = "You have to pass some details")
        String details
) {
}
