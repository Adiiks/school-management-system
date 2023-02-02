package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.dto.CourseRequest;
import com.adrian.school.management.system.dto.CourseResponse;
import com.adrian.school.management.system.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    final String BASE_URL = "/api/courses";

    MockMvc mockMvc;

    CourseController courseController;

    @Mock
    CourseService courseService;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        courseController = new CourseController(courseService);

        mockMvc = MockMvcBuilders.standaloneSetup(courseController)
                .setControllerAdvice(new ValidationExceptionHandler())
                .build();
    }

    @DisplayName("Add new course - Validation failed")
    @Test
    void addCourseValidationFailed() throws Exception {
        CourseRequest courseRequest = new CourseRequest("", "");

        mockMvc.perform(post(BASE_URL + "/add-course")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courseRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @DisplayName("Add new course")
    @Test
    void addCourse() throws Exception {
        CourseRequest courseRequest = new CourseRequest("CHEMIA", "some description...");

        mockMvc.perform(post(BASE_URL + "/add-course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseRequest)))
                .andExpect(status().isCreated());
    }

    @DisplayName("Remove course")
    @Test
    void removeCourse() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/remove-course/1"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Get course by id")
    @Test
    void getCourse() throws Exception {
        CourseResponse courseResponse = buildCourseResponse();

        when(courseService.getCourse(anyInt())).thenReturn(courseResponse);

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(courseResponse)));
    }

    private CourseResponse buildCourseResponse() {
        return new CourseResponse(1, "BIOLOGY",
                "Science about nature and other staff ...");
    }
}