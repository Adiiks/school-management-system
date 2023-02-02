package com.adrian.school.management.system.repository;

import com.adrian.school.management.system.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    Page<Attendance> findAllByStudentEmail(String studentEmail, Pageable pageable);
}
