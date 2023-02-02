package com.adrian.school.management.system.dto;

import java.time.LocalDate;

public record ExamResponse(
        Integer id,
        LocalDate date,
        String name
) {
}
