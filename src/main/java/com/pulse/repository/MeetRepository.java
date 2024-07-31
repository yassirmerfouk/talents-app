package com.pulse.repository;

import com.pulse.model.Meet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface MeetRepository extends JpaRepository<Meet, Long> {

    Page<Meet> findBySenderId(Long senderId, Pageable pageable);
    Page<Meet> findBySenderIdAndDateBetween(Long senderId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Meet> findByReceiversId(Long receiverId, Pageable pageable);

    Page<Meet> findByReceiversIdAndDateBetween(Long receiverId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
