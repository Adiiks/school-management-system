package com.adrian.school.management.system.dto;

public record JwtRequest(
        String email,
        String password
) {
}
