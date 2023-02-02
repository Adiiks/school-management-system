package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.ResultConverter;
import com.adrian.school.management.system.dto.ExamResults;
import com.adrian.school.management.system.dto.ResultReport;
import com.adrian.school.management.system.dto.ResultResponse;
import com.adrian.school.management.system.entity.Course;
import com.adrian.school.management.system.entity.Exam;
import com.adrian.school.management.system.entity.Result;
import com.adrian.school.management.system.entity.Student;
import com.adrian.school.management.system.repository.CourseRepository;
import com.adrian.school.management.system.repository.ExamRepository;
import com.adrian.school.management.system.repository.ResultRepository;
import com.adrian.school.management.system.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultRepository resultRepository;
    private final ResultConverter resultConverter;
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public List<ResultResponse> getStudentResultsForCourse(String userEmail, String courseName) {
        return resultRepository.findAllByStudentEmailAndCourseName(userEmail, courseName.toUpperCase())
                .stream()
                .map(resultConverter::convertResultToResultResponse)
                .toList();
    }

    @Transactional
    public void addMarks(ExamResults examResults) {
        Exam exam = findExam(examResults.examId());

        Course course = findCourse(examResults.courseId());

        List<Student> students = findStudents(examResults.studentsMarks().keySet());

        List<Result> results = students.stream()
                .map(student -> buildResult(examResults, exam, course, student))
                .toList();

        resultRepository.saveAll(results);
    }

    private Result buildResult(ExamResults examResults, Exam exam, Course course, Student student) {
        Integer mark = examResults.studentsMarks().get(student.getId());

        if (mark < 1 || mark > 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid mark: " + mark);
        }

        return Result.builder()
                .exam(exam)
                .course(course)
                .student(student)
                .mark(examResults.studentsMarks().get(student.getId()))
                .build();
    }

    private List<Student> findStudents(Set<Integer> keySet) {
        return studentRepository.findByIdIsIn(keySet);
    }

    private Course findCourse(Integer courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Course with id " + courseId + " not found."));
    }

    private Exam findExam(Integer examId) {
        return examRepository.findById(examId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Exam with id " + examId + " not found."));
    }

    public List<ResultReport> generateReport(String studentEmail) {
        return resultRepository.getFinalMarksForStudent(studentEmail);
    }
}
