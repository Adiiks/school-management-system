package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.ClassroomConverter;
import com.adrian.school.management.system.dto.ClassroomRequest;
import com.adrian.school.management.system.dto.ClassroomResponse;
import com.adrian.school.management.system.entity.Classroom;
import com.adrian.school.management.system.entity.Teacher;
import com.adrian.school.management.system.repository.ClassroomRepository;
import com.adrian.school.management.system.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassroomServiceTest {

    ClassroomService classroomService;

    ClassroomConverter classroomConverter = new ClassroomConverter();

    @Mock
    ClassroomRepository classroomRepository;

    @Mock
    TeacherRepository teacherRepository;

    @Captor
    ArgumentCaptor<Classroom> acClassroom;

    @BeforeEach
    void setUp() {
        classroomService = new ClassroomService(classroomRepository, teacherRepository,
                classroomConverter);
    }

    @DisplayName("Create new classroom - Teacher not found")
    @Test
    void createClassroomTeacherNotFound() {
        ClassroomRequest classroomRequest = new ClassroomRequest(100, 1);

        when(classroomRepository.existsByNumber(anyInt())).thenReturn(false);

        when(teacherRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                classroomService.createClassroom(classroomRequest));
    }

    @DisplayName("Create new classroom")
    @Test
    void createClassroom() {
        ClassroomRequest classroomRequest = new ClassroomRequest(100, 1);
        Teacher teacherDb = new Teacher();

        when(classroomRepository.existsByNumber(anyInt())).thenReturn(false);

        when(teacherRepository.findById(anyInt())).thenReturn(Optional.of(teacherDb));

        classroomService.createClassroom(classroomRequest);

        verify(classroomRepository).save(acClassroom.capture());

        Classroom classroom = acClassroom.getValue();

        assertNotNull(classroom);
        assertEquals(teacherDb, classroom.getTeacher());
    }

    @DisplayName("Create new classroom - Number isn't unique")
    @Test
    void createClassroomNumberNotUnique() {
        ClassroomRequest classroomRequest = new ClassroomRequest(100, 1);

        when(classroomRepository.existsByNumber(anyInt())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () ->
                classroomService.createClassroom(classroomRequest));
    }

    @DisplayName("Get classroom by id - not found")
    @Test
    void getClassroomNotFound() {
        when(classroomRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> classroomService.getClassroom(1));
    }

    @DisplayName("Get classroom by id")
    @Test
    void getClassroom() {
        Classroom classroomDb = buildClassroom();

        when(classroomRepository.findById(anyInt())).thenReturn(Optional.of(classroomDb));

        ClassroomResponse classroomResponse = classroomService.getClassroom(1);

        assertNotNull(classroomResponse);
    }

    private Classroom buildClassroom() {
        return Classroom.builder()
                .id(1)
                .number(100)
                .teacher(Teacher.builder().name("Jan Kowalski").build())
                .build();
    }
}