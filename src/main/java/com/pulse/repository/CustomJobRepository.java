package com.pulse.repository;

import com.pulse.enumeration.JobStatus;
import com.pulse.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomJobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByStatus(JobStatus status, Pageable pageable);
    Page<Job> findByStatusAndClientIdNot(JobStatus status,Long clientId ,Pageable pageable);

    @Query(
            "SELECT job FROM Job job where (job.title like %:keyword% or job.sector like %:keyword% or job.description like %:keyword%) and job.status = 'OPEN'"
    )
    Page<Job> searchJobs(@Param("keyword") String keyword, Pageable pageable);

    @Query(
            "SELECT job FROM Job job where (job.title like %:keyword% or job.sector like %:keyword% or job.description like %:keyword%) and job.status = 'OPEN' and job.client.id != :userId"
    )
    Page<Job> searchJobs(@Param("keyword") String keyword,@Param("userId") Long userId, Pageable pageable);

    Page<Job> findByClientId(Long clientId, Pageable pageable);

    List<Job> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Job> findByCreatedAtBetweenAndStatus(LocalDateTime startDate, LocalDateTime endDate, JobStatus jobStatus);
}
