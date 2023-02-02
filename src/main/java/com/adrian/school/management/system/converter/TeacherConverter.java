package com.adrian.school.management.system.converter;

import com.adrian.school.management.system.dto.UserRequest;
import com.adrian.school.management.system.entity.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeacherConverter {

    private final PasswordEncoder passwordEncoder;

    public Teacher convertUserRequestToTeacher(UserRequest teacherRequest) {
        return Teacher.builder()
                .email(teacherRequest.email())
                .password(passwordEncoder.encode(teacherRequest.password()))
                .name(teacherRequest.name())
                .dateOfBirth(teacherRequest.dateOfBirth())
                .sex(teacherRequest.sex())
                .address(teacherRequest.address())
                .phone(teacherRequest.phone())
                .build();
    }
}
