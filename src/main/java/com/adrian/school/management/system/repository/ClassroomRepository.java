package com.adrian.school.management.system.repository;

import com.adrian.school.management.system.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {

    boolean existsByNumber(Integer number);
}
