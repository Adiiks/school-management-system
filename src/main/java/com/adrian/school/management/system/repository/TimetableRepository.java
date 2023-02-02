package com.adrian.school.management.system.repository;

import com.adrian.school.management.system.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Integer> {

    List<Timetable> findAllByStudentsEmailOrderByTime(String studentEmail);
}
