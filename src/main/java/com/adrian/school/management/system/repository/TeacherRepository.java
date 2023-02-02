package com.adrian.school.management.system.repository;

import com.adrian.school.management.system.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
}
