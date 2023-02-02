package com.adrian.school.management.system.converter;

import com.adrian.school.management.system.dto.AttendanceResponse;
import com.adrian.school.management.system.entity.Attendance;
import org.springframework.stereotype.Component;

@Component
public class AttendanceConverter {

    public AttendanceResponse convertAttendanceToAttendanceResponse(Attendance attendance) {
        return new AttendanceResponse(attendance.getId(), attendance.getDate(), attendance.getStatus());
    }
}
