package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.dto.ExemptionRequest;
import com.adrian.school.management.system.service.ExemptionService;
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
class ExemptionControllerTest {

    final String BASE_URL = "/api/exemptions";

    MockMvc mockMvc;

    ExemptionController exemptionController;

    @Mock
    ExemptionService exemptionService;

    static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setObjectMapper() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() {
        exemptionController = new ExemptionController(exemptionService);

        mockMvc = MockMvcBuilders.standaloneSetup(exemptionController)
                .setControllerAdvice(new ValidationExceptionHandler())
                .build();
    }

    @DisplayName("Add new exemption - Validation failed")
    @Test
    void addExemptionValidationFailed() throws Exception {
        ExemptionRequest exemptionRequest = new ExemptionRequest("", null, null,
                null);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exemptionRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(4)));
    }

    @DisplayName("Add new exemption")
    @Test
    void addExemption() throws Exception {
        ExemptionRequest exemptionRequest = new ExemptionRequest("some reason", LocalDate.now(),
                LocalDate.now().plusDays(10), 1);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exemptionRequest)))
                .andExpect(status().isCreated());
    }
}