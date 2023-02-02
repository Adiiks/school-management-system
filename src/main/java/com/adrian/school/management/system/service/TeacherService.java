package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.TeacherConverter;
import com.adrian.school.management.system.dto.UserRequest;
import com.adrian.school.management.system.entity.Role;
import com.adrian.school.management.system.entity.Teacher;
import com.adrian.school.management.system.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    private final TeacherConverter teacherConverter;

    private final UserService userService;

    @Transactional
    public void createTeacher(UserRequest teacherRequest) {
        if (userService.isUserExistsWithEmail(teacherRequest.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User with email " + teacherRequest.email() + " already exists");
        }

        Teacher teacher = teacherConverter.convertUserRequestToTeacher(teacherRequest);

        teacher.setDateOfJoin(LocalDate.now());
        teacher.setRoles(Set.of(Role.ROLE_TEACHER));

        teacherRepository.save(teacher);
    }
}
