package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.CourseConverter;
import com.adrian.school.management.system.dto.CourseRequest;
import com.adrian.school.management.system.dto.CourseResponse;
import com.adrian.school.management.system.entity.Course;
import com.adrian.school.management.system.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    CourseService courseService;

    @Mock
    CourseRepository courseRepository;

    CourseConverter courseConverter = new CourseConverter();

    @BeforeEach
    void setUp() {
        courseService = new CourseService(courseRepository, courseConverter);
    }

    @DisplayName("Create new course")
    @Test
    void createCourse() {
        String courseName = "chemia";
        CourseRequest courseRequest = new CourseRequest(courseName, "some description...");

        ArgumentCaptor<Course> acCourse = ArgumentCaptor.forClass(Course.class);

        courseService.createCourse(courseRequest);

        verify(courseRepository).save(acCourse.capture());

        Course course = acCourse.getValue();

        assertNotNull(course);
        assertEquals(courseName.toUpperCase(), course.getName());
    }

    @DisplayName("Remove course")
    @Test
    void removeCourse() {
        when(courseRepository.existsById(anyInt())).thenReturn(true);

        courseService.removeCourse(1);

        verify(courseRepository).deleteById(anyInt());
    }

    @DisplayName("Remove course - Course not found")
    @Test
    void removeCourseNotFound() {
        when(courseRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> courseService.removeCourse(1));
    }

    @DisplayName("Get course by id - not found")
    @Test
    void getCourseNotFound() {
        when(courseRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> courseService.getCourse(1));
    }

    @DisplayName("Get course by id")
    @Test
    void getCourse() {
        Course course = buildCourse();

        when(courseRepository.findById(anyInt())).thenReturn(Optional.of(course));

        CourseResponse courseResponse = courseService.getCourse(1);

        assertNotNull(courseResponse);
    }

    private Course buildCourse() {
        return Course.builder().id(1).name("BIOLOGY").description("some description").build();
    }
}