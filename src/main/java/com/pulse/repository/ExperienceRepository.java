package com.pulse.repository;

import com.pulse.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    @Query(
            "select count (experience) from Experience experience"
            + " where "
            + "(experience.title like %:skill% or experience.description like %:skill%)"
            +" and experience.talent.id = :talentId"
    )
    long countBySkill(@Param("talentId") Long talentId,@Param("skill") String skill);


    @Query(
            "select experience from Experience experience"
            + " where "
            + "(experience.title like %:skill% or experience.description like %:skill%)"
            +" and experience.talent.id = :talentId"
    )
    List<Experience> findBySkill(@Param("talentId") Long talentId,@Param("skill") String skill);
}
