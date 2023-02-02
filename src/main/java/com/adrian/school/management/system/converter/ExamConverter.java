package com.adrian.school.management.system.converter;

import com.adrian.school.management.system.dto.ExamRequest;
import com.adrian.school.management.system.dto.ExamResponse;
import com.adrian.school.management.system.entity.Exam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExamConverter {

    public ExamResponse convertExamToExamResponse(Exam exam) {
        return new ExamResponse(exam.getId(), exam.getDate(), exam.getName());
    }

    public Exam convertExamRequestToExam(ExamRequest examRequest) {
        return Exam.builder()
                .date(examRequest.date())
                .name(examRequest.name())
                .build();
    }
}
