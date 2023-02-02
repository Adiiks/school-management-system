package com.adrian.school.management.system.repository;

import com.adrian.school.management.system.dto.ResultReport;
import com.adrian.school.management.system.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Integer> {

    List<Result> findAllByStudentEmailAndCourseName(String userEmail, String courseName);

    @Query("SELECT new com.adrian.school.management.system.dto.ResultReport(r.course.name, AVG(r.mark)) FROM Result r " +
            "WHERE r.student.email = :studentEmail " +
            "GROUP BY r.course")
    List<ResultReport> getFinalMarksForStudent(String studentEmail);
}
