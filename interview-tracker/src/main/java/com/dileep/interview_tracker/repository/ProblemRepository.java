package com.dileep.interview_tracker.repository;

import com.dileep.interview_tracker.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findByUserId(Long userId);
    List<Problem> findByCompanyId(Long companyId);
}