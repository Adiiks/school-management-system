package com.adrian.school.management.system.converter;

import com.adrian.school.management.system.dto.ClassroomResponse;
import com.adrian.school.management.system.entity.Classroom;
import org.springframework.stereotype.Component;

@Component
public class ClassroomConverter {

    public ClassroomResponse convertClassroomToClassroomResponse(Classroom classroom) {
        return new ClassroomResponse(classroom.getId(), classroom.getNumber(), classroom.getTeacher().getName());
    }
}
