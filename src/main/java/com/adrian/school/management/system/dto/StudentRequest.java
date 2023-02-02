package com.adrian.school.management.system.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentRequest(
        @Valid
        @NotNull(message = "User's information is required")
        UserRequest user,
        @NotBlank(message = "Parent name is required")
        String parentName
) {
}
