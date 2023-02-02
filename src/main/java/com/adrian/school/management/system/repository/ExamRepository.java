package com.adrian.school.management.system.repository;

import com.adrian.school.management.system.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Integer> {
}
