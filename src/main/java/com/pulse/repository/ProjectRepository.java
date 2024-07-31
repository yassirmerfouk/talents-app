package com.pulse.repository;

import com.pulse.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query(
            "select count(project) from Project project"
            + " where "
            + "(project.title like %:skill% or project.shortDescription like %:skill% or project.longDescription like %:skill%)"
            + " and project.talent.id = :talentId"
    )
    long countBySkill(@Param("talentId") Long talentId,@Param("skill") String skill);
}
