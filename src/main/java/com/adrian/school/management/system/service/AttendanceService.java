package com.adrian.school.management.system.service;

import com.adrian.school.management.system.converter.AttendanceConverter;
import com.adrian.school.management.system.dto.AttendanceList;
import com.adrian.school.management.system.dto.AttendanceResponse;
import com.adrian.school.management.system.entity.Attendance;
import com.adrian.school.management.system.entity.AttendanceStatus;
import com.adrian.school.management.system.entity.Student;
import com.adrian.school.management.system.repository.AttendanceRepository;
import com.adrian.school.management.system.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final ExemptionService exemptionService;
    private final AttendanceConverter attendanceConverter;

    @Transactional
    public void checkAttendance(AttendanceList attendanceList) {
        List<Student> students = studentRepository.findByIdIsIn(attendanceList.studentList().keySet());

        List<Attendance> studentAttendanceList = new ArrayList<>();

        for (Student student : students) {
            boolean attendanceStatus = attendanceList.studentList().get(student.getId());

            Attendance studentAttendance = Attendance.builder()
                    .date(attendanceList.date())
                    .student(student)
                    .build();

            setAttendanceStatus(attendanceStatus, studentAttendance);

            studentAttendanceList.add(studentAttendance);
        }

        attendanceRepository.saveAll(studentAttendanceList);
    }

    private void setAttendanceStatus(boolean attendanceStatus, Attendance attendance) {
        if (attendanceStatus) {
            attendance.setStatus(AttendanceStatus.PRESENT);
        }
        else {
            if (exemptionService.isStudentExcused(attendance.getStudent(), attendance.getDate())) {
                attendance.setStatus(AttendanceStatus.EXCUSED);
            }
            else {
                attendance.setStatus(AttendanceStatus.ABSENT);
            }
        }
    }

    public Page<AttendanceResponse> getStudentAttendances(String studentEmail, Pageable pageable) {
        Page<Attendance> studentAttendancePage = attendanceRepository.findAllByStudentEmail(studentEmail, pageable);

        List<AttendanceResponse> studentAttendanceList = studentAttendancePage.getContent()
                .stream()
                .map(attendanceConverter::convertAttendanceToAttendanceResponse)
                .toList();

        return new PageImpl<>(studentAttendanceList, studentAttendancePage.getPageable(),
                studentAttendancePage.getTotalElements());
    }
}
