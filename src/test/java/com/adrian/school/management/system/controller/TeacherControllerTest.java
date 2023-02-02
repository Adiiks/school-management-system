package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.dto.UserRequest;
import com.adrian.school.management.system.entity.Sex;
import com.adrian.school.management.system.service.TeacherService;
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
class TeacherControllerTest {

    final String BASE_URL = "/api/teachers";

    MockMvc mockMvc;

    TeacherController teacherController;

    @Mock
    TeacherService teacherService;

    static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setObjectMapper() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() {
        teacherController = new TeacherController(teacherService);

        mockMvc = MockMvcBuilders.standaloneSetup(teacherController)
                .setControllerAdvice(new ValidationExceptionHandler())
                .build();
    }

    @DisplayName("Add new teacher - Validation Failed")
    @Test
    void addTeacherValidationFailed() throws Exception {
        UserRequest invalidUserRequest = new UserRequest("invalid email", "", "",
                null, null, "", "");

        mockMvc.perform(post(BASE_URL + "/add-teacher")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUserRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(7)));
    }

    @DisplayName("Add new teacher")
    @Test
    void addTeacher() throws Exception {
        UserRequest userRequest = new UserRequest("email@gmail.com", "pass", "name",
                LocalDate.now(), Sex.FEMALE, "address", "312321321");

        mockMvc.perform(post(BASE_URL + "/add-teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated());
    }
}