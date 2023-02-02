package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.auth.AuthenticationFacade;
import com.adrian.school.management.system.dto.AttendanceList;
import com.adrian.school.management.system.dto.AttendanceResponse;
import com.adrian.school.management.system.entity.AttendanceStatus;
import com.adrian.school.management.system.service.AttendanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AttendanceControllerTest {

    final String BASE_URL = "/api/attendances";

    MockMvc mockMvc;

    AttendanceController attendanceController;

    @Mock
    AttendanceService attendanceService;

    @Mock
    AuthenticationFacade authFacade;

    static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setObjectMapper() {
        objectMapper = objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() {
        attendanceController = new AttendanceController(attendanceService, authFacade);

        mockMvc = MockMvcBuilders.standaloneSetup(attendanceController)
                .setControllerAdvice(new ValidationExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @DisplayName("Check student list attendance - Validation failed")
    @Test
    void checkAttendanceValidationFailed() throws Exception {
        AttendanceList attendanceList = new AttendanceList(null, Map.of());

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(attendanceList)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @DisplayName("Check student list attendance")
    @Test
    void checkAttendance() throws Exception {
        AttendanceList attendanceList = new AttendanceList(LocalDate.now(), Map.of(1, true));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(attendanceList)))
                .andExpect(status().isCreated());
    }

    @DisplayName("Get student list of attendances")
    @Test
    void getStudentAttendances() throws Exception {
        AttendanceResponse attendanceResponse = new AttendanceResponse(1, LocalDate.now(),
                AttendanceStatus.PRESENT);

        Page<AttendanceResponse> attendanceResponsePage = new PageImpl<>(List.of(attendanceResponse),
                Pageable.unpaged(), 1);

        when(authFacade.getUserEmail()).thenReturn("email");

        when(attendanceService.getStudentAttendances(anyString(), any()))
                .thenReturn(attendanceResponsePage);

        mockMvc.perform(get(BASE_URL + "/student"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(attendanceResponsePage)));
    }
}