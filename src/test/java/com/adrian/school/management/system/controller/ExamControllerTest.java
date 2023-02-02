package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.dto.ExamRequest;
import com.adrian.school.management.system.service.ExamService;
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
class ExamControllerTest {

    final String BASE_URL = "/api/exams";

    MockMvc mockMvc;

    ExamController examController;

    @Mock
    ExamService examService;

    static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setObjectMapper() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() {
        examController = new ExamController(examService);

        mockMvc = MockMvcBuilders.standaloneSetup(examController)
                .setControllerAdvice(new ValidationExceptionHandler())
                .build();
    }

    @DisplayName("Create new exam - Validation failed")
    @Test
    void createExamValidationFailed() throws Exception {
        ExamRequest examRequest = new ExamRequest(null, "");

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(examRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @DisplayName("Create new exam")
    @Test
    void createExam() throws Exception {
        ExamRequest examRequest = buildExamRequest();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(examRequest)))
                .andExpect(status().isCreated());
    }

    private ExamRequest buildExamRequest() {
        return new ExamRequest(LocalDate.now(), "Anatomy of Humans");
    }
}