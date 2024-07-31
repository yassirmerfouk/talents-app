package com.pulse.repository;

import com.pulse.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {

    boolean existsByToken(String token);
    Optional<ConfirmationToken> findByToken(String token);
}
