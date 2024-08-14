package com.pulse.repository;

import com.pulse.enumeration.SelectionStatus;
import com.pulse.model.Selection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectionRepository extends JpaRepository<Selection, Long> {

    Page<Selection> findByClientId(Long clientId, Pageable pageable);

    Page<Selection> findByStatus(SelectionStatus status, Pageable pageable);
}
