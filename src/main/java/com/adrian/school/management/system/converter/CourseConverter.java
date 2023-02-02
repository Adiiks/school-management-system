package com.adrian.school.management.system.converter;

import com.adrian.school.management.system.dto.CourseResponse;
import com.adrian.school.management.system.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseConverter {

    public CourseResponse convertCourseToCourseResponse(Course course) {
        return new CourseResponse(course.getId(), course.getName(), course.getDescription());
    }
}
