package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.StudentConverter;
import com.adrian.school.management.system.dto.StudentRequest;
import com.adrian.school.management.system.entity.Role;
import com.adrian.school.management.system.entity.Student;
import com.adrian.school.management.system.entity.Teacher;
import com.adrian.school.management.system.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    private final StudentConverter studentConverter;

    private final UserService userService;

    @Transactional
    public void createStudent(StudentRequest studentRequest) {
        if (userService.isUserExistsWithEmail(studentRequest.user().email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User with email " + studentRequest.user().email() + " already exists");
        }

        Student student = studentConverter.convertStudentRequestToStudent(studentRequest);

        student.setDateOfJoin(LocalDate.now());
        student.setRoles(Set.of(Role.ROLE_STUDENT));

        studentRepository.save(student);
    }
}
