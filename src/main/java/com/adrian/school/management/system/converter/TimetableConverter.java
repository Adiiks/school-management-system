package com.adrian.school.management.system.converter;

import com.adrian.school.management.system.dto.TimetableResponse;
import com.adrian.school.management.system.entity.Timetable;
import org.springframework.stereotype.Component;

@Component
public class TimetableConverter {

    public TimetableResponse convertTimetableToTimetableResponse(Timetable timetable) {
        return new TimetableResponse(timetable.getId(), timetable.getTime(), timetable.getCourse().getName(),
                timetable.getClassroom().getNumber(), timetable.getClassroom().getTeacher().getName());
    }
}
