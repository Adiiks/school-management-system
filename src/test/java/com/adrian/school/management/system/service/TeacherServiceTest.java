package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.TeacherConverter;
import com.adrian.school.management.system.dto.UserRequest;
import com.adrian.school.management.system.entity.Role;
import com.adrian.school.management.system.entity.Sex;
import com.adrian.school.management.system.entity.Teacher;
import com.adrian.school.management.system.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    TeacherService teacherService;

    @Mock
    TeacherRepository teacherRepository;

    TeacherConverter teacherConverter = new TeacherConverter(new BCryptPasswordEncoder());

    @Mock
    UserService userService;

    @BeforeEach
    void setUp() {
        teacherService = new TeacherService(teacherRepository, teacherConverter, userService);
    }

    @DisplayName("Create new teacher - Email already in use")
    @Test
    void createTeacherEmailAlreadyInUse() {
        UserRequest userRequest = getUserRequest();

        when(userService.isUserExistsWithEmail(anyString())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> teacherService.createTeacher(userRequest));
    }

    @DisplayName("Create new teacher")
    @Test
    void createTeacher() {
        UserRequest userRequest = getUserRequest();

        when(userService.isUserExistsWithEmail(anyString())).thenReturn(false);

        teacherService.createTeacher(userRequest);

        ArgumentCaptor<Teacher> acTeacher = ArgumentCaptor.forClass(Teacher.class);

        verify(teacherRepository).save(acTeacher.capture());

        Teacher teacher = acTeacher.getValue();

        assertNotNull(teacher);
        assertNotNull(teacher.getDateOfJoin());
        assertEquals(Set.of(Role.ROLE_TEACHER), teacher.getRoles());
    }

    private UserRequest getUserRequest() {
        return new UserRequest("email@gmail.com", "pass", "name",
                LocalDate.now(), Sex.FEMALE, "address", "222333444");
    }
}