package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.StudentConverter;
import com.adrian.school.management.system.dto.StudentRequest;
import com.adrian.school.management.system.dto.UserRequest;
import com.adrian.school.management.system.entity.Role;
import com.adrian.school.management.system.entity.Sex;
import com.adrian.school.management.system.entity.Student;
import com.adrian.school.management.system.entity.Teacher;
import com.adrian.school.management.system.repository.StudentRepository;
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
class StudentServiceTest {

    StudentService studentService;

    @Mock
    StudentRepository studentRepository;

    StudentConverter studentConverter = new StudentConverter(new BCryptPasswordEncoder());

    @Mock
    UserService userService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository, studentConverter, userService);
    }

    @DisplayName("Create new student - Email already in use")
    @Test
    void createStudentEmailAlreadyInUse() {
        StudentRequest studentRequest = getStudentRequest();

        when(userService.isUserExistsWithEmail(anyString())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> studentService.createStudent(studentRequest));
    }

    @DisplayName("Create new student")
    @Test
    void createStudent() {
        StudentRequest studentRequest = getStudentRequest();

        when(userService.isUserExistsWithEmail(anyString())).thenReturn(false);

        studentService.createStudent(studentRequest);

        ArgumentCaptor<Student> acStudent = ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(acStudent.capture());

        Student student = acStudent.getValue();

        assertNotNull(student);
        assertNotNull(student.getDateOfJoin());
        assertEquals(Set.of(Role.ROLE_STUDENT), student.getRoles());
    }

    private StudentRequest getStudentRequest() {
        UserRequest user = new UserRequest("email@gmail.com", "pass", "name",
                LocalDate.now(), Sex.FEMALE, "address", "222333444");

        return new StudentRequest(user, "parent name");
    }
}