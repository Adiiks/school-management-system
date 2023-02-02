package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.ClassroomConverter;
import com.adrian.school.management.system.dto.ClassroomRequest;
import com.adrian.school.management.system.dto.ClassroomResponse;
import com.adrian.school.management.system.entity.Classroom;
import com.adrian.school.management.system.entity.Teacher;
import com.adrian.school.management.system.repository.ClassroomRepository;
import com.adrian.school.management.system.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final TeacherRepository teacherRepository;
    private final ClassroomConverter classroomConverter;

    @Transactional
    public void createClassroom(ClassroomRequest classroomRequest) {
        isClassroomNumberUnique(classroomRequest.number());

        Teacher teacher = findTeacher(classroomRequest.teacherId());

        Classroom classroom = Classroom.builder()
                .number(classroomRequest.number())
                .teacher(teacher)
                .build();

        classroomRepository.save(classroom);
    }

    private void isClassroomNumberUnique(Integer number) {
        if (classroomRepository.existsByNumber(number)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Classroom with number " + number + " already exists.");
        }
    }

    private Teacher findTeacher(Integer teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Teacher with id " + teacherId + " not found."));
    }

    public ClassroomResponse getClassroom(Integer classroomId) {
        return classroomRepository.findById(classroomId)
                .map(classroomConverter::convertClassroomToClassroomResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Classroom with id " + classroomId + " not found."));
    }
}
