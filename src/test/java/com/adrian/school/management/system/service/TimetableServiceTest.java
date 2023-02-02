package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.TimetableConverter;
import com.adrian.school.management.system.dto.TimetableRequest;
import com.adrian.school.management.system.dto.TimetableResponse;
import com.adrian.school.management.system.entity.*;
import com.adrian.school.management.system.repository.ClassroomRepository;
import com.adrian.school.management.system.repository.CourseRepository;
import com.adrian.school.management.system.repository.StudentRepository;
import com.adrian.school.management.system.repository.TimetableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimetableServiceTest {

    TimetableService timetableService;

    @Mock
    CourseRepository courseRepository;

    @Mock
    TimetableRepository timetableRepository;

    @Mock
    ClassroomRepository classroomRepository;

    @Mock
    StudentRepository studentRepository;

    TimetableConverter timetableConverter = new TimetableConverter();

    @Captor
    ArgumentCaptor<Timetable> acTimetable;

    @BeforeEach
    void setUp() {
        timetableService = new TimetableService(timetableRepository, courseRepository, classroomRepository,
                studentRepository, timetableConverter);
    }

    @DisplayName("Create new timetable - Course not found")
    @Test
    void createNewTimetableCourseNotFound() {
        TimetableRequest timetableRequest = buildTimetableRequest();

        when(courseRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                timetableService.createNewTimetable(timetableRequest));
    }

    @DisplayName("Create new timetable - Classroom not found")
    @Test
    void createNewTimetableClassroomNotFound() {
        TimetableRequest timetableRequest = buildTimetableRequest();
        Course course = new Course();

        when(courseRepository.findById(anyInt())).thenReturn(Optional.of(course));

        when(classroomRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                timetableService.createNewTimetable(timetableRequest));
    }

    @DisplayName("Create new timetable")
    @Test
    void createNewTimetable() {
        TimetableRequest timetableRequest = buildTimetableRequest();
        Course course = new Course();
        Classroom classroom = new Classroom();
        List<Student> students = List.of(new Student());

        when(courseRepository.findById(anyInt())).thenReturn(Optional.of(course));

        when(classroomRepository.findById(anyInt())).thenReturn(Optional.of(classroom));

        when(studentRepository.findByIdIsIn(anyCollection())).thenReturn(students);

        timetableService.createNewTimetable(timetableRequest);

        verify(timetableRepository).save(acTimetable.capture());

        Timetable timetable = acTimetable.getValue();

        assertNotNull(timetable);
        assertEquals(classroom, timetable.getClassroom());
        assertEquals(course, timetable.getCourse());
        assertEquals(students, timetable.getStudents());
    }

    private TimetableRequest buildTimetableRequest() {
        return new TimetableRequest(Day.FRIDAY, LocalTime.of(9, 0), 1,
                1, List.of(1));
    }

    @DisplayName("Get weekly timetable for student")
    @Test
    void getWeeklyTimetableForStudent() {
        Timetable timetableDb1 = buildTimetable(1, LocalTime.of(8, 00), "CHEMIA",
                100, "Jan Kowalski", Day.FRIDAY);

        Timetable timetableDb2 = buildTimetable(2, LocalTime.of(9, 00), "CHEMIA",
                100, "Jan Kowalski", Day.MONDAY);

        Timetable timetableDb3 = buildTimetable(3, LocalTime.of(10, 00), "CHEMIA",
                100, "Jan Kowalski", Day.FRIDAY);

        Timetable timetableDb4 = buildTimetable(4, LocalTime.of(11, 00), "CHEMIA",
                100, "Jan Kowalski", Day.MONDAY);

        when(timetableRepository.findAllByStudentsEmailOrderByTime(anyString()))
                .thenReturn(List.of(timetableDb1, timetableDb2, timetableDb3, timetableDb4));

        Map<Day, List<TimetableResponse>> weeklyTimetable =
                timetableService.getWeeklyTimetableForStudent("email");

        assertNotNull(weeklyTimetable);

        assertEquals(Set.of(Day.values()), weeklyTimetable.keySet());

        assertEquals(timetableDb2.getTime(), weeklyTimetable.get(Day.MONDAY).get(0).time());
        assertEquals(timetableDb4.getTime(), weeklyTimetable.get(Day.MONDAY).get(1).time());

        assertEquals(timetableDb1.getTime(), weeklyTimetable.get(Day.FRIDAY).get(0).time());
        assertEquals(timetableDb3.getTime(), weeklyTimetable.get(Day.FRIDAY).get(1).time());
    }

    private Timetable buildTimetable(Integer id, LocalTime time, String courseName,
                                     Integer classroomNumber, String teacherName, Day day) {
        return Timetable.builder()
                .id(id)
                .time(time)
                .course(Course.builder().name(courseName).build())
                .classroom(Classroom.builder()
                        .number(classroomNumber)
                        .teacher(Teacher.builder().name(teacherName).build()).build())
                .day(day)
                .build();
    }
}