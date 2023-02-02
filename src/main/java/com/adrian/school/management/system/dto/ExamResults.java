package com.adrian.school.management.system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record ExamResults(
        @NotNull(message = "Id of exam is required")
        Integer examId,
        @NotNull(message = "Id of course is required")
        Integer courseId,
        @Size(min = 1, message = "Student id and his mark is required")
        Map<Integer, Integer> studentsMarks
) {
}
