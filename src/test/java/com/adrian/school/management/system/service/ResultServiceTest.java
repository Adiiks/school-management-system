package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.ExamConverter;
import com.adrian.school.management.system.converter.ResultConverter;
import com.adrian.school.management.system.dto.ExamResults;
import com.adrian.school.management.system.dto.ResultResponse;
import com.adrian.school.management.system.entity.Course;
import com.adrian.school.management.system.entity.Exam;
import com.adrian.school.management.system.entity.Result;
import com.adrian.school.management.system.entity.Student;
import com.adrian.school.management.system.repository.CourseRepository;
import com.adrian.school.management.system.repository.ExamRepository;
import com.adrian.school.management.system.repository.ResultRepository;
import com.adrian.school.management.system.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResultServiceTest {

    ResultService resultService;

    @Mock
    ResultRepository resultRepository;

    @Mock
    ExamRepository examRepository;

    @Mock
    CourseRepository courseRepository;

    @Mock
    StudentRepository studentRepository;

    ResultConverter resultConverter = new ResultConverter(new ExamConverter());

    @Captor
    ArgumentCaptor<List<Result>> acResults;

    @BeforeEach
    void setUp() {
        resultService = new ResultService(resultRepository, resultConverter, examRepository,
                courseRepository, studentRepository);
    }

    @DisplayName("Get student's list of marks for course")
    @Test
    void getStudentResultsForCourse() {
        String courseName = "biologia";

        Exam examDb = getExam();

        Result resultDb = getResult(examDb);

        when(resultRepository.findAllByStudentEmailAndCourseName(anyString(), anyString()))
                .thenReturn(List.of(resultDb));

        List<ResultResponse> resultResponseList =
                resultService.getStudentResultsForCourse("email", courseName);

        assertEquals(1, resultResponseList.size());

        ArgumentCaptor<String> acCourseName = ArgumentCaptor.forClass(String.class);

        verify(resultRepository).findAllByStudentEmailAndCourseName(anyString(), acCourseName.capture());

        String courseNameUpperCase = acCourseName.getValue();

        assertEquals(courseName.toUpperCase(), courseNameUpperCase);
    }

    private Result getResult(Exam examDb) {
        return Result.builder()
                .id(1)
                .exam(examDb)
                .mark(1)
                .build();
    }

    private Exam getExam() {
        return Exam.builder()
                .date(LocalDate.now())
                .id(1)
                .name("exam name")
                .build();
    }

    @DisplayName("Add marks - Exam not found")
    @Test
    void addMarksExamNotFound() {
        ExamResults examResults = buildExamResults();

        when(examRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> resultService.addMarks(examResults));
    }

    @DisplayName("Add marks - Course not found")
    @Test
    void addMarksCourseNotFound() {
        ExamResults examResults = buildExamResults();
        Exam examDb = getExam();

        when(examRepository.findById(anyInt())).thenReturn(Optional.of(examDb));

        when(courseRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> resultService.addMarks(examResults));
    }

    @DisplayName("Add marks - Invalid mark")
    @Test
    void addMarksInvalidMark() {
        ExamResults examResults = new ExamResults(1, 1, Map.of(1,0));
        Exam examDb = getExam();
        Course courseDb = new Course();
        Student studentDb = Student.builder().id(1).build();
        List<Student> studentsDb = List.of(studentDb);

        when(examRepository.findById(anyInt())).thenReturn(Optional.of(examDb));

        when(courseRepository.findById(anyInt())).thenReturn(Optional.of(courseDb));

        when(studentRepository.findByIdIsIn(anyCollection())).thenReturn(studentsDb);

        assertThrows(ResponseStatusException.class, () -> resultService.addMarks(examResults));
    }

    @DisplayName("Add marks")
    @Test
    void addMarks() {
        ExamResults examResults = buildExamResults();
        Exam examDb = getExam();
        Course courseDb = new Course();
        Student studentDb = Student.builder().id(1).build();
        List<Student> studentsDb = List.of(studentDb);

        when(examRepository.findById(anyInt())).thenReturn(Optional.of(examDb));

        when(courseRepository.findById(anyInt())).thenReturn(Optional.of(courseDb));

        when(studentRepository.findByIdIsIn(anyCollection())).thenReturn(studentsDb);

        resultService.addMarks(examResults);

        verify(resultRepository).saveAll(acResults.capture());

        List<Result> results = acResults.getValue();

        assertNotNull(results);
        assertEquals(1, results.size());

        Result result = results.get(0);

        assertEquals(1, result.getMark());
        assertEquals(examDb, result.getExam());
        assertEquals(studentDb, result.getStudent());
        assertEquals(courseDb, result.getCourse());
    }

    private ExamResults buildExamResults() {
        return new ExamResults(1, 1, Map.of(1,1));
    }
}