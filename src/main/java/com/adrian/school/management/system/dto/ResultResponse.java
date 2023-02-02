package com.adrian.school.management.system.dto;

public record ResultResponse(
        Integer id,
        Integer mark,
        ExamResponse exam
) {
}
