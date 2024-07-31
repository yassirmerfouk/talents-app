package com.pulse.repository;

import com.pulse.model.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByJobIdAndTalentId(Long jobId, Long talentId);

    Optional<Application> findByJobIdAndTalentId(Long jobId, Long talentId);

    Page<Application> findByJobId(Long jobId, Pageable pageable);

    List<Application> findByJobIdAndSelectedTrue(Long jobId);

    Page<Application> findByJobIdAndSelectedTrue(Long jobId, Pageable pageable);

    List<Application> findByJobIdAndApprovedTrue(Long jobId);

    Page<Application> findByJobIdAndApprovedTrue(Long jobId, Pageable pageable);

    Long countByJobId(Long jobId);
}
