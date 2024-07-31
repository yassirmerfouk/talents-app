package com.pulse.repository;

import com.pulse.enumeration.VerificationStatus;
import com.pulse.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Page<Client> findByEnabledTrue(Pageable pageable);

    Page<Client> findByStatusAndEnabledTrue(VerificationStatus status, Pageable pageable);
    Page<Client> findByStatusAndIdNotAndEnabledTrue(VerificationStatus status, Long userId, Pageable pageable);

    Long countByEnabledTrue();
}
