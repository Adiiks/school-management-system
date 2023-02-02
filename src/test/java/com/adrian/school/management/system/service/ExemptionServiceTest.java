package com.adrian.school.management.system.service;

import com.adrian.school.management.system.dto.ExemptionRequest;
import com.adrian.school.management.system.entity.Exemption;
import com.adrian.school.management.system.entity.Student;
import com.adrian.school.management.system.repository.ExemptionRepository;
import com.adrian.school.management.system.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExemptionServiceTest {

    ExemptionService exemptionService;

    @Mock
    ExemptionRepository exemptionRepository;

    @Mock
    StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        exemptionService = new ExemptionService(exemptionRepository, studentRepository);
    }

    @DisplayName("Create new exemption - Failed start date is after end date")
    @Test
    void createExemptionStartDateIsAfterEndDate() {
        ExemptionRequest exemptionRequest = new ExemptionRequest("some reason", LocalDate.now(),
                LocalDate.now().minusDays(1), 1);

        assertThrows(ResponseStatusException.class, () ->
                exemptionService.createExemption(exemptionRequest));
    }

    @DisplayName("Create new exemption - Student not found")
    @Test
    void createExemptionStudentNotFound() {
        ExemptionRequest exemptionRequest = new ExemptionRequest("some reason", LocalDate.now(),
                LocalDate.now().plusDays(1), 1);

        when(studentRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                exemptionService.createExemption(exemptionRequest));
    }

    @DisplayName("Create new exemption")
    @Test
    void createExemption() {
        ExemptionRequest exemptionRequest = new ExemptionRequest("some reason", LocalDate.now(),
                LocalDate.now().plusDays(1), 1);

        Student studentDb = new Student();

        when(studentRepository.findById(anyInt())).thenReturn(Optional.of(studentDb));

        exemptionService.createExemption(exemptionRequest);

        ArgumentCaptor<Exemption> acExemption = ArgumentCaptor.forClass(Exemption.class);

        verify(exemptionRepository).save(acExemption.capture());

        Exemption exemption = acExemption.getValue();

        assertNotNull(exemption);
        assertNotNull(exemption.getStudent());
    }
}