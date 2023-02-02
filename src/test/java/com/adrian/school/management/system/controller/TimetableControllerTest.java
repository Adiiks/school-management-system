package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.auth.AuthenticationFacade;
import com.adrian.school.management.system.dto.TimetableRequest;
import com.adrian.school.management.system.dto.TimetableResponse;
import com.adrian.school.management.system.entity.Day;
import com.adrian.school.management.system.service.TimetableService;
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

import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TimetableControllerTest {

    final String BASE_URL = "/api/timetables";

    MockMvc mockMvc;
    TimetableController timetableController;

    @Mock
    TimetableService timetableService;

    @Mock
    AuthenticationFacade authFacade;

    static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setObjectMapper() {
        objectMapper = objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() {
        timetableController = new TimetableController(timetableService, authFacade);

        mockMvc = MockMvcBuilders.standaloneSetup(timetableController)
                .setControllerAdvice(new ValidationExceptionHandler())
                .build();
    }

    @DisplayName("Create new timetable - Validation failed")
    @Test
    void createNewTimetableValidationFailed() throws Exception {
        TimetableRequest timetableRequest = new TimetableRequest(null, null, null,
                null, List.of());

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(timetableRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(5)));
    }

    @DisplayName("Create new timetable")
    @Test
    void createNewTimetable() throws Exception {
        TimetableRequest timetableRequest = buildTimetableRequest();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(timetableRequest)))
                .andExpect(status().isCreated());
    }

    private TimetableRequest buildTimetableRequest() {
        return new TimetableRequest(Day.FRIDAY, LocalTime.of(9, 0), 1,
                1, List.of(1));
    }

    @DisplayName("Get weekly timetable for student")
    @Test
    void getWeeklyTimetableForStudent() throws Exception {
        Map<Day, List<TimetableResponse>> weeklyTimetable = new EnumMap<>(Day.class);
        weeklyTimetable.put(Day.FRIDAY, List.of(getTimetableResponse()));

        when(authFacade.getUserEmail()).thenReturn("some email");

        when(timetableService.getWeeklyTimetableForStudent(anyString())).thenReturn(weeklyTimetable);

        mockMvc.perform(get(BASE_URL + "/weekly"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(weeklyTimetable)));
    }

    private TimetableResponse getTimetableResponse() {
        return new TimetableResponse(1, LocalTime.of(9, 00), "CHEMIA",
                112, "Jan Kowalski");
    }
}