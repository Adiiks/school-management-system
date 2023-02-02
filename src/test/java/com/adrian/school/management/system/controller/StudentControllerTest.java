package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.dto.StudentRequest;
import com.adrian.school.management.system.dto.UserRequest;
import com.adrian.school.management.system.entity.Sex;
import com.adrian.school.management.system.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
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

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    final String BASE_URL = "/api/students";

    MockMvc mockMvc;

    StudentController studentController;

    @Mock
    StudentService studentService;

    static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setObjectMapper() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() {
        studentController = new StudentController(studentService);

        mockMvc = MockMvcBuilders.standaloneSetup(studentController)
                .setControllerAdvice(new ValidationExceptionHandler())
                .build();
    }

    @DisplayName("Add new student - Validation Failed")
    @Test
    void addStudentValidationFailed() throws Exception {
        StudentRequest studentRequest = new StudentRequest(null, "");

        mockMvc.perform(post(BASE_URL + "/add-student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @DisplayName("Add new student")
    @Test
    void addStudent() throws Exception {
        StudentRequest studentRequest = new StudentRequest(getUserRequest(), "parent name");

        mockMvc.perform(post(BASE_URL + "/add-student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isCreated());
    }

    private UserRequest getUserRequest() {
        return new UserRequest("email@gmail.com", "pass", "name", LocalDate.now(), Sex.FEMALE,
                "some address", "111222333");
    }
}