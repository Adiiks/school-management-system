package com.adrian.school.management.system.repository;

import com.adrian.school.management.system.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
