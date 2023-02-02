package com.adrian.school.management.system.repository;

import com.adrian.school.management.system.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findByIdIsIn(Collection<Integer> ids);

    Optional<Student> findByEmail(String studentEmail);
}
