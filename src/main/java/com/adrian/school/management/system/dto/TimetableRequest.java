package com.adrian.school.management.system.dto;

import com.adrian.school.management.system.entity.Day;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;
import java.util.List;

public record TimetableRequest(
        @NotNull(message = "Day is required from Monday - Friday")
        Day day,
        @NotNull(message = "Time is required")
        LocalTime time,
        @NotNull(message = "Course id is required")
        Integer courseId,
        @NotNull(message = "Classroom id is required")
        Integer classroomId,
        @Size(min = 1, message = "You have to add at least one student")
        List<Integer> studentIds
) {
}
