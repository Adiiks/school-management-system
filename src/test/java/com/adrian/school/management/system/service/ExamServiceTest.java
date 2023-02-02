package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.ExamConverter;
import com.adrian.school.management.system.dto.ExamRequest;
import com.adrian.school.management.system.entity.Exam;
import com.adrian.school.management.system.repository.ExamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExamServiceTest {

    ExamService examService;

    ExamConverter examConverter = new ExamConverter();

    @Mock
    ExamRepository examRepository;

    @Captor
    ArgumentCaptor<Exam> acExam;

    @BeforeEach
    void setUp() {
        examService = new ExamService(examRepository, examConverter);
    }

    @DisplayName("Create exam")
    @Test
    void createExam() {
        ExamRequest examRequest = buildExamRequest();

        examService.createExam(examRequest);

        verify(examRepository).save(acExam.capture());

        Exam exam = acExam.getValue();

        assertNotNull(exam);
    }

    private ExamRequest buildExamRequest() {
        return new ExamRequest(LocalDate.now(), "Anatomy of Humans");
    }
}