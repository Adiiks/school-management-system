package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.dto.ClassroomRequest;
import com.adrian.school.management.system.dto.ClassroomResponse;
import com.adrian.school.management.system.service.ClassroomService;
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
class ClassroomControllerTest {

    final String BASE_URL = "/api/classrooms";

    MockMvc mockMvc;
    ClassroomController classroomController;

    @Mock
    ClassroomService classroomService;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        classroomController = new ClassroomController(classroomService);

        mockMvc = MockMvcBuilders.standaloneSetup(classroomController)
                .setControllerAdvice(new ValidationExceptionHandler())
                .build();
    }

    @DisplayName("Create new classroom - Validation failed")
    @Test
    void createClassroomValidationFailed() throws Exception {
        ClassroomRequest classroomRequest = new ClassroomRequest(null, null);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(classroomRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @DisplayName("Create new classroom")
    @Test
    void createClassroom() throws Exception {
        ClassroomRequest classroomRequest = new ClassroomRequest(100, 1);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(classroomRequest)))
                .andExpect(status().isCreated());
    }

    @DisplayName("Get classroom by id")
    @Test
    void getClassroom() throws Exception {
        ClassroomResponse classroomResponse = buildClassroomResponse();

        when(classroomService.getClassroom(anyInt())).thenReturn(classroomResponse);

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(classroomResponse)));
    }

    private ClassroomResponse buildClassroomResponse() {
        return new ClassroomResponse(1, 1, "Jan Kowalski");
    }
}