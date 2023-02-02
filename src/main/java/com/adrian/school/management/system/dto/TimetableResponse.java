package com.adrian.school.management.system.dto;

import java.time.LocalTime;

public record TimetableResponse(
        Integer id,
        LocalTime time,
        String courseName,
        Integer classroomNumber,
        String teacherName
) {
}
