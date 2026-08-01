package com.dileep.interview_tracker.repository;

import com.dileep.interview_tracker.entity.ApplicationStage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationStageRepository extends JpaRepository<ApplicationStage, Long> {
List<ApplicationStage> findByCompanyIdOrderByDateChangedDescIdDesc(Long companyId);}