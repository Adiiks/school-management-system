package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.TimetableConverter;
import com.adrian.school.management.system.dto.TimetableRequest;
import com.adrian.school.management.system.dto.TimetableResponse;
import com.adrian.school.management.system.entity.*;
import com.adrian.school.management.system.repository.ClassroomRepository;
import com.adrian.school.management.system.repository.CourseRepository;
import com.adrian.school.management.system.repository.StudentRepository;
import com.adrian.school.management.system.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final CourseRepository courseRepository;
    private final ClassroomRepository classroomRepository;
    private final StudentRepository studentRepository;
    private final TimetableConverter timetableConverter;

    @Transactional
    public void createNewTimetable(TimetableRequest timetableRequest) {
        Course course = findCourse(timetableRequest.courseId());

        Classroom classroom = findClassroom(timetableRequest.classroomId());

        List<Student> students = findStudents(timetableRequest.studentIds());

        Timetable timetable = Timetable.builder()
                .classroom(classroom)
                .time(timetableRequest.time())
                .course(course)
                .day(timetableRequest.day())
                .students(students)
                .build();

        timetableRepository.save(timetable);
    }

    private List<Student> findStudents(List<Integer> studentIds) {
        return studentRepository.findByIdIsIn(studentIds);
    }

    private Classroom findClassroom(Integer classroomId) {
        return classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Classroom with id " + classroomId + " not found."));
    }

    private Course findCourse(Integer courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Course with id " + courseId + " not found."));
    }

    public Map<Day, List<TimetableResponse>> getWeeklyTimetableForStudent(String studentEmail) {
        List<Timetable> timetableList = timetableRepository.findAllByStudentsEmailOrderByTime(studentEmail);

        Map<Day, List<TimetableResponse>> weeklyTimetable = fillWeeklyTimetableWithDays();

        for (Timetable timetable : timetableList) {
            TimetableResponse timetableResponse = timetableConverter.convertTimetableToTimetableResponse(timetable);

            weeklyTimetable.get(timetable.getDay()).add(timetableResponse);
        }

        return weeklyTimetable;
    }

    private Map<Day, List<TimetableResponse>> fillWeeklyTimetableWithDays() {
        Map<Day, List<TimetableResponse>> weeklyTimetable = new EnumMap<>(Day.class);

        for (Day day : Day.values()) {
            weeklyTimetable.put(day, new ArrayList<>());
        }

        return weeklyTimetable;
    }
}
