package com.dileep.interview_tracker.repository;

import com.dileep.interview_tracker.entity.MockInterview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MockInterviewRepository extends JpaRepository<MockInterview, Long> {
    List<MockInterview> findByCompanyId(Long companyId);
}