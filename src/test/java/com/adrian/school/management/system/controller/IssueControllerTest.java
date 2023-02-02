package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.auth.AuthenticationFacade;
import com.adrian.school.management.system.dto.IssueRequest;
import com.adrian.school.management.system.entity.Student;
import com.adrian.school.management.system.service.IssueService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class IssueControllerTest {

    final String BASE_URL = "/api/issues";

    MockMvc mockMvc;
    IssueController issueController;

    @Mock
    AuthenticationFacade authFacade;

    @Mock
    IssueService issueService;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        issueController = new IssueController(authFacade, issueService);

        mockMvc = MockMvcBuilders.standaloneSetup(issueController)
                .setControllerAdvice(new ValidationExceptionHandler())
                .build();
    }

    @DisplayName("Raise issue - Validation failed")
    @Test
    void raiseIssueValidationFailed() throws Exception {
        IssueRequest issueRequest = new IssueRequest("", "");

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(issueRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @DisplayName("Raise issue")
    @Test
    void raiseIssue() throws Exception {
        IssueRequest issueRequest = buildIssueRequest();

        when(authFacade.getStudent()).thenReturn(new Student());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issueRequest)))
                .andExpect(status().isCreated());
    }

    private IssueRequest buildIssueRequest() {
        return new IssueRequest("Stipend", "some details");
    }

    @DisplayName("Resolve issue")
    @Test
    void resolveIssue() throws Exception {
        mockMvc.perform(patch(BASE_URL + "/resolve/1"))
                .andExpect(status().isNoContent());
    }
}