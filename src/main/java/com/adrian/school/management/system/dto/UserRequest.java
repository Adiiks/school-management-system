package com.adrian.school.management.system.dto;

import com.adrian.school.management.system.entity.Sex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email address")
        String email,
        @NotBlank(message = "Password is required")
        String password,
        @NotBlank(message = "Name is required")
        String name,
        @NotNull(message = "Date of birth is required")
        LocalDate dateOfBirth,
        @NotNull(message = "Sex is required")
        Sex sex,
        @NotBlank(message = "Address is required")
        String address,
        @NotBlank(message = "Phone is required")
        String phone
) {
}
