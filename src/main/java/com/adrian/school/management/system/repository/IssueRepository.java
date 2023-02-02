package com.adrian.school.management.system.repository;

import com.adrian.school.management.system.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Integer> {
}
