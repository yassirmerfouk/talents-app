package com.pulse.repository;

import com.pulse.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    Optional<Skill> findByTitle(String title);

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title);
}
