package com.pulse.repository;

import com.pulse.model.JobInterview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobInterviewRepository extends JpaRepository<JobInterview, Long> {

    List<JobInterview> findByApplicationId(Long applicationId);
}
