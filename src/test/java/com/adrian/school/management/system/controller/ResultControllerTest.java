package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.auth.AuthenticationFacade;
import com.adrian.school.management.system.dto.ExamResponse;
import com.adrian.school.management.system.dto.ExamResults;
import com.adrian.school.management.system.dto.ResultReport;
import com.adrian.school.management.system.dto.ResultResponse;
import com.adrian.school.management.system.service.ResultService;
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
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ResultControllerTest {

    final String BASE_URL = "/api/results";

    MockMvc mockMvc;

    ResultController resultController;

    @Mock
    ResultService resultService;

    @Mock
    AuthenticationFacade authFacade;

    static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setObjectMapper() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() {
        resultController = new ResultController(resultService, authFacade);

        mockMvc = MockMvcBuilders.standaloneSetup(resultController)
                .setControllerAdvice(new ValidationExceptionHandler())
                .build();
    }

    @DisplayName("Get student's list of marks for course")
    @Test
    void getStudentResultsForCourse() throws Exception {
        ExamResponse examResponse = new ExamResponse(1, LocalDate.now(), "name");

        ResultResponse resultResponse = new ResultResponse(1, 2, examResponse);

        when(authFacade.getUserEmail()).thenReturn("email");

        when(resultService.getStudentResultsForCourse(anyString(), anyString()))
                .thenReturn(List.of(resultResponse));

        mockMvc.perform(get(BASE_URL + "/biologia"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(resultResponse))));
    }

    @DisplayName("Add marks - Validation failed")
    @Test
    void addMarksValidationFailed() throws Exception {
        ExamResults examResults = new ExamResults(null, null, Map.of());

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(examResults)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(3)));
    }

    @DisplayName("Add marks")
    @Test
    void addMarks() throws Exception {
        ExamResults examResults = buildExamResults();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(examResults)))
                .andExpect(status().isCreated());
    }

    private ExamResults buildExamResults() {
        return new ExamResults(1, 1, Map.of(1, 1));
    }

    @DisplayName("Generate report containing final marks of student")
    @Test
    void generateReport() throws Exception {
        List<ResultReport> report = List.of(new ResultReport("BIOLOGY", 4.00));

        when(authFacade.getUserEmail()).thenReturn("email");

        when(resultService.generateReport(anyString())).thenReturn(report);

        mockMvc.perform(get(BASE_URL + "/generate-report"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(report)));
    }
}