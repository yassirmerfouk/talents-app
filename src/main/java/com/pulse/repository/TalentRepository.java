package com.pulse.repository;

import com.pulse.enumeration.VerificationStatus;
import com.pulse.model.Talent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TalentRepository extends JpaRepository<Talent, Long> {

    Page<Talent> findByEnabledTrue(Pageable pageable);
    Page<Talent> findByStatusAndEnabledTrue(VerificationStatus status, Pageable pageable);
    Page<Talent> findByStatusAndIdNotAndEnabledTrue(VerificationStatus status, Long userId, Pageable pageable);

    @Query(
            "select count(talent) > 0 from Talent talent join talent.skills skill where talent.id = :talentId and skill.title = :skill"
    )
    boolean hasSkill(@Param("talentId") Long talentId,@Param("skill") String skill);

    Page<Talent> findByFirstNameLikeOrLastNameLikeAndEnabledTrue(
            String firstName,
            String lastName,
            Pageable pageable
    );

    Page<Talent> findByFirstNameLikeOrLastNameLikeAndStatusAndEnabledTrue(
            String firstName,
            String lastName,
            VerificationStatus status,
            Pageable pageable
    );

    Long countByEnabledTrue();
}
