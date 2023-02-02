package com.adrian.school.management.system.repository;

import com.adrian.school.management.system.entity.Exemption;
import com.adrian.school.management.system.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface ExemptionRepository extends JpaRepository<Exemption, Integer> {

    @Query("select (count(e) > 0) from Exemption e " +
            "where e.student = :student " +
            "AND e.startDate <= :date " +
            "AND e.endDate >= :date")
    boolean isExemptionExistsForStudentAtDate(Student student, LocalDate date);
}
