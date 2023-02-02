package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.CourseConverter;
import com.adrian.school.management.system.dto.CourseRequest;
import com.adrian.school.management.system.dto.CourseResponse;
import com.adrian.school.management.system.entity.Course;
import com.adrian.school.management.system.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseConverter courseConverter;

    @Transactional
    public void createCourse(CourseRequest courseRequest) {
        Course course = Course.builder()
                .name(courseRequest.name().toUpperCase())
                .description(courseRequest.description())
                .build();

        courseRepository.save(course);
    }

    @Transactional
    public void removeCourse(Integer courseId) {
        if (courseRepository.existsById(courseId)) {
            courseRepository.deleteById(courseId);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course with id " + courseId + " not found.");
        }
    }

    public CourseResponse getCourse(Integer courseId) {
        return courseRepository.findById(courseId)
                .map(courseConverter::convertCourseToCourseResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Course with id " + courseId + " not found."));
    }
}
