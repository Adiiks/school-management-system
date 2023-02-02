package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.ExamConverter;
import com.adrian.school.management.system.dto.ExamRequest;
import com.adrian.school.management.system.entity.Exam;
import com.adrian.school.management.system.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final ExamConverter examConverter;

    @Transactional
    public void createExam(ExamRequest examRequest) {
        Exam exam = examConverter.convertExamRequestToExam(examRequest);

        examRepository.save(exam);
    }
}
