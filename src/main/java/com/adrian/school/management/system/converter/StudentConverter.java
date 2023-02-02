package com.adrian.school.management.system.converter;

import com.adrian.school.management.system.dto.StudentRequest;
import com.adrian.school.management.system.dto.UserRequest;
import com.adrian.school.management.system.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentConverter {

    private final PasswordEncoder passwordEncoder;

    public Student convertStudentRequestToStudent(StudentRequest studentRequest) {
        UserRequest user = studentRequest.user();

        return Student.builder()
                .parentName(studentRequest.parentName())
                .address(user.address())
                .name(user.name())
                .password(passwordEncoder.encode(user.password()))
                .dateOfBirth(user.dateOfBirth())
                .phone(user.phone())
                .sex(user.sex())
                .email(user.email())
                .build();
    }
}
