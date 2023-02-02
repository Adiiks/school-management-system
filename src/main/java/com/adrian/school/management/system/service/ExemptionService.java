package com.adrian.school.management.system.service;

import com.adrian.school.management.system.dto.ExemptionRequest;
import com.adrian.school.management.system.entity.Exemption;
import com.adrian.school.management.system.entity.Student;
import com.adrian.school.management.system.repository.ExemptionRepository;
import com.adrian.school.management.system.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExemptionService {

    private final ExemptionRepository exemptionRepository;

    private final StudentRepository studentRepository;

    @Transactional
    public void createExemption(ExemptionRequest exemptionRequest) {
        if (exemptionRequest.startDate().isAfter(exemptionRequest.endDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date cannot be after end date");
        }

        Student student = studentRepository.findById(exemptionRequest.studentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student with id " + exemptionRequest.studentId() + " not found."));

        Exemption exemption = Exemption.builder()
                .endDate(exemptionRequest.endDate())
                .startDate(exemptionRequest.startDate())
                .student(student)
                .reason(exemptionRequest.reason())
                .build();

        exemptionRepository.save(exemption);
    }

    public boolean isStudentExcused(Student student, LocalDate date) {
        return exemptionRepository.isExemptionExistsForStudentAtDate(student, date);
    }
}
