package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.AttendanceConverter;
import com.adrian.school.management.system.dto.AttendanceList;
import com.adrian.school.management.system.dto.AttendanceResponse;
import com.adrian.school.management.system.entity.Attendance;
import com.adrian.school.management.system.entity.AttendanceStatus;
import com.adrian.school.management.system.entity.Student;
import com.adrian.school.management.system.repository.AttendanceRepository;
import com.adrian.school.management.system.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    AttendanceService attendanceService;

    @Mock
    AttendanceRepository attendanceRepository;

    @Mock
    StudentRepository studentRepository;

    @Mock
    ExemptionService exemptionService;

    @Captor
    ArgumentCaptor<List<Attendance>> acAttendanceList;

    AttendanceConverter attendanceConverter;

    @BeforeEach
    void setUp() {
        attendanceConverter = new AttendanceConverter();

        attendanceService = new AttendanceService(attendanceRepository, studentRepository,
                exemptionService, attendanceConverter);
    }

    @DisplayName("Check students list of attendances")
    @Test
    void checkAttendance() {
        AttendanceList attendanceList = buildAttendanceList();

        List<Student> students = buildStudentList();

        when(studentRepository.findByIdIsIn(anyCollection())).thenReturn(students);

        when(exemptionService.isStudentExcused(any(), any()))
                .thenReturn(true)
                .thenReturn(false);

        attendanceService.checkAttendance(attendanceList);

        verify(attendanceRepository).saveAll(acAttendanceList.capture());

        List<Attendance> attendances = acAttendanceList.getValue();

        assertEquals(3, attendances.size());

        assertEquals(AttendanceStatus.PRESENT, attendances.get(0).getStatus());
        assertEquals(AttendanceStatus.EXCUSED, attendances.get(1).getStatus());
        assertEquals(AttendanceStatus.ABSENT, attendances.get(2).getStatus());
    }

    private List<Student> buildStudentList() {
        Student student1 = Student.builder().id(1).build();
        Student student2 = Student.builder().id(2).build();
        Student student3 = Student.builder().id(3).build();

        return List.of(student1, student2, student3);
    }

    private AttendanceList buildAttendanceList() {
        Map<Integer, Boolean> studentList = new HashMap<>();
        studentList.put(1, true);
        studentList.put(2, false);
        studentList.put(3, false);

        return new AttendanceList(LocalDate.of(2023, 1, 24),
                studentList);
    }

    @DisplayName("Get student attendances list")
    @Test
    void getStudentAttendances() {
        Attendance attendance = buildAttendance();

        Page<Attendance> attendancePage = new PageImpl<>(List.of(attendance), Pageable.unpaged(), 1);

        when(attendanceRepository.findAllByStudentEmail(anyString(), any())).thenReturn(attendancePage);

        Page<AttendanceResponse> attendanceResponsePage =
                attendanceService.getStudentAttendances("email", Pageable.unpaged());

        assertNotNull(attendanceResponsePage);
        assertEquals(1, attendanceResponsePage.getContent().size());
    }

    private Attendance buildAttendance() {
        return Attendance.builder()
                .date(LocalDate.of(2023, 1, 25))
                .id(1)
                .status(AttendanceStatus.PRESENT)
                .build();
    }
}